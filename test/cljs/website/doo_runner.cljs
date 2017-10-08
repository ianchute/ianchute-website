(ns website.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [website.core-test]))

(doo-tests 'website.core-test)

