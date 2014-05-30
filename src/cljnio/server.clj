(ns cljnio.server
  (:require
    [cljnio.net :refer [address]]
    [cljnio.common :refer [fn-handler]])
  (:import
    java.net.SocketAddress
    [java.nio.channels AsynchronousServerSocketChannel CompletionHandler]))

;; Low Level tools
(defn- ^AsynchronousServerSocketChannel open
  []
  (AsynchronousServerSocketChannel/open))

(defn- bind!
  [^AsynchronousServerSocketChannel assc ^SocketAddress addr]
    (.bind assc addr)
    assc)

(defn- accept!
  [^AsynchronousServerSocketChannel assc ^CompletionHandler ch]
  (.accept assc nil ch)
  assc)

(defn- close!
  [^AsynchronousServerSocketChannel assc]
  (.close assc)
  assc)

;; Medium level API
(defn listen!
  [addr]
  (let [assc (open)]
    (bind! assc (address addr))
    assc))

(defn set-handler!
  [assc f]
  (accept! assc (fn-handler (fn [[status r]]
                              (if (= :completed status)
                                (do (set-handler! assc f)
                                    (f [:connection r]))
                                (f [:error r]))))))

;; High Level API
(defn with-server*
  [addr make-handler]
  (let [assc (listen! addr)
        close! #(close! assc)]
    (set-handler! assc (make-handler close!))
    close!))

(defmacro with-server
  [addr client-sym & body]
  `(let [f# (fn [~'close!]
              (fn [~client-sym]
                ~@body))]
     (with-server* ~addr f#)))
