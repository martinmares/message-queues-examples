(defproject avro-serializer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [fundingcircle/jackdaw "0.9.8"]
                 [cheshire "5.11.0"]
                 [clj-http "2.3.0"]
                 [org.apache.kafka/kafka-streams-test-utils "3.3.1"]]
  :main ^:skip-aot avro-serializer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
