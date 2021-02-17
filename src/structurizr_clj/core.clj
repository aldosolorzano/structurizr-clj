(ns structurizr-clj.core
  (:import (com.structurizr Workspace)
           (com.structurizr.api StructurizrClient)))

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
  [node-a node-b description sub]
  (.uses node-a node-b description sub))

;; Views

(defn views
  "Get views from workspace"
  [workspace]
  (.getViews workspace))

(defn create-system-landscape-view
  "Creates SystemLandscape view "
  [views  key description]
  (.createSystemLandscapeView views key description))

(defn create-system-context-view
  "Creates SystemContextView for given software-system"
  [views software-system key description]
  (.createSystemContextView views software-system key description))

(defn create-container-view
  "Creates ContainerView for given software-system"
  [views software-system key description]
  (.createContainerView views software-system key description))

(defn create-component-view
  "Creates ComponentView for given container"
  [views container key description]
  (.createComponentView views container key description))

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

(defn add-all-components
  [view]
  (.addAllComponents view))

(defn add-all-elements
  [view]
  (.addAllElements view))

(defmacro defworkspace
  "NOTE: Rework todo, Creates a workspace and binds it to Var name"
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
