(ns structurizr-clj.core
  (:import (com.structurizr Workspace)))

;; Tags

(defn add-tags
  [item tags]
  (if (empty? tags)
    item
    (do
      (.addTags item (into-array tags))
      item)))

;; Workspace

(defn new-workspace
  [key description]
  (Workspace. key description))

(defn model
  [workspace]
  (.getModel workspace))

;; Model

(defn add-person
  ([model key description technology]
   (add-person model key description technology []))
  ([model key description technology tags]
   (let [person (.addPerson model key description technology)]
     (add-tags person tags))))

(defn add-software-system
  ([model key description]
   (add-software-system model key description []))
  ([model key description tags]
   (let [software-system (.addSoftwareSystem model key description )]
     (add-tags software-system tags))))

(defn add-container
  ([model key description technology]
   (add-container model key description technology []))
  ([model key description technology tags]
   (let [container (.addContainer model key description technology)]
     (add-tags container tags))))

(defn uses
  [node-a node-b description sub]
  (.uses node-a node-b description sub))

;; Views

(defn views
  "Get views from workspace"
  [workspace]
  (.getViews workspace))

(defn create-container-view
  "Creates ContainerView for given software-system"
  [views software-system key description]
  (.createContainerView views software-system key description))

(defn create-system-context-view
  "Creates SystemContextView for given software-system"
  [views software-system key description]
  (.createSystemContextView views software-system key description))

(defn configuration
  "Gets configuration for given views"
  [views]
  (.getConfiguration views))

(defn styles
  "Get styles HashSet for given views"
  [views]
  (.getStyles (configuration views)))

(defn add-element-style
  [styles tag]
  (.addElementStyle styles tag))

(defn background
  "Add background to style-item"
  [style-item hex]
  (.background style-item hex))

(defn color
  "Add color to style-item"
  [style-item hex]
  (.color style-item hex))

(defn shape
  "Add shape to style-item"
  [style-item shape-name]
  (.shape style-item  shape-name))

;; Renders

(defn add-all-software-systems
  [view]
  (.addAllSoftwareSystems view))

(defn add-all-containers
  [view]
  (.addAllContainers view))

(defn add-all-elements
  [view]
  (.addAllElements view))

(defmacro defworkspace
  "NOTE: Not finished yet, Creates a workspace and binds it to Var name"
  [& params]
  (let [workspace-name             (first params)
        [_ workspace :as bindings] (second params)
        model                      (last params)]
    `(let ~bindings
       ~model
       ~workspace)))

;; Main Macros

(defmacro defmodel
  [system-bindings
   container-bindings
   component-bindings & body]
  `(let ~(into [] (concat system-bindings container-bindings component-bindings))
     ~@body))

(defmacro defviews
  [bindings
   styles
   & renders]
  `(let ~bindings
     ~styles
     ~@renders))

(defmacro defstyles
  [bindings
   & definitions]
  `(let ~bindings
     ~@definitions))
