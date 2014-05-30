(ns cljnio.common
  (:import [java.nio.channels CompletionHandler]))

(set! *warn-on-reflection* true)

(defn ^CompletionHandler fn-handler
  "Builds a CompletionHandler from a function"
  [f]
  (reify CompletionHandler
    (completed [_ v _] 
      (f [:completed v]))
    (failed [_ e _]
      (f [:failed e]))))

(defmacro defalias
  "Defines an alias for a var: a new var with the same root binding (if
  any) and similar metadata. The metadata of the alias is its initial
  metadata (as provided by def) merged into the metadata of the original."
  ([name orig]
   `(do
      (alter-meta!
        (if (.hasRoot (var ~orig))
          (def ~name (.getRoot (var ~orig)))
          (def ~name))
        ;; When copying metadata, disregard {:macro false}.
        ;; Workaround for http://www.assembla.com/spaces/clojure/tickets/273
        #(conj (dissoc % :macro)
               (apply dissoc (meta (var ~orig)) (remove #{:macro} (keys %)))))
      (var ~name)))
  ([name orig doc]
   (list `defalias (with-meta name (assoc (meta name) :doc doc)) orig)))
