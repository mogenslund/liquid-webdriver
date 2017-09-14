(ns liqtest.tests.searchtest
  (:require [clojure.test :refer :all]
            [liqtest.web :refer :all]
            [liqtest.pages.wikiarticle :as article]
            [liqtest.pages.wikifront :as wikifront]))




(deftest searchtest
  "This test searches wikipedia for the word
  Turtle and checks the result is an article
  with headline Turtle.
  To execute run:
    lein test :only liqtest.tests.searchtest
  from commandline."
  (testing "Basic search on wikipedia"
    (def web (new-browser))
    (wikifront/navigate-to-frontpage web)
    (wikifront/enter-searchquery web "Turtle")
    (wikifront/click-searchbutton web)
    (is (= (article/get-first-heading web) "Turtle"))
    (quit web)))




(deftest searchtest2
  "This test searches wikipedia for the word
  Turtle and checks the result is an article
  with headline Turtle.
  To execute run:
    lein test :only liqtest.tests.searchtest2
  from commandline."
  (testing "Basic search on wikipedia"
    (let [web (new-browser)]
      (doto web
        wikifront/navigate-to-frontpage
        (wikifront/enter-searchquery "Turtle")
        wikifront/click-searchbutton)
      (is (= (article/get-first-heading web) "Turtle"))
      (quit web))))

