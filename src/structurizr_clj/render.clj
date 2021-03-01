(ns structurizr-clj.render
  (:require [clojure.java.io :as io])
  (:import (com.structurizr.io.mermaid MermaidWriter)
           (com.structurizr.io.plantuml StructurizrPlantUMLWriter)
           (com.structurizr.util WorkspaceUtils)
           (java.io ByteArrayOutputStream)
           (java.nio.charset Charset)
           (net.sourceforge.plantuml FileFormat
                                     FileFormatOption
                                     SourceStringReader)))
(defn plantuml
  "Receives a structurizr.View and returns the plantUML encoding as string"
  [view]
  (let [writer (StructurizrPlantUMLWriter.)]
    (.toString writer view)))

(defn plantuml->svg
  [plantuml-str]
  (let [reader (SourceStringReader. plantuml-str)]
    (with-open [output-stream (ByteArrayOutputStream.)]
      (.generateImage reader output-stream (FileFormatOption. FileFormat/SVG))
      (String. (.toByteArray output-stream) (Charset/forName "UTF-8")))))

(defn svg-writer
  [view path]
  (let [plantuml-str (plantuml view)
        svg          (plantuml->svg plantuml-str)]
    (io/make-parents path)
    (spit path svg)))

(defn mermaid
  "Receives a structurizr.View and returns the mermaid encoding as string"
  [view]
  (let [writer (MermaidWriter.)]
    (.toString writer view)))

(defn plantuml-writer
  "Receives a structurizr.View and a path and writes the plantUML code to the file"
  [view path]
  (io/make-parents path)
  (spit path (plantuml view)))

(defn mermaid-writer
  "Receives a structurizr.View and a path and writes the mermaid code to the file"
  [view path]
  (io/make-parents path)
  (spit path (mermaid view)))

(defn workspace->json
  "Writes a JSON file to the given path with the workspace data"
  [workspace file-path]
  (WorkspaceUtils/saveWorkspaceToJson workspace (io/file file-path)))

(defn json->workspace
  "Creates a workspace from a JSON defined in the given path"
  [file-path]
  (WorkspaceUtils/loadWorkspaceFromJson (io/file file-path)))
