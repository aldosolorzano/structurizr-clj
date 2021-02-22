(ns structurizr-clj.tags-test
  (:require [clojure.test :refer :all]
            [structurizr-clj.tags :as structurizr.tags])
  (:import (com.structurizr.model Tags)))

(deftest tags-test
  (is (= structurizr.tags/person Tags/PERSON))
  (is (= structurizr.tags/element Tags/ELEMENT))
  (is (= structurizr.tags/relationship Tags/RELATIONSHIP))
  (is (= structurizr.tags/software-system Tags/SOFTWARE_SYSTEM))
  (is (= structurizr.tags/container Tags/CONTAINER))
  (is (= structurizr.tags/component Tags/COMPONENT))
  (is (= structurizr.tags/deployment-node Tags/DEPLOYMENT_NODE))
  (is (= structurizr.tags/infrastructure-node Tags/INFRASTRUCTURE_NODE))
  (is (= structurizr.tags/software-system-instance Tags/SOFTWARE_SYSTEM_INSTANCE))
  (is (= structurizr.tags/container-instance Tags/CONTAINER_INSTANCE))
  (is (= structurizr.tags/synchronous Tags/SYNCHRONOUS))
  (is (= structurizr.tags/asynchronous Tags/ASYNCHRONOUS)))
