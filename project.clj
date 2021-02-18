(defproject structurizr-clj "0.1.0"
  :description "Wrapper for structurizr in Java"
  :url "https://github.com/aldosolorzano/structurizr-clj"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-nsorg "0.3.0"]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.structurizr/structurizr-client "1.6.3"]
                 [com.structurizr/structurizr-plantuml "1.5.0"]
                 [com.structurizr/structurizr-mermaid "1.4.0"]]

  :profiles {:dev {:dependencies [[clj-kondo "2020.05.02"]
                                  [cljfmt "0.6.7"]]}}
  :aliases {"clj-kondo" ["run" "-m" "clj-kondo.main" "--config" ".clj-kondo/config.edn" "--lint" "src" "test"]
            "cljfmt"    ["run" "-m" "cljfmt.main" "--indents" ".cljfmt-indents.edn"]
            "lint"      ["do" ["clj-kondo"] ["cljfmt" "check"] ["nsorg"]]
            "lint-fix"  ["do" ["cljfmt" "fix"] ["nsorg" "--replace"]]}
  :repl-options {:init-ns structurizr-clj.core})
