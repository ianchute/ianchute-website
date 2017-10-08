(ns user
  (:require [luminus-migrations.core :as migrations]
            [website.config :refer [env]]
            [mount.core :as mount]
            [website.figwheel :refer [start-fw stop-fw cljs]]
            website.core))

(defn start []
  (mount/start-without #'website.core/repl-server))

(defn stop []
  (mount/stop-except #'website.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))


