(ns structurizr-clj.core-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.core :refer [defmodel defstyles defviews defworkspace] :as structurizr]
            [structurizr-clj.shape :as structurizr.shape]
            [structurizr-clj.style :as structurizr.style]
            [structurizr-clj.tags :as structurizr.tags]
            [structurizr-clj.view :as structurizr.view]))

(defworkspace my-workspace
  [workspace (structurizr/new-workspace "Getting Started" "This is a model of my software system")]
  (defmodel [model           (structurizr/model workspace)
             user            (structurizr/add-person model "User" "A user of my software system" [structurizr.tags/person])
             software-system (structurizr/add-software-system model "Software System" "My software system")]
            [yo-service (structurizr/add-container software-system "Yo" "Service" "Clojure" ["Main"])
             tu-service (structurizr/add-container software-system "Tu" "Service" "Clojure")
             database   (structurizr/add-container software-system "Database" "Main database" "Datomic" ["Database"])]
            []
    (structurizr/uses user software-system "Uses")
    (structurizr/uses tu-service yo-service "Uses")
    (structurizr/uses yo-service database "Persists data" "Datomic")
    (defviews [views                (structurizr/views workspace)
               containers-view      (structurizr.view/create-container views software-system "Containers" "An example of Container context diagram")
               software-system-view (structurizr.view/create-system-context views software-system "System Context" "An example of a System Context diagram")]
      (defstyles [styles (structurizr.view/styles views)]
        (doto (structurizr.style/add-element styles structurizr.tags/person)
              (structurizr.style/shape structurizr.shape/person))
        (doto (structurizr.style/add-element styles "Database")
              (structurizr.style/shape structurizr.shape/cylinder))
        (doto (structurizr.style/add-element styles "Main")
              (structurizr.style/background "#800080")
              (structurizr.style/color "#ffffff")))
      (doto software-system-view
            structurizr.view/add-software-systems
            (structurizr.view/add-element user))
      (doto containers-view
            structurizr.view/add-software-systems
            structurizr.view/add-containers
            (structurizr.view/remove-element tu-service)))))

(deftest workspace-test
  (testing "Nothing breaks and the workspace is a com.structurizr.Workspace"
    (is (= com.structurizr.Workspace (class my-workspace)))))

(def client (structurizr/client "api-key" "api-secret"))
(def client-2 (structurizr/client "http://localhost" "api-key" "api-secret"))

(deftest client-test
  (testing "Client returns a structurizr client"
    (is (= com.structurizr.api.StructurizrClient (class client)))
    (is (= "http://localhost" (.getUrl client-2)))))

