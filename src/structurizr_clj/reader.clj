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

;; TODO
;; 1. Allow more than one relationship
;; 2. Allow to be able to add a relationship with containers from another sf-system
;; 3. Support for views
;; 4. Support for styles

;; SUPORTS
;; 1. All relationships should be in the same software system
;; 2. Only one relationship per container
;; 3. Relationships only happen at container level

(defn read-workspace [workspace-data]
  (let [workspace  (structurizr/new-workspace (:key workspace-data) (:description workspace-data))
        model      (structurizr/model workspace)
        sf-systems (get-in workspace-data [:model :software-systems])]
    (doall (for [sf-data sf-systems]
             (let [sf                  (structurizr/add-software-system model (:key sf-data) (:description sf-data) (:tags sf-data))
                   containers-instance (reduce (fn [initial container]
                                                 (merge initial
                                                        {(:key container) (structurizr/add-container sf
                                                                                                     (:key container)
                                                                                                     (:description container)
                                                                                                     (:technology container)
                                                                                                     (:tags container))}))
                                               {}
                                               (:containers sf-data))]
               (run! (fn [{:keys [uses] :as container}]
                       (when uses
                         (structurizr/uses (get containers-instance (:key container))
                                           (get containers-instance (:to-node uses))
                                           (:description uses)
                                           (:technology uses))))
                     (:containers sf-data)))))
    workspace))

#_(render/workspace->json (read-sofware-systems new-example) "hola.json")

(def new-example
  {:key         "Getting Started"
   :description "This is a model of my software system"
   :model      {:persons          [{:key         "User"
                                    :description "A user of my software system"
                                    :tags        #{structurizr.tags/person}
                                    :uses        ""}]
                :software-systems [{:key         "Software System"
                                    :description "My software system"
                                    :containers  [{:key         "Yo"
                                                   :description "Service"
                                                   :technology  "Clojure"
                                                   :tags        #{"Main"}
                                                   :uses        #structurizr/use ["Database" "Uses" "Datomic"]}
                                                  {:key         "Database"
                                                   :description "Main database"
                                                   :technology  "Datomic"
                                                   :tags        #{"Database"}}]}]}
   :views       []
   :styles      []})
