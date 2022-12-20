(ns avro-serializer.core
  (:require [jackdaw.client :as jc]
            [clojure.pprint :as pp]
            [jackdaw.serdes.json :as json-serde]
            [jackdaw.serdes :as str-serde]
            [cheshire.core :as json]))

(defn produce-messages
  [broker-config topic-name messages]
  ;; konfigurace producenta zprav ve formatu JSON
  (let [producer-config (merge broker-config {"acks" "all"
                                              "client.id" "foo"})
        ; specifikace zpusobu serializace klicu i obsahu zpravy
        producer-serde-config {:key-serde   (str-serde/string-serde)
                               :value-serde (json-serde/serde)}]

    ;; poslani 100 zprav se serializaci klice i hodnoty
    (with-open [producer (jc/producer producer-config producer-serde-config)]
      (doseq [i (range 0 messages)]
        (let [topic {:topic-name topic-name}
              ; posilany klic
              message-key (json/generate-string {:n i
                                                 :foo "foo"})
              ; posilany obsah zpravy
              message-value {:bar "bar"
                             :value i
                             :recip (/ 1 (inc i))
                             :factorial (reduce * (range 1M (inc i)))
                             :values (range i)}
              record-metadata (jc/produce! producer topic message-key message-value)]
          (pp/pprint message-value)
          (pp/pprint @record-metadata))))))

(defn -main
  [& _args]
  (let [broker-config {"bootstrap.servers" "127.0.0.1:9092"}
        topic-name "test-avro"]
    ;; poslani 100 zprav se serializaci klice i hodnoty
    (produce-messages broker-config topic-name 10)))
