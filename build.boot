(set-env!
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [adzerk/boot-bookmarklet   "0.1.1"]
                  [adzerk/boot-cljs          "1.7.228-1"]])

(require '[adzerk.boot-bookmarklet :refer (bookmarklet)])

(deftask build
  []
  (comp
    (watch)
    (speak)
    (bookmarklet)
    (target)))

