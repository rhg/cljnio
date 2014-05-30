(ns cljnio.async
  "Optional core.async support

  (let [ch (clojure.core.async/chan)]
    (cljnio.server/with-server {:port 4004} client
      (cljnio.common/fn-handler (cljnio.async/async-fn ch)))

  ch will now get the result pushed on to it")

;; core.async is optional so we load it at runtime
(def ^:private put!
  "If we have core.async this is put! else nil"
  (try
    (require 'clojure.core.async)
    @(resolve 'clojure.core.async
              'put!)
    (catch Throwable _)))

(defn async-fn
  "returns a function putting events on a channel
  core.async must be on the classpath"
  [ch]
  {:pre [put! ch]}
  (fn [v]
    (put! ch v)))
