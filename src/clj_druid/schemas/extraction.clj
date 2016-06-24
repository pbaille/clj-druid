(ns clj-druid.schemas.extraction
  (:require [schema.core :as s]
            [clj-druid.schemas.search-query :refer [searchQuery]]))

(s/defschema regularExpressionExtractionFunction
  {:type (s/enum :regex)
   :expr s/Str
   (s/optional-key :replaceMissingValue) s/Bool
   (s/optional-key :replaceMissingValueWith) s/Str})

(s/defschema partialExtractionFunction
  {:type (s/enum :partial)
   :expr s/Str})

(s/defschema searchQueryExtractionFunction
  {:type (s/enum :searchQuery)
   :query searchQuery})

(s/defschema substringExtractionFunction
  {:type (s/enum :substring)
   :index s/Int
   (s/optional-key :length) s/Int})

(s/defschema timeFormatExtractionFunction
  {:type (s/enum :timeFormat)
   :format s/Str
   :timeZone s/Str
   :locale s/Str})

(s/defschema timeParsingExtractionFunction
  {:type (s/enum :time)
   :timeFormat s/Str
   :resultFormat s/Str})

(s/defschema javascriptExtractionFunction
  {:type (s/enum :javascript)
   :function s/Str
   (s/optional-key :injective) s/Bool})

(s/defschema lookupExtractionFunction
  {:type (s/enum :lookup)
   :lookup (s/conditional
             #(= :map (:type %)) {:type (s/enum :map)
                                  :map {s/Keyword s/Str}}
             #(= :namespace (:type %)) {:type (s/enum :namespace)
                                        :namespace s/Str})
   (s/optional-key :replaceMissingValue) s/Bool
   (s/optional-key :replaceMissingValueWith) s/Str
   (s/optional-key :injective) s/Bool})

(declare extractionFn)

(s/defschema cascadeExtractionFunction
  {:type (s/enum :cascade)
   :extractionFns [extractionFn]})

(s/defschema upperExtractionFunction
  {:type (s/enum :upper)
   (s/optional-key :locale) s/Str})

(s/defschema lowerExtractionFunction
  {:type (s/enum :lower)
   (s/optional-key :locale) s/Str})

(s/defschema extractionFn
  (s/conditional
   #(= :regex (:type %)) regularExpressionExtractionFunction
   #(= :partial (:type %)) partialExtractionFunction
   #(= :searchQuery (:type %)) searchQueryExtractionFunction
   #(= :substring (:type %)) substringExtractionFunction
   #(= :timeFormat (:type %)) timeFormatExtractionFunction
   #(= :time (:type %)) timeParsingExtractionFunction
   #(= :javascript (:type %)) javascriptExtractionFunction
   #(= :lookup (:type %)) lookupExtractionFunction
   #(= :cascade (:type %)) cascadeExtractionFunction
   #(= :upper (:type %)) upperExtractionFunction
   #(= :lower (:type %)) lowerExtractionFunction))

(s/defschema extraction
  {:type (s/enum :extraction)
   :dimension s/Str
   :outputName s/Str
   :extractionFn extractionFn})
