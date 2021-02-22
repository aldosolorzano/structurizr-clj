(ns structurizr-clj.core-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.core :refer [defmodel defstyles defviews defworkspace] :as structurizr]
            [structurizr-clj.shape :as structurizr.shape]
            [structurizr-clj.tags :as structurizr.tags]))

(defworkspace my-workspace
  [workspace (structurizr/new-workspace "Getting Started" "This is a model of my software system")]
  (defmodel [model           (structurizr/model workspace)
             user            (structurizr/add-person model "User" "A user of my software system" [structurizr.tags/person])
             software-system (structurizr/add-software-system model "Software System" "My software system")]
            [yo-service (structurizr/add-container software-system "Yo" "Service" "Clojure" ["Main"])
             database   (structurizr/add-container software-system "Database" "Main database" "Datomic" ["Database"])]
            []
    (structurizr/uses user software-system "Uses")
    (structurizr/uses yo-service database "Persists data" "Datomic")
    (defviews [views                (structurizr/views workspace)
               containers-view      (structurizr/create-container-view views software-system "Containers" "An example of Container context diagram")
               software-system-view (structurizr/create-system-context-view views software-system "System Context" "An example of a System Context diagram")]
      (defstyles [styles (structurizr/styles views)]
        (doto (structurizr/add-element-style styles structurizr.tags/person)
              (structurizr/shape structurizr.shape/person))
        (doto (structurizr/add-element-style styles "Database")
              (structurizr/shape structurizr.shape/cylinder))
        (doto (structurizr/add-element-style styles "Main")
              (structurizr/background "#800080")
              (structurizr/color "#ffffff")))
      (doto software-system-view
            structurizr/add-all-software-systems
            structurizr/add-all-people)
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
