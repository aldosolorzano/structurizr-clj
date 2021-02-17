(ns structurizr-clj.render
  (:import (com.structurizr.io.mermaid MermaidWriter)
           (com.structurizr.io.plantuml StructurizrPlantUMLWriter)))

(defn get-key
  [view]
  (.getKey view))

(defn system-landscape-views
  [views]
  (.getSystemLandscapeViews views))

(defn system-context-views
  [views]
  (.getSystemContextViews views))

(defn container-views
  [views]
  (.getContainerViews views))

(defn component-views
  [views]
  (.getComponentViews views))

(defn plantuml
  "Receives a structurizr.View and returns the plantUML encoding as string"
  [view]
  (let [writer (StructurizrPlantUMLWriter.)]
    (.toString writer view)))

(defn mermaid
  "Receives a structurizr.View and returns the mermaid encoding as string"
  [view]
  (let [writer (MermaidWriter.)]
    (.toString writer view)))

(defn plantuml-writer
  "Receives a structurizr.View and a path and writes the plantUML code to the file"
  [view path]
  (spit path (plantuml view)))

(defn mermaid-writer
  "Receives a structurizr.View and a path and writes the mermaid code to the file"
  [view path]
  (spit path (mermaid view)))
