(ns liqtest.web
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))
(import org.openqa.selenium.firefox.FirefoxDriver)
(import org.openqa.selenium.chrome.ChromeDriver)
(import org.openqa.selenium.chrome.ChromeOptions)
(import org.openqa.selenium.firefox.FirefoxBinary)
(import org.openqa.selenium.firefox.FirefoxProfile)
(import org.openqa.selenium.By)
(import org.openqa.selenium.WebDriver)
(import org.openqa.selenium.WebElement)
(import org.openqa.selenium.remote.DesiredCapabilities)
(import org.openqa.selenium.remote.CapabilityType)
(import org.openqa.selenium.logging.LoggingPreferences)
(import org.openqa.selenium.logging.LogType)
(import org.openqa.selenium.remote.RemoteWebDriver)
(import org.openqa.selenium.interactions.Actions)
(import org.openqa.selenium.JavascriptExecutor)
(import org.openqa.selenium.support.ui.WebDriverWait)
(import org.openqa.selenium.support.ui.ExpectedConditions)
(import java.util.concurrent.TimeUnit)
(import java.util.logging.Level)
(import java.util.logging.Logger)
(import java.net.URL)

(def myout (java.io.StringWriter.))

(defn new-firefox
  "Starts a Firefox instance."
  []
  (let [capabilities (DesiredCapabilities/firefox)
        profile (doto (FirefoxProfile.)
                  (.setPreference "media.navigator.permission.disabled" true))]
      (FirefoxDriver. profile)))

(defn new-chrome
  "Start a new instance of Chrome."
  []
  (System/setProperty "webdriver.chrome.silentOutput", "true")
  (.setLevel (Logger/getLogger "org.openqa.selenium.remote") Level/OFF)
  (let [options (doto (ChromeOptions.)
                  (.addArguments ["--use-fake-ui-for-media-stream"
                                 ; "--use-file-for-fake-audio-capture=/tmp/output.wav"
                                 ]))
        capabilities (doto (DesiredCapabilities/chrome)
                       (.setCapability ChromeOptions/CAPABILITY options))]
    (ChromeDriver. capabilities)))

(defn new-chrome-headless
  "Start a new instance of Chrome Headless."
  []
  (System/setProperty "webdriver.chrome.silentOutput", "true")
  (.setLevel (Logger/getLogger "org.openqa.selenium.remote") Level/OFF)
  (let [options (doto (ChromeOptions.)
                  (.addArguments ["--use-fake-ui-for-media-stream"
                                  "--headless"
                                 ; "--use-file-for-fake-audio-capture=/tmp/output.wav"
                                 ]))
        capabilities (doto (DesiredCapabilities/chrome)
                       (.setCapability ChromeOptions/CAPABILITY options))]
    (ChromeDriver. capabilities)))


(defn new-browser
  "Start a new browser."
  []
  (cond (= (System/getProperty "browser") "firefox") (new-firefox)
        (= (System/getProperty "browser") "headless") (new-chrome-headless)
        true (new-chrome)))

(defn execute-script
  "Execute javascript in browser."
  [^RemoteWebDriver web js & js-args]
    (.executeScript web ^String js (into-array Object js-args)))

(defn disable-changed-warning
  [web]
  (execute-script web "window.onbeforeunload = function(e){};"))


(defn sleep ; time in milliseconds
  ([web s]
    (Thread/sleep s))
  ([s] (sleep nil s)))

(defn wait-for-disappear
  [web css]
  (.until (WebDriverWait. web 30)
    (ExpectedConditions/invisibilityOfElementLocated (By/cssSelector css))))

(defn wait-for-visible
  [web css]
  (.until (WebDriverWait. web 30)
    (ExpectedConditions/visibilityOfElementLocated (By/cssSelector css))))

(defn quit
  "Closes the Firefox instance."
  [web]
  (.quit web))

(defn goto
  "Opens url in started Firefox.
  Prepends http:// if not starting with http."
  [web url]
  (if (.startsWith url "http")
    (.get web url)
    ;;else
    (.get web (str "http://" url))))

(defn get-url
  [web]
  (.getCurrentUrl web))

(defn get-element
  "Returns WebElement if present, otherwise nil.
  Using css selector."
  [web css]
  (.findElement web (By/cssSelector css)))

(defn get-elements
  "Returns list of WebElements if present, otherwise nil.
  Using css selector."
  [web css]
  (.findElements web (By/cssSelector css)))

(defn get-safe
  [web css]
  ;; Wait for document ready
  (loop [attempts 30]
    (when (and (not= (execute-script web "return document.readyState") "complete") (> attempts 0))
      (sleep web 200)
      (recur (dec attempts))))
  (let [element (wait-for-visible web css)]
    ; NOTE: In some cases of exception where element is not clickable it might be the topmenu that
    ;       lays over the element.
    ;(execute-script web "arguments[0].scrollIntoView();" element)
    ;(execute-script web "scrollBy(0,50);")
    ;(sleep web 500)
    ;https://stackoverflow.com/questions/11908249/debugging-element-is-not-clickable-at-point-error
    (when (System/getProperty "slow")
      (execute-script web "arguments[0].style.border='3px solid red'" element)
      (sleep web 2000))
    element))

(defn send-keys
  "Takes a WebElement and a string to type into element.
  If the element is a string it will be considered a
  css selector."
  [web element keys]
  (.sendKeys (if (string? element) (get-safe web element) element) (into-array [keys])))

(defn click
  "Clicks a WebElement.
  If the element is a string it will be
  considered css selector."
  [web element]
  (let [resolvedelement (if (string? element) (get-safe web element) element)]
    (.click resolvedelement)))

(defn clear
  "Clears a WebElement.
  If the element is a string it will be
  considered a css selector."
  [web element]
  (.clear (if (string? element) (get-safe web element) element)))

(defn hover
  "Hover the WebElement."
  [web element]
  (let [e (if (string? element) (get-safe web element) element)]
    (-> (Actions. web)
      (.moveToElement e)
      (.build)
      (.perform))))

(defn get-text
  "Returns the text content of a WebElement given
  the WebElement itself or a css selector to the WebElement."
  [web element]
  (.getText (if (string? element) (get-safe web element) element)))

(defn assert-css
  "Throws an Exception if the given css selector
  does not have a target on the current page."
  [web css]
  (is (not (nil? (get-safe web css)))))

(defn assert-text
  [web css text]
  (is (= (get-text web css) text)))

(defn post-screenshot
  "Takes a screenshot of the current page
  and saves it to the given path."
  [web path]
  (io/copy (.getScreenshotAs web org.openqa.selenium.OutputType/FILE)
           (io/file path)))