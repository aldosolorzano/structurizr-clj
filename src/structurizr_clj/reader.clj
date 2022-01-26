(ns structurizr-clj.reader
  (:require [structurizr-clj.core :refer [defmodel defstyles defviews defworkspace] :as structurizr]
            [structurizr-clj.shape :as structurizr.shape]
            [structurizr-clj.style :as structurizr.style]
            [structurizr-clj.tags :as structurizr.tags]
            [structurizr-clj.view :as structurizr.view]
            [structurizr-clj.render :as render]))

(defrecord UsesLink [to-node description technology])

(defn uses-reader [[to-node description technology]]
  (->UsesLink to-node description technology))

(defn uses! [instances coll]
  (doall (for [{:keys [uses key]} coll]
           (run! (fn [{:keys [to-node description technology]}]
                   (structurizr/uses (get instances key)
                                     (get instances to-node)
                                     description
                                     technology))
                 uses))))

(defn read-workspace [workspace-data]
  (let [workspace  (structurizr/new-workspace (:key workspace-data) (:description workspace-data))
        model      (structurizr/model workspace)
        sf-systems (get-in workspace-data [:model :software-systems])
        instances  (loop [software-systems sf-systems
                          instances        {}]
                     (if (empty? software-systems)
                       instances
                       (let [sf-data             (first software-systems)
                             sf-key              (:key sf-data)
                             sf-instance         (structurizr/add-software-system model
                                                                                  sf-key
                                                                                  (:description sf-data)
                                                                                  (:tags sf-data))
                             containers-instance (reduce (fn [initial container]
                                                           (merge initial
                                                                  {(:key container) (structurizr/add-container sf-instance
                                                                                                               (:key container)
                                                                                                               (:description container)
                                                                                                               (:technology container)
                                                                                                               (:tags container))}))
                                                         {}
                                                         (:containers sf-data))]


                         (recur (rest software-systems)
                                (merge instances containers-instance {sf-key sf-instance})))))]
    (run! #(uses! instances (get % :containers)) sf-systems)
    (uses! instances sf-systems)
    workspace))

(def new-example
  {:key         "Getting Started"
   :description "This is a model of my software system"
   :model      {:persons          [{:key         "User"
                                    :description "A user of my software system"
                                    :tags        #{structurizr.tags/person}
                                    :uses        [#structurizr/use ["Yo" "Uses" ""]]}]
                :software-systems [{:key         "External software system"
                                    :description "External system used"
                                    :containers  []
                                    :uses        [#structurizr/use ["Yo" "Uses" "Logging"]]}
                                   {:key         "Software System"
                                    :description "My software system"
                                    :containers  [{:key         "Yo"
                                                   :description "Service"
                                                   :technology  "Clojure"
                                                   :tags        #{"Main"}
                                                   :uses        [#structurizr/use ["Database" "Uses" "Datomic"]]}
                                                  {:key         "Database"
                                                   :description "Main database"
                                                   :technology  "Datomic"
                                                   :tags        #{"Database"}}]}]}
   :styles []})
