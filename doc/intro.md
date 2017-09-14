# ================================================================== #
# This is a demo page of using Selenium Webdriver from inside liquid #
# ================================================================== #

# Navigation
# ==========

With blue cursor, navigate with "i" "j" "k" "l". Try it.
To insert text use tab to switch to green cursor. Use Tab again to switch
back to blue cursor for navigation.
For more options look at

    https://github.com/mogenslund/liquid/wiki/Cheat-Sheet


# Evaluation
# ==========

With blue cursor click "e" to evaluate current s-expression:
Place cursor inside these parentheses (range 10) and click "e".
Observe the expression is evaluated.

Notice what is evaluated depends on where in the expression the
cursor is placed. Try pressing "e" and "1" (toggle highlight expression)
on different locations inside the expression below:

    (println "Something" (range 3) (range 4))

Try changing something and evaluate again.

# Webdriver
# =========

Evalute the first form below by clicking "e" while on "web". This will spawn
a browser window. Before evaluating the other forms I recommend arranging the
the windows so this editor and the browserwindow can be seen at the same time,
side by side.

    (def web (new-browser))

Now a browser should be running.
Evaluate the next form to navigate to Wikipedia:

    (goto web "https://wikipedia.org")

The next form will type "Turtle" into the search field

    (send-keys web "#searchInput" "Turtle")

To click the search button evalute:

    (click web "button[class^='pure-button']")

Then exit the browser using

    (quit web)


# Using page objects
# ==================

I have create some functions using the Page Object pattern:

    src/liqtest/pages/wikifront.clj
    src/liqtest/pages/wikiarticle.clj

Use CTRL+space and use typeahead to navigate to the files.
With this pattern the code can be simplified.
Execute the code below by placing the cursor on "doto" and
type "e" to evalute. Then wait a bit.


    (doto (new-browser)
      wikifront/navigate-to-frontpage
      (wikifront/enter-searchquery "Turtle")
      wikifront/click-searchbutton
      quit)


# Real tests
# ==========

A real test can be found by using CTRL+space and typeahead to
navigate to src/liqtest/tests/searchtest.clj

The test can be executed from the commandline using

    lein test :only liqtest.tests.searchtest/searchtest

To run the tests slowly (if you need to see what is happening), use

    lein slow :only liqtest.tests.searchtest/searchtest

To run headless:

    lein headless :only liqtest.tests.searchtest/searchtest

To run all tests, skip the :only part, like this:

    lein test

To run on Firefox, use

    lein firefox

The tests can also be evaluate directly in the editor. First use "E" (shift+"e")
to evalute the file (That is, load the dependencies)
Then move the cursor to the first form in a test, the word "testing" and click "e"
to evaluate the form, which is also the test.