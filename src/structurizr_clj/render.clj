(ns structurizr-clj.render
  (:import (com.structurizr.io.plantuml StructurizrPlantUMLWriter)
           (com.structurizr.io.mermaid MermaidWriter)))

(defn get-key [view]
  (.getKey view))

(defn system-landscape-views
  [views]
  (.getSystemLandscapetViews views))

(defn system-context-views
  [views]
  (.getSystemContextViews views))

(defn container-views
  [views]
  (.getContainerViews views))

(defn component-views
  [views]
  (.getComponentViews views))

(defn plantUML-writer
  [view path]
  (let [writer (StructurizrPlantUMLWriter.)]
    (spit path (.toString writer view))))

(defn mermaid-writer
  [view path]
  (let [writer (MermaidWriter.)]
    (spit path (.toString writer view))))
