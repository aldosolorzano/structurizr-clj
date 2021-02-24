(ns structurizr-clj.core
  (:import (com.structurizr Workspace)
           (com.structurizr.api StructurizrClient)))

;; Tags

(defn add-tags
  "Add given tags to given element"
  [element tags]
  (if (empty? tags)
    element
    (do
      (.addTags element (into-array tags))
      element)))

;; Workspace

(defn new-workspace
  [key description]
  (Workspace. key description))

(defn model
  [workspace]
  (.getModel workspace))

(defn views
  "Get views from workspace"
  [workspace]
  (.getViews workspace))

;; Model

(defn add-person
  ([model key description]
   (add-person model key description []))
  ([model key description tags]
   (let [person (.addPerson model key description)]
     (add-tags person tags))))

(defn add-software-system
  ([model key description]
   (add-software-system model key description []))
  ([model key description tags]
   (let [software-system (.addSoftwareSystem model key description)]
     (add-tags software-system tags))))

(defn add-container
  ([sofware-system key description technology]
   (add-container sofware-system key description technology []))
  ([software-system key description technology tags]
   (let [container (.addContainer software-system key description technology)]
     (add-tags container tags))))

(defn add-component
  ([container key description technology]
   (add-component container key description technology []))
  ([container key description technology tags]
   (let [component (.addComponent container key description technology)]
     (add-tags component tags))))

(defn uses
  ([node-a node-b description]
   (.uses node-a node-b description))
  ([node-a node-b description technology]
   (.uses node-a node-b description technology)))

(defmacro defworkspace
  "Creates a workspace and binds it to Var name"
  [& params]
  (let [workspace-name                   (first params)
        [workspace-binding :as bindings] (second params)
        model                            (last params)]
    `(let ~bindings
       ~model
       (def ~workspace-name ~workspace-binding))))

(defmacro defmodel
  "Somewhat a let wrapper, it receives three vector of bindings to improve code structure when creating diagrams"
  [system-bindings
   container-bindings
   component-bindings & body]
  `(let ~(into [] (concat system-bindings container-bindings component-bindings))
     ~@body))

(defmacro defviews
  "A let wrapper to improve code readability and structure when creating diagrams"
  [bindings
   styles
   & renders]
  `(let ~bindings
     ~styles
     ~@renders))

(defmacro defstyles
  "A let wrapper to improve code readability and structure when creating diagrams"
  [bindings
   & definitions]
  `(let ~bindings
     ~@definitions))

(defn client
  ([api-key api-secret]
   (StructurizrClient. api-key api-secret))
  ([url api-key api-secret]
   (StructurizrClient. url api-key api-secret)))

(defn publish-workspace
  [client workscape-id workspace]
  (.putWorkspace client workscape-id workspace))
