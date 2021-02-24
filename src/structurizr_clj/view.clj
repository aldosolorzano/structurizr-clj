(ns structurizr-clj.view)

(defn configuration
  "Gets configuration for given views"
  [views]
  (.getConfiguration views))

(defn get-key
  [view]
  (.getKey view))

(defn styles
  "Get styles HashSet for given views"
  [views]
  (.getStyles (configuration views)))

(defn system-landscapes
  [views]
  (.getSystemLandscapeViews views))

(defn system-contexts
  [views]
  (.getSystemContextViews views))

(defn containers
  [views]
  (.getContainerViews views))

(defn components
  [views]
  (.getComponentViews views))

(defn create-system-landscape
  "Creates SystemLandscape view "
  [views  key description]
  (.createSystemLandscapeView views key description))

(defn create-system-context
  "Creates SystemContextView for given software-system"
  [views software-system key description]
  (.createSystemContextView views software-system key description))

(defn create-container
  "Creates ContainerView for given software-system"
  [views software-system key description]
  (.createContainerView views software-system key description))

(defn create-component
  "Creates ComponentView for given container"
  [views container key description]
  (.createComponentView views container key description))

(defn add-element
  "Adds te given element to this view, including relationships to/from that element"
  [view element]
  (.add view element))

(defn remove-element
  "Removes an individual element from this view"
  [view element]
  (.remove view element))

(defn add-people
  "Adds all person elements to the view"
  [view]
  (.addAllPeople view))

(defn add-software-systems
  "Adds all software-systems to the view"
  [view]
  (.addAllSoftwareSystems view))

(defn add-containers
  "Adds all containers to the view"
  [view]
  (.addAllContainers view))

(defn add-components
  "Adds all components to the view"
  [view]
  (.addAllComponents view))

(defn add-elements
  "Adds all elements to the view"
  [view]
  (.addAllElements view))
