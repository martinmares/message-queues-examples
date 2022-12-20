(ns avro-serializer.core
  (:require [jackdaw.client :as jc]
            [clojure.pprint :as pp]
            [cheshire.core :as json]
            [clj-http.client :as http-client]
            [jackdaw.serdes.avro.confluent :as avro-serde])
  (:import org.apache.kafka.common.serialization.Serdes))

(defonce ^:dynamic
  *registry-url*
  "http://192.168.125.110:8081/api/ccompat")

(defonce ^:dynamic
  *registry-schema*
  "test-avro-cz.datalite.zis.cdc.avro.Sample")

(defn get-schema-str
  ([subject] 
   (get-schema-str subject "latest"))
  ([subject version]
   (let [resp (http-client/get   
               (str *registry-url* "/subjects/" subject "/versions/" version)) 
         body (:body resp) 
         json (json/parse-string body true)] 
     (str (:schema json)))))

(defn produce-messages
  [broker-config topic-name messages]
  ;; konfigurace producenta zprav ve formatu JSON
  (let [producer-config (merge broker-config {"acks" "all"
                                              "client.id" "foo"})
        ; specifikace zpusobu serializace klicu i obsahu zpravy
        producer-serde-config {:key-serde   (Serdes/ByteArray)
                               :value-serde (avro-serde/serde *registry-url*
                                                              (get-schema-str *registry-schema*)
                                                              false ;; key?
                                                              {:serializer-properties
                                                               {"value.subject.name.strategy"
                                                                "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy"}
                                                               :deserializer-properties 
                                                               {"specific.avro.reader"
                                                                true}})}]

    ;; poslani 100 zprav se serializaci klice i hodnoty
    (with-open [producer (jc/producer producer-config producer-serde-config)]
      (doseq [i (range 0 messages)]
        (let [topic {:topic-name topic-name}
              ; posilany klic
              message-key (.getBytes (str "{\"id\":" i "}"))
              ; posilany obsah zpravy
              message-value {:id i
                             :name "Hello World!"}
              record-metadata (jc/produce! producer topic message-key message-value)]
          (pp/pprint message-value)
          (pp/pprint @record-metadata))))))

(defn -main
  [& _args]
  (let [broker-config {"bootstrap.servers" "127.0.0.1:9092"}
        topic-name "test-avro"]
    ;; poslani 10 zprav se serializaci klice i hodnoty
    (produce-messages broker-config topic-name 10)))
