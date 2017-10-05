
[{:name "Client"
  :indexed-by "String"
  :fields [{:name "name"}
           {:name "exerciseStart" :type "Date"}
           {:name "synchronize" :type "boolean"}]
  
  :childs [{:name "Connection"
            :indexed-by "String"
            :fields [{:name "port"}
                     {:name "database"}
                     {:name "host"}
                     {:name "username"}
                     {:name "password"}]}
           
           {:name "TableImplementation"
            :fields [{:name "implementedTable" :type "Table"}
                     {:name "columnImplementations" :type "String" :many true}
                     {:name "connection" :type "Connection"}
                     {:name "query"}]}]}

 
 {:name "User"
  :indexed-by "String"
  :fields [{:name "email"}
           {:name "plainPassword"}
           {:name "name"}
           {:name "client" :type "Client"}]}

 {:name "Table"
  :indexed-by "String"
  :fields [{:name "reference" :type "boolean"}]
  :childs [{:name "Column"
            :indexed-by "String"
            :fields [{:name "type" :type "DataType"}
                     {:name "property" :type "ColumnProperty"}
                     {:name "columnReference"}]}]}

 {:name "ColumnProperty"
  :kind "enum"
  :values ["PRIMARY_KEY"
           "SHARD_KEY"
           "FOREIGN_KEY"]}
 
 {:name "DataType"
  :kind "enum"
  :values ["PK"
           "FK"
           "TEXT"
           "TRANSLATABLE_TEXT"
           "INTEGER"
           "DECIMAL"
           "MONEY"
           "PERCENTAGE"
           "DOW"
           "HOD"
           "DATE"
           "DATETIME"
           "HOUR"
           "WEEK"
           "MONTH"
           "YEAR"
           "BOOLEAN"
           "DYNAMIC"]}]

