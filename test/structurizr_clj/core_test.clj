(ns structurizr-clj.core-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.core :refer [defworkspace defmodel defviews defstyles] :as structurizr]))

(defworkspace my-workspace
  [workspace (structurizr/new-workspace "My Workspace" " My Workspace Architecture")]
  (defmodel [model           (structurizr/model workspace)
             software-system (structurizr/add-software-system model "My System" "System")]
            [yo-service (structurizr/add-container software-system "Yo" "Service" "Clojure" ["Main"])
             database   (structurizr/add-container software-system "Database" "Main database" "Datomic" ["Database"])]
            []
    (structurizr/uses yo-service database "Persists data" "Datomic")
    (defviews [views           (structurizr/views workspace)
               containers-view (structurizr/create-container-view views software-system "Containers" "Chargeback services")]
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
