(ns structurizr-clj.view-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.core :as structurizr]
            [structurizr-clj.render :as structurizr.render]
            [structurizr-clj.view :as structurizr.view]))

(def workspace (structurizr.render/json->workspace "resources/json-workspaces/views-workspace-test.json"))
(def views (structurizr/views workspace))
(def container-view (first (structurizr.view/containers views)))

(deftest get-key-test
  (is (= "Containers view" (structurizr.view/get-key container-view))))

(deftest get-functions-test
  (is (= com.structurizr.view.SystemLandscapeView (class (first (structurizr.view/system-landscapes views)))))
  (is (= com.structurizr.view.SystemContextView (class (first (structurizr.view/system-contexts views)))))
  (is (= com.structurizr.view.ContainerView (class (first (structurizr.view/containers views)))))
  (is (= com.structurizr.view.ComponentView (class (first (structurizr.view/components views))))))
