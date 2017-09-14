(ns liqtest.pages.wikifront
  (:require [liqtest.web :refer :all]))

;; URL
(def URL "https://www.wikipedia.org/")

;; LOCATORS
(def SEARCHFIELD "#searchInput")
(def SEARCHBUTTON "button[class^='pure-button']")

;; ACTIONS
(defn navigate-to-frontpage
  [web]
  (goto web URL))

(defn enter-searchquery
  [web text]
  (send-keys web SEARCHFIELD text))

(defn click-searchbutton
  [web]
  (click web SEARCHBUTTON))