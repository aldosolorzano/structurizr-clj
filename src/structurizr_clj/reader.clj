(ns structurizr-clj.reader
  (:require [structurizr-clj.core  :as structurizr]
            [structurizr-clj.shape :as structurizr.shape]
            [structurizr-clj.style :as structurizr.style]
            [structurizr-clj.tags :as structurizr.tags]
            [structurizr-clj.view :as structurizr.view]
            [structurizr-clj.render :as render]))

(defrecord UsesLink [to-node description technology])

(defn uses-reader [[to-node description technology]]
  (->UsesLink to-node description technology))

(defn read-workspace [workspace-data]
  (let [workspace  (structurizr/new-workspace (:key workspace-data) (:description workspace-data))
        model      (structurizr/model workspace)
        sf-systems (get-in workspace-data [:model :software-systems])
        persons    (get-in workspace-data [:model :persons])
        [p-instances p-relationships]
        (loop [ppersons persons
               instances {}
               relationships []]
          (if (empty? ppersons)
            [instances relationships]
            (let [person (first ppersons)
                  person-instance (structurizr/add-person model
                                                          (:key person)
                                                          (:description person)
                                                          (:tags person))
                  current-relationships [(:key person) (:uses person)]]
              (recur (rest persons)
                     (merge instances {(:key person) person-instance})
                     (conj relationships current-relationships)))))

        [instances relationships]
        (loop [software-systems sf-systems
               instances        {}
               relationships    []]
          (if (empty? software-systems)
            [instances relationships]
            (let [sf-data (first software-systems)
                  sf-key (:key sf-data)
                  sf-instance (structurizr/add-software-system model
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
                                              (:containers sf-data))
                  containers-rels (->> (:containers sf-data)
                                       (filter :uses)
                                       (mapv (fn [{:keys [key uses]}] [key uses])))
                  current-relationships (if (:uses sf-data)
                                          (concat relationships containers-rels [[sf-key (:uses sf-data)]])
                                          (concat relationships containers-rels))]


              (recur (rest software-systems)
                     (merge instances containers-instance {sf-key sf-instance})
                     current-relationships))))
        instances-index (merge instances p-instances)
        full-relationships (concat relationships p-relationships)]

    (doall (for [[from to-nodes] full-relationships
                 :let [from-instance (get instances-index from)]]
             (run! (fn [{:keys [to-node description technology]}]
                     (structurizr/uses from-instance
                                       (get instances-index to-node)
                                       description
                                       technology))
                   to-nodes)))
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
