(ns structurizr-clj.shape-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.shape :as structurizr.shape])
  (:import (com.structurizr.view Shape)))

(deftest shape-test
  (is (= structurizr.shape/box Shape/Box))
  (is (= structurizr.shape/rounded-box Shape/RoundedBox))
  (is (= structurizr.shape/circle Shape/Circle))
  (is (= structurizr.shape/ellipse Shape/Ellipse))
  (is (= structurizr.shape/hexagon Shape/Hexagon))
  (is (= structurizr.shape/cylinder Shape/Cylinder))
  (is (= structurizr.shape/pipe Shape/Pipe))
  (is (= structurizr.shape/person Shape/Person))
  (is (= structurizr.shape/robot Shape/Robot))
  (is (= structurizr.shape/folder Shape/Folder))
  (is (= structurizr.shape/web-browser Shape/WebBrowser))
  (is (= structurizr.shape/mobile-device-portrait Shape/MobileDevicePortrait))
  (is (= structurizr.shape/mobile-device-landscape Shape/MobileDeviceLandscape))
  (is (= structurizr.shape/component Shape/Component)))
