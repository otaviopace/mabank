(ns mabank.core
  (:require [datomic.api :as d]))

(def uri "datomic:free://localhost:4334/mabank-db")
(def conn (d/connect uri))

(defn create-recipient
  [name]
  @(d/transact conn [{:db/id (d/tempid :db.part/recipient)
                      :recipients/name name}]))

(defn find-recipients
  []
  (d/q '[:find ?recipient-name
         :where [_ :recipients/name ?recipients/name]]))

(defn -main
  [& args]
  (-> (create-recipient "fulano")))