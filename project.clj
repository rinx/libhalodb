(defproject libhalodb "0.1.0-SNAPSHOT"
  :description "libhalodb provides HaloDB APIs"
  :url "https://github.com/rinx/libhalodb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [clj-halodb "0.0.3"]
                 [borkdude/clj-reflector-graal-java11-fix "0.0.1-graalvm-20.1.0"]]
  :profiles {:uberjar {:aot :all
                       :main libhalodb.impl.libhalodb}})
