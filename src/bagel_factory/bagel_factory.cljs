(ns bagel-factory.bagel-factory
  (:require [clojure.string :as str]
            [goog.string    :as gstring]))

;;;;;;

(comment
  "There is a bug (http://dev.clojure.org/jira/browse/CLJS-485) in the cljs
   implementation of clojure.string/replace -- it ignores regex flags and
   hardcodes in 'g' instead.

   Copying replace-all, replace-with and replace and adding the case
   insensitivity flag in as a work-around.")

(defn- str-replace-all
  [s re replacement]
  (.replace s (js/RegExp. (.-source re) "gi") replacement))

(defn- str-replace-with
  [f]
  (fn [& args]
    (let [matches (drop-last 2 args)]
      (if (= (count matches) 1)
        (f (first matches))
        (f (vec matches))))))

(defn str-replace
  "Replaces all instance of match with replacement in s.
   match/replacement can be:
   string / string
   pattern / (string or function of match)."
  [s match replacement]
  (cond
    (string? match)
    (.replace s (js/RegExp. (gstring/regExpEscape match) "gi") replacement)

    (instance? js/RegExp match)
    (if (string? replacement)
      (str-replace-all s match replacement)
      (str-replace-all s match (str-replace-with replacement)))

    :else (throw (str "Invalid match arg: " match))))

;;;;;;

(defn words-to-replace-regex
  [replacements]
  (js/RegExp. (apply str (interpose "|" (keys replacements))) "i"))

(defn replace-fn
  [replacements]
  (fn [match]
    (let [replacement (get replacements (str/lower-case match))]
      (cond
        (re-matches #"[a-z]+" match)      replacement
        (re-matches #"[A-Z]+" match)      (str/upper-case replacement)
        (re-matches #"[A-Z][a-z]+" match) (str/capitalize replacement)))))

(defn replace-all
  [txt replacements]
  (str-replace txt
               (words-to-replace-regex replacements)
               (replace-fn replacements)))

(defn leaf-node? [node]
  (= 3 (.-nodeType node)))

(defn replace-text-content!
  "Replace the text content of a node."
  [replacements node]
  (let [field (cond
                (aget node "textContent") "textContent"
                (aget node "nodeValue")   "textContent")
        text  (aget node field)]
    (aset node field (replace-all text replacements))))

(defn replace-text!
  "Replaces all matches of a regular expression on the page with a replacement
   string."
  ([replacements]
    (replace-text! replacements js/document.body))
  ([replacements node]
    (if (leaf-node? node)
      (replace-text-content! replacements node)
      (doseq [child-node (array-seq (aget node "childNodes"))]
        (replace-text! replacements child-node)))))

(replace-text! {"bagel" "croissant"
                "factory" "refectory"})

