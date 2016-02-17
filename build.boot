(set-env!
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [adzerk/boot-cljs          "1.7.228-1"]])

(require '[adzerk.boot-cljs :refer (cljs)]
         '[boot.core        :as    core]
         '[clojure.java.io  :as    io]
         '[clojure.string   :as    str])

(ns-unmap 'boot.user 'path->ns)

(defn path->js
  "Given a path to a cljs namespace source file, returns the corresponding
   Google Closure namespace name for goog.provide() or goog.require().

   (stolen from boot-cljs)"
  [path]
  (-> path
      (str/replace #"\.clj([s|c])?$" "")
      (str/replace #"[/\\]" ".")))

(defn- path->ns
  "Given a path to a cljs namespace source file, returns the corresponding
   cljs namespace name.

   (stolen from boot-cljs)"
  [path]
  (-> (path->js path) (str/replace #"_" "-")))

(defn- all-cljs-src-files
  [fileset]
  (->> fileset core/input-files (core/by-ext ["cljs"])))

(defn- cljs-edn-for
  [cljs-file]
  {:require          (mapv (comp symbol path->ns core/tmp-path) [cljs-file])
   :compiler-options {:optimizations :advanced}})

(deftask ^:private generate-cljs-edn
  [f file FILE #{str} "The cljs files for which to generate .cljs.edn files."]
  (let [tmp-main (core/tmp-dir!)]
    (core/with-pre-wrap fileset
      (core/empty-dir! tmp-main)
      (let [file-filter (if file
                          #(core/by-name file %)
                          #(core/by-ext ["cljs"] %))
            cljs-files  (->> fileset
                             core/input-files
                             file-filter
                             (sort-by :path))]
        (doseq [cljs cljs-files
                :let [out-main (str (.getName (core/tmp-file cljs)) ".edn")
                      out-file (io/file tmp-main out-main)]]
          (info "Writing %s...\n" (.getName out-file))
          (doto out-file
            (io/make-parents)
            (spit (cljs-edn-for cljs))))
        (-> fileset (core/add-source tmp-main) core/commit!)))))

(defn bookmarklet-link
  [js-file]
  "TODO\n")

(deftask ^:private generate-html
  []
  (let [tmp-main (core/tmp-dir!)]
    (core/with-pre-wrap fileset
      (core/empty-dir! tmp-main)
      (let [js-files  (->> fileset
                           core/output-files
                           (core/by-ext ["js"])
                           (sort-by :path))
            html-file (io/file tmp-main "bookmarklets.html")]
        (info "Writing %s...\n" (.getName html-file))
        (doto html-file
          (io/make-parents)
          (spit "HEADER\n"))
        #_(doseq [js js-files]
          (spit html-file (bookmarklet-link js) :append true))
        (prn (slurp html-file))
        (-> fileset (core/add-source tmp-main) core/commit!)))))

(deftask bookmarklet
  [f file FILE #{str} "The cljs files to turn into bookmarklets."]
  (comp
    (generate-cljs-edn :file file)
    (cljs)
    (generate-html)))

(deftask build
  []
  (comp
    (watch)
    (speak)
    (bookmarklet)
    (target)))

(comment
  "* for each namespace provided as a task option (and/or perhaps just going
     through all namespaces in source paths), use boot-cljs to output a
     separate js file (use advanced compilation)

     * will need to generate .cljs.edn files

   * generate an html file that includes, for each js file, a link like:

       <a href='javascript: ...'>namespace.name</a>

     * sanitize the JS:
       * wrap the whole thing in an anonymous function: (function(){     })();
       * strip out // comments
       * replace newlines with single spaces
       * replace double quotes with &quot;

   * commit the html file to the fileset")
