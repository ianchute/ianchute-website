(ns website.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[website started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[website has shut down successfully]=-"))
   :middleware identity})
