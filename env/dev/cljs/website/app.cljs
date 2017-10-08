(ns ^:figwheel-no-load website.app
  (:require [website.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
