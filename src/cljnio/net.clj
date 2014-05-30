(ns cljnio.net
  (:import
    [java.net SocketAddress InetSocketAddress]))

(defn ^SocketAddress address
  "Returns a SocketAddress from a map of form
  {:host \"127.0.0.1\" :port 4004}
  :host key defaults to \"127.0.0.1\" if not a string or missing"
  ([m]
  (let [{:keys [host port]} m]
   (when (integer? port)
     (address (if (string? host)
                host
                "127.0.0.1")
              port))))
  ([^String host ^long port] (InetSocketAddress. host port)))
