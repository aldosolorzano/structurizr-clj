(ns structurizr-clj.render-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [structurizr-clj.core :refer [defmodel defviews defworkspace] :as structurizr]
            [structurizr-clj.render :as render]))

(defworkspace my-workspace
  [workspace (structurizr/new-workspace "My Workspace" " My Workspace Architecture")]
  (defmodel [model           (structurizr/model workspace)
             software-system (structurizr/add-software-system model "My System" "System")]
            [yo-service (structurizr/add-container software-system "Yo" "Service" "Clojure" ["Main"])
             database   (structurizr/add-container software-system "Database" "Main database" "Datomic" ["Database"])]
            [http (structurizr/add-component yo-service "HTTP" "Allows http request calls" "Component")]
    (structurizr/uses yo-service database "Persists data" "Datomic")
    (defviews [views                 (structurizr/views workspace)
               system-landscape-view (structurizr/create-system-landscape-view views "System Landscape view" "My services")
               system-context-view   (structurizr/create-system-context-view views software-system "System Context view" "My services")
               containers-view       (structurizr/create-container-view views software-system "Containers view" "My services")
               component-view        (structurizr/create-component-view views yo-service "Component view" "HTTP interaction")]
      (doto system-landscape-view
            structurizr/add-all-software-systems)
      (doto system-context-view
            structurizr/add-all-software-systems
            structurizr/add-all-elements)
      (doto containers-view
            structurizr/add-all-software-systems
            structurizr/add-all-containers)
      (doto component-view
            structurizr/add-all-containers
            structurizr/add-all-components))))

(def tmp
  (str (.toString (io/file (System/getProperty "java.io.tmpdir"))) "/"))

(def plantuml-path (str tmp "plantuml-structurizr.txt"))
(def mermaid-path (str tmp "mermaid-structurizr.txt"))
(def views (structurizr/views my-workspace))
(def system-context-view (first (render/system-context-views views)))
(def container-view (first (render/container-views views)))

(deftest get-key-test
  (is (= "Containers view" (render/get-key container-view))))

(deftest get-views-functions-test
  (is (= com.structurizr.view.SystemLandscapeView (class (first (render/system-landscape-views views)))))
  (is (= com.structurizr.view.SystemContextView (class (first (render/system-context-views views)))))
  (is (= com.structurizr.view.ContainerView (class (first (render/container-views views)))))
  (is (= com.structurizr.view.ComponentView (class (first (render/component-views views))))))

(def plantuml-str "@startuml(id=Containers_view)\ntitle My System - Containers\ncaption My services\n\nskinparam {\n  shadowing false\n  arrowFontSize 10\n  defaultTextAlignment center\n  wrapWidth 200\n  maxMessageSize 100\n}\nhide stereotype\nskinparam rectangle<<2>> {\n  BackgroundColor #dddddd\n  FontColor #000000\n  BorderColor #9A9A9A\n}\nskinparam rectangle<<3>> {\n  BackgroundColor #dddddd\n  FontColor #000000\n  BorderColor #9A9A9A\n}\npackage \"My System\\n[Software System]\" {\n  rectangle \"==Database\\n<size:10>[Container: Datomic]</size>\\n\\nMain database\" <<3>> as 3\n  rectangle \"==Yo\\n<size:10>[Container: Clojure]</size>\\n\\nService\" <<2>> as 2\n}\n2 .[#707070].> 3 : \"Persists data\\n<size:8>[Datomic]</size>\"\n@enduml")
(def mermaid-str "graph TB\n  linkStyle default fill:#ffffff\n  subgraph boundary [My System]\n    3[\"<div style='font-weight: bold'>Database</div><div style='font-size: 70%; margin-top: 0px'>[Container: Datomic]</div><div style='font-size: 80%; margin-top:10px'>Main database</div>\"]\n    style 3 fill:#dddddd,stroke:#9a9a9a,color:#000000\n    2[\"<div style='font-weight: bold'>Yo</div><div style='font-size: 70%; margin-top: 0px'>[Container: Clojure]</div><div style='font-size: 80%; margin-top:10px'>Service</div>\"]\n    style 2 fill:#dddddd,stroke:#9a9a9a,color:#000000\n  end\n  style boundary fill:#ffffff,stroke:#000000,color:#000000\n  2-. \"<div>Persists data</div><div style='font-size: 70%'>[Datomic]</div>\" .->3\n")

(deftest plantuml-test
  (is (= plantuml-str (render/plantuml container-view))))

(deftest mermaid-test
  (is (= mermaid-str (render/mermaid container-view))))

(deftest plantuml-writer-test
  (testing "File is writen with view content"
    (render/plantuml-writer system-context-view plantuml-path)
    (is (true? (.exists (io/file plantuml-path))))))

(deftest mermaid-writer-test
  (testing "File is writen with view content"
    (render/mermaid-writer container-view mermaid-path)
    (is (true? (.exists (io/file mermaid-path))))))
