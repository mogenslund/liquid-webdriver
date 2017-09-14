(ns liqtest.pages.wikiarticle
  (:require [liqtest.web :refer :all]))

;; LOCATORS
(def FIRSTHEADING "#firstHeading")

;; ACTIONS
(defn get-first-heading
  [web]
  (get-text web FIRSTHEADING))