(ns mabank.models.transaction.fetch-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.transaction.fetch :refer :all]
            [mabank.models.transaction.create :as trx-create]
            [mabank.models.recipient.create :as recipient-create]
            [mabank.models.recipient.fetch :as recipient-fetch]
            [cheshire.core :refer :all]))

;;FIXME: GET RID OF THIS MESS FOR ZOD SAKE!! aka needs refactoring :P
(def new-id (d/tempid :db.part/user))

(def recipient-payload  {:json-params {:name "Saitama"
                                       :document_number "1234567890"}})

(def transaction-payload  {:json-params {:amount 100
                                         :installments 1
                                         :recipient-id 17592186045465}})

(defn get-id
  [obj]
  (get (first (parse-string obj true)) :id))

(defn create-empty-db
  []
  (let [uri "datomic:mem://mabank-db"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")]
      (d/transact conn schema)
      conn)))

(expect []
        (with-redefs [conn (create-empty-db)]
          (do
            (recipient-create/run recipient-payload)
            ; (let [recipient-id (get-id (recipient-fetch/run))]
            (trx-create/run transaction-payload)
            ; (trx-create/run (assoc-in transaction-payload [:json-params :recipient-id] recipient-id)))
            (run))))
