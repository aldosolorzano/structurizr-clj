(ns structurizr-clj.render-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [structurizr-clj.core :as structurizr]
            [structurizr-clj.render :as structurizr.render]
            [structurizr-clj.view :as structurizr.view]))

(def workspace (structurizr.render/json->workspace "resources/json-workspaces/views-workspace-test.json"))
(def tmp (str (.toString (io/file (System/getProperty "java.io.tmpdir"))) "/"))
(def plantuml-path (str tmp "plantuml-structurizr.txt"))
(def mermaid-path (str tmp "mermaid-structurizr.txt"))
(def svg-path (str tmp "svg-structurizr.svg"))
(def views (structurizr/views workspace))
(def system-context-view (first (structurizr.view/system-contexts views)))
(def container-view (first (structurizr.view/containers views)))
(def plantuml-str "@startuml(id=Containers_view)\ntitle My System - Containers\ncaption My services\n\nskinparam {\n  shadowing false\n  arrowFontSize 10\n  defaultTextAlignment center\n  wrapWidth 200\n  maxMessageSize 100\n}\nhide stereotype\nskinparam rectangle<<2>> {\n  BackgroundColor #dddddd\n  FontColor #000000\n  BorderColor #9A9A9A\n}\nskinparam rectangle<<3>> {\n  BackgroundColor #dddddd\n  FontColor #000000\n  BorderColor #9A9A9A\n}\npackage \"My System\\n[Software System]\" {\n  rectangle \"==Database\\n<size:10>[Container: Datomic]</size>\\n\\nMain database\" <<3>> as 3\n  rectangle \"==Yo\\n<size:10>[Container: Clojure]</size>\\n\\nService\" <<2>> as 2\n}\n2 .[#707070].> 3 : \"Persists data\\n<size:8>[Datomic]</size>\"\n@enduml")
(def mermaid-str "graph TB\n  linkStyle default fill:#ffffff\n  subgraph boundary [My System]\n    3[\"<div style='font-weight: bold'>Database</div><div style='font-size: 70%; margin-top: 0px'>[Container: Datomic]</div><div style='font-size: 80%; margin-top:10px'>Main database</div>\"]\n    style 3 fill:#dddddd,stroke:#9a9a9a,color:#000000\n    2[\"<div style='font-weight: bold'>Yo</div><div style='font-size: 70%; margin-top: 0px'>[Container: Clojure]</div><div style='font-size: 80%; margin-top:10px'>Service</div>\"]\n    style 2 fill:#dddddd,stroke:#9a9a9a,color:#000000\n  end\n  style boundary fill:#ffffff,stroke:#000000,color:#000000\n  2-. \"<div>Persists data</div><div style='font-size: 70%'>[Datomic]</div>\" .->3\n")

(deftest mermaid-test
  (is (= mermaid-str (structurizr.render/mermaid container-view))))

(deftest plantuml-test
  (is (= plantuml-str (structurizr.render/plantuml container-view))))

(deftest plantuml->svg-test
  (is (= (slurp "resources/svg-examples/container.svg")
         (structurizr.render/plantuml->svg plantuml-str))))

(deftest svg-writer-test
  (testing "File is writen with view content"
    (structurizr.render/svg-writer system-context-view svg-path)
    (is (true? (.exists (io/file svg-path))))))

(deftest plantuml-writer-test
  (testing "File is writen with view content"
    (structurizr.render/plantuml-writer system-context-view plantuml-path)
    (is (true? (.exists (io/file plantuml-path))))))

(deftest mermaid-writer-test
  (testing "File is writen with view content"
    (structurizr.render/mermaid-writer container-view mermaid-path)
    (is (true? (.exists (io/file mermaid-path))))))

(deftest json-test
  (let [path           (str tmp "/my-workspace-test.json")
        _json          (structurizr.render/workspace->json workspace path)
        my-workspace-2 (structurizr.render/json->workspace path)
        my-workspace-3 (structurizr.render/json->workspace "resources/json-workspaces/my-workspace-test.json")]
    (is (= (.getName workspace) (.getName my-workspace-2)))
    (is (= (.getDescription workspace) (.getDescription my-workspace-2)))
    (is (= "Getting Started" (.getName my-workspace-3)))))
