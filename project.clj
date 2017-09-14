(defproject liqtest "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.seleniumhq.selenium/selenium-firefox-driver "3.4.0"]
                 [org.seleniumhq.selenium/selenium-chrome-driver "3.4.0"]
                 [org.seleniumhq.selenium/selenium-support "3.4.0"]]
  :test-paths ["src/liqtest/tests"]
  :profiles {:uberjar {:aot :all}
             :liq {:dependencies [[mogenslund/liquid "0.8.1"]]
                   :main dk.salza.liq.core}
             :headless {:jvm-opts ["-Dbrowser=headless"]}
             :firefox {:jvm-opts ["-Dbrowser=firefox"]}
             :chrome {:jvm-opts ["-Dbrowser=chrome"]}
             :slow {:jvm-opts ["-Dslow=true"]}}
  :aliases {"liq" ["with-profile" "liq" "run" "--load=.liq"]
            "headless" ["with-profile" "+headless" "test"]
            "firefox" ["with-profile" "+firefox" "test"]
            "chrome" ["with-profile" "+chrome" "test"]
            "slow" ["with-profile" "+slow" "test"]})