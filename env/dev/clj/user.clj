(ns user
  (:require 
            [mount.core :as mount]
            website.core))

(defn start []
  (mount/start-without #'website.core/repl-server))

(defn stop []
  (mount/stop-except #'website.core/repl-server))

(defn restart []
  (stop)
  (start))


