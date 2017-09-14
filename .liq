(ns user
  (:require [dk.salza.liq.editor :as editor]
            [dk.salza.liq.clojureutil :as clojureutil]
            [dk.salza.liq.tools.cshell :refer :all]
            [dk.salza.liq.apps.textapp :as textapp]
            [dk.salza.liq.logging :as logging]
            [dk.salza.liq.tools.util :as util]
            [clojure.test :refer :all]
            [liqtest.web :refer :all]
            [liqtest.pages.wikiarticle :as article]
            [liqtest.pages.wikifront :as wikifront]))

(editor/find-file (.getCanonicalPath (clojure.java.io/file "./doc/intro.md")))