;
;  (C) Copyright 2021  Pavel Tisnovsky
;
;  All rights reserved. This program and the accompanying materials
;  are made available under the terms of the Eclipse Public License v1.0
;  which accompanies this distribution, and is available at
;  http://www.eclipse.org/legal/epl-v10.html
;
;  Contributors:
;      Pavel Tisnovsky
;

(defproject json-serializer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [fundingcircle/jackdaw "0.9.8"]
                 [org.apache.kafka/kafka-streams-test-utils "3.3.1"]]
  :plugins [[lein-codox "0.10.7"]
            [test2junit "1.1.0"]
            [lein-cloverage "1.0.7-SNAPSHOT"]
            [lein-kibit "0.1.8"]
            [lein-clean-m2 "0.1.2"]
            [lein-project-edn "0.3.0"]
            [lein-marginalia "0.9.1"]]
  :project-edn {:output-file "doc/details.clj"}
  :main ^:skip-aot json-serializer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
