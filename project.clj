(defproject bravery "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.2.0"]
                 [cljspp "0.0.2-SNAPSHOT"]
                 [ring/ring-jetty-adapter "1.3.1"]
                 [environ "1.0.0"]

                 [org.clojure/clojurescript "0.0-2371"]
                 [reagent "0.4.3"]]

  :min-lein-version "2.1.2"
  :main oxylab.web
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}

  :source-paths ["src/clj"
                 "target/generated/clj"]

  :plugins [
      [lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :uberjar-name "bravery.jar"

  :cljsbuild {
    :builds [{:source-paths ["src/cljs"]
              :compiler {:output-to "war/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true
                         :preamble ["reagent/react.js"]}}]})
