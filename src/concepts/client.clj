[{:name "Client"
  :id "String"
  :fields [{:name "name" :type "String"}
           {:name "exerciseStart" :type "Date"}
           {:name "synchronize" :type "boolean"}
          ]
  
  :childs [{:name "Connection"
            :indexed-by "String"
            :fields [{:name "port" :type "int"}
                     {:name "database" :type "String"}
                     {:name "host" :type "String"}
                     {:name "username" :type "String"}
                     {:name "password" :type "String"}
                    ]
            }
           
           {:name "TableImplementation"
            :fields [{:name "implementedTable" :type "Table"}
                     {:name "columnImplementations" :type "String" :many true}
                     {:name "connection" :type "Connection"}
                     {:name "query" :type "String"}
                    ]
          }

          {:name "ReportDefinition"
          :fields [{:name "users" :type "User" :many true }
                   {:name "dashboard" :type "boolean"}
                   {:name "name" :type "String"}
                   {:name "hasSidebar" :type "boolean"}
                   {:name "defaultSelections" :type "SelectionList"}
                   {:name "widgets" :type "Widget" :many true}
                  ]
           }
           ]
  }

 {:name "SelectionList"
 :fields [{:name "datasourcesWithUnits" :type "DatasourceWithUnitsSelection" :many true}
          {:name "periodFilter" :type "PeriodFilterSelection"}
          {:name "filters", :type "FilterSelection" :many true}
          {:name "groupings", :type "GroupingSelection" :many true}
          {:name "ranges", :type "RangeSelection" :many true}
          {:name "comparison", :type "ComparisonSelection"}
          {:name "blockedFacets", :type "Facet" :many true}
        ]
 }

 {:name "PeriodFilterSelection"
  :fields[{:name "selectedId" :type "String"}
          {:name "timeMachineLocalNow" :type "String"}
         ]
 }

 {:name "FilterSelection"
  :fields[{:name "facet" :type "Facet"}
          {:name "selectedId" :type "String"}
         ]
 }

 {:name "GroupingSelection"
  :fields[{:name "facet" :type "Facet"}
          {:name "selectedId" :type "String"}
          {:name "index" :type "Long"}
         ]
 }

 {:name "RangeSelection"
  :fields[{:name "datasource" :type "Datasource"}
          {:name "units" :type "RangeUnit" :many true}
         ]
 }

 {:name "RangeUnit"
  :fields[{:name "rangeSelection" :type "RangeSelection"}
          {:name "unit" :type "Unit"}
          {:name "min" :type "double"}
          {:name "max" :type "double"}
          {:name "selectedMin" :type "double"}
          {:name "selectedMax" :type "double"}
         ]
 }

 {:name "Widget"
  :fields[{:name "gridPosition" :type "GridPosition"}
          {:name "selectionList" :type "SelectionList"}
          {:name "customSelectionList" :type "SelectionList"}
          {:name "firstUnit" :type "Unit"}
          {:name "secondUnit" :type "Unit"}
          {:name "thirdUnit" :type "Unit"}
          {:name "fourthUnit" :type "Unit"}
          {:name "type" :type "Type"}
         ]
 }

{:name "Report"
  :fields[{:name "name" :type "String"}
          {:name "index" :type "Integer"}
          {:name "dashboard" :type "boolean"}
          {:name "hasSidebar" :type "boolean"}
          {:name "defaultSelections" :type "SelectionList"}
         ]
  :childs[{:name "widgets" :type "Widget" :many true}]
 }

{:name "GridPosition"
  :fields[{:name "index" :type "Integer"}
          {:name "width" :type "Integer"}
         ]
 }

{:name "Type"
  :kind "enum"
  :values [
           "KPI"
           "AREA"
           "BARS"
           "COLUMNS"
           ]
  }

 {:name "Unit"
  :kind "enum"
  :values [
          "VALUE"
          "PREVIOUS_VALUE"
          "PREVIOUS_VALUE_DIFF"
          "PREVIOUS_VALUE_VAR"
          "PARTICIPATION"
          "PREVIOUS_PARTICIPATION"
          "PREVIOUS_PARTICIPATION_DIFF"
          "DEVIATION_FROM_AVERAGE"
          "BUDGET"
          "BUDGET_DIFF"
          "BUDGET_VAR"
          "BUDGET_MONTHLY"
          "BUDGET_MONTHLY_DIFF"
          "BUDGET_MONTHLY_COMPLETED"
           ]
  }

  {:name "Facet"
  :fields[{:name "name" :type "String"}
          {:name "extendedInfo" :type "ExtendedInfo"}
          {:name "priority" :type "Integer"}
          {:name "category" :type "FacetCategory"}
          {:name "tagList" :type "TagList"}
          {:name "positiveId" :type "String"}
          {:name "negativeId" :type "String"}
          {:name "positiveCondition" :type "String"}
          {:name "base" :type "Facet"}
          {:name "user" :type "User"}
          {:name "label" :type "String"}
          {:name "labelsQuery" :type "String"}
          {:name "joinId" :type "String"}
          {:name "positiveCondition" :type "String"}
          {:name "groups" :type "GroupMapping" :many true}
          {:name "shared" :type "boolean"}
          {:name "partitioned" :type "boolean"}
         ]
 }

{:name "ExtendedInfo"
  :fields[{:name "columns" :type "Column" :many true}
          {:name "query" :type "String"}
         ]
 }

 {:name "TagList"
  :fields[{:name "dummy" :type "String"}
          {:name "tags" :type "Tag" :many true}
         ]
 }

 {:name "Tag"
  :fields[{:name "position" :type "Integer"}
          {:name "requiredFacet" :type "String"}
          {:name "requiredBaseTable" :type "String"}
          {:name "key" :type "String"}
          {:name "replacement" :type "String"}
         ]
 }

  {:name "GroupMapping"
  :fields[{:name "label" :type "String"}
          {:name "itemsSet" :type "String"}
         ]
 }


 {:name "ComparisonSelection"
  :fields[{:name "comparisonType" :type "ComparisonType"}
          {:name "customFrom" :type "String"}
         ]
 }

  {:name "ComparisonType"
  :kind "enum"
  :values [
          "LAST_MONTH"
          "LAST_YEAR_52_WEEKS"
          "LAST_YEAR_CALENDAR"
          "CUSTOM"
           ]
  }

 {:name "DatasourceWithUnitsSelection"
  :fields[{:name "datasource" :type "Datasource"}
          {:name "unitsStr" :type "String"}
         ]
 }

 {:name "Datasource"
  :fields[{:name "name" :type "String"}
          {:name "category" :type "String"}
          {:name "allowParticipation" :type "boolean"}
          {:name "UIAvailable" :type "boolean"}
          {:name "sign" :type "DatasourceSign"}
          {:name "datasourceConditions" :type "DatasourceCondition" :many true}
         ]
 }

 {:name "DatasourceCondition"
  :fields[{:name "datasource" :type "Datasource"}
          {:name "facet" :type "Facet"}
          {:name "facetCategory" :type "FacetCategory"}
          {:name "clientIdsStr" :type "String"}
         ]
 }


 {:name "FacetCategory"
  :fields[{:name "name" :type "String"}
          {:name "filterOrder" :type "int"}
          {:name "groupingOrder" :type "int"}
         ]
 }



{:name "DatasourceSign"
  :kind "enum"
  :values ["POSITIVE"
           "NEGATIVE"
           "NEUTRAL"
           ]
  }
    
 
 {:name "User"
  :indexed-by "String"
  :fields [{:name "email" :type "String"}
           {:name "plainPassword" :type "String"}
           {:name "name" :type "String"}
           {:name "client" :type "Client"}
           ]
  }

 {:name "Table"
  :indexed-by "String"
  :fields [{:name "reference" :type "boolean"}]
  :childs [{:name "Column"
            :indexed-by "String"
            :fields [{:name "type" :type "DataType"}
                     {:name "property" :type "ColumnProperty"}
                     {:name "columnReference" :type "String"}
                     ]
            }
          ]
}

 {:name "ColumnProperty" 
  :kind "enum"
  :values ["PRIMARY_KEY" 
           "SHARD_KEY" 
           "FOREIGN_KEY" 
          ]
 }
 
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
           "DYNAMIC"
           ]
  }
  ]

