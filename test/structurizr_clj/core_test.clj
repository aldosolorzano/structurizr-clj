(ns structurizr-clj.core-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.core :refer [defmodel defstyles defviews defworkspace] :as structurizr]))

(defworkspace my-workspace
  [workspace (structurizr/new-workspace "My Workspace" " My Workspace Architecture")]
  (defmodel [model           (structurizr/model workspace)
             software-system (structurizr/add-software-system model "My System" "System")]
            [yo-service (structurizr/add-container software-system "Yo" "Service" "Clojure" ["Main"])
             database   (structurizr/add-container software-system "Database" "Main database" "Datomic" ["Database"])]
            []
    (structurizr/uses yo-service database "Persists data" "Datomic")
    (defviews [views           (structurizr/views workspace)
               containers-view (structurizr/create-container-view views software-system "Containers" "My services")]
      (defstyles [styles (structurizr/styles views)]
        (doto (structurizr/add-element-style styles "Main")
              (.background "#800080")
              (.color "#ffffff")))
      (doto containers-view
            structurizr/add-all-software-systems
            structurizr/add-all-containers))))

(deftest workspace-test
  (testing "Nothing breaks and the workspace is a com.structurizr.Workspace"
    (is (= com.structurizr.Workspace (class my-workspace)))))

(def client (structurizr/client "api-key" "api-secret"))
(def client-2 (structurizr/client "http://localhost" "api-key" "api-secret"))

(deftest client-test
  (testing "Client returns a structurizr client"
    (is (= com.structurizr.api.StructurizrClient (class client)))
    (is (= "http://localhost" (.getUrl client-2)))))
