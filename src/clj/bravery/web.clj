(ns bravery.web
    (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
              [compojure.handler :refer [site]]
              [compojure.route :as route]
              [clojure.java.io :as io]
              [ring.adapter.jetty :as jetty]
              [ring.util.response :as resp]
              [environ.core :refer [env]])
  (:gen-class))

(defroutes app
    (GET "/" [] (resp/file-response "index.html" {:root "war"}))
    (GET "/js/main.js" [] (resp/file-response "js/main.js" {:root "war"}))
    (ANY "*" []
         "not found"))

(defn -main [& [port]]
    (let [port (Integer. (or port (env :port) 5001))]
      (jetty/run-jetty (site #'app) {:port port :join? false})))
