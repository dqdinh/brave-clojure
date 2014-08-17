(defproject brave-clojure "0.1.0-SNAPSHOT"
  :url "https://github.com/dqdinh/brave-clojure"
  :license {:name "MIT"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace  "0.2.5"]
                                  [org.clojure/tools.trace  "0.7.8"]]
                   :source-paths ["dev"]}})
