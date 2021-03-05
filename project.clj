(defproject structurizr-clj "0.2.1"
  :description "Wrapper for structurizr in Java"
  :url "https://github.com/aldosolorzano/structurizr-clj"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-nsorg "0.3.0"]]

  :deploy-repositories [["clojars" {:url           "https://repo.clojars.org"
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password
                                    :sign-releases false}]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.structurizr/structurizr-client "1.6.3"]
                 [com.structurizr/structurizr-plantuml "1.5.0"]
                 [com.structurizr/structurizr-mermaid "1.4.0"]
                 [net.sourceforge.plantuml/plantuml "1.2021.1"]]

  :profiles {:dev {:dependencies [[clj-kondo "2021.02.13"]
                                  [cljfmt "0.7.0"]]}}
  :aliases {"clj-kondo" ["run" "-m" "clj-kondo.main" "--config" ".github/linters/.clj-kondo/config.edn" "--lint" "src" "test"]
            "cljfmt"    ["run" "-m" "cljfmt.main" "--indents" ".cljfmt-indents.edn"]
            "lint"      ["do" ["clj-kondo"] ["cljfmt" "check"] ["nsorg"]]
            "lint-fix"  ["do" ["cljfmt" "fix"] ["nsorg" "--replace"]]}
  :repl-options {:init-ns structurizr-clj.core})
