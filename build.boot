(set-env!
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [adzerk/boot-bookmarklet   "0.2.0"]
                  [adzerk/boot-cljs          "1.7.228-1"]])

(require '[adzerk.boot-bookmarklet :refer (bookmarklet external-bookmarklet)])

(deftask build
  "Updates the hosted bagel_factory.js file and generates bookmarklet.html, a
   page with a link to the hosted bookmarklet."
  []
  (comp
    (watch)
    (speak)
    ; TODO: upload updated .js file to dropbox
    (external-bookmarklet
      :urls #{"https://dl.dropboxusercontent.com/u/100360569/bagel_factory.js"})
    (target)))

