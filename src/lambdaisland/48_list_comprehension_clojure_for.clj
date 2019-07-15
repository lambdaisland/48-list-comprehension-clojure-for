(ns lambdaisland.48-list-comprehension-clojure-for
  (:require [clojure.string :as str]))


(for [x (range 2 7)]
  x)
;; => (2 3 4 5 6)


;; |          | lazy-seq | nil (side effects) |
;; |----------+----------+--------------------|
;; | function | map      | run!               |
;; | macro    | for      | doseq              |


(map inc (range 5))
;; => (1 2 3 4 5)


(run! inc (range 5))
;; => nil


(run! println (range 5))


(do
  (map println (range 5))
  nil)


(for [x (range 5)]
  (inc x))
;; => (1 2 3 4 5)


(doseq [x (range 5)]
  (println x))


mapv ;; function | eager | returns vector

(mapv inc (range 5))
;; => [1 2 3 4 5]



(defn url-parts [url]
  (next (re-matches #"(\w+)://([^/]+)(.*)" url)))

(url-parts "https://lambdaisland.com/episodes")
;; => ("https" "lambdaisland.com" "/episodes")

(let [[scheme domain path] (url-parts "https://lambdaisland.com/episodes")]
  {:sheme scheme
   :domain domain
   :path path})
;; => {:sheme "https", :domain "lambdaisland.com", :path "/episodes"}


(defn url-map [url]
  (let [[scheme domain path] (url-parts url)]
    {:scheme scheme
     :domain domain
     :path path}))

(let [{:keys [scheme domain path]} (url-map "https://lambdaisland.com/episodes")]
  (str scheme "://" domain "/robots.txt"))
;; => "https://lambdaisland.com/robots.txt"




(def urls ["http://gaiwan.co/"
           "http://lambdaisland.com/"
           "http://clojureverse.org/"])

(for [{:keys [scheme domain]} (map url-map urls)]
  (str scheme "://" domain "/favicon.ico"))
;; => ("http://gaiwan.co/favicon.ico"
;;     "http://lambdaisland.com/favicon.ico"
;;     "http://clojureverse.org/favicon.ico")


(defn keywordize-map-keys [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))

(keywordize-map-keys {"lambda" "island"})
;; => {:lambda "island"}


(defn keywordize-map-keys2 [m]
  (into {}
        (map (juxt (comp keyword key) val))
        m))

(keywordize-map-keys2 {"lambda" "island"})
;; => {:lambda "island"}



(for [x (range 2)
      y [:a :b]
      z [:+ :-]]
  [x y])
;; => ([0 :a] [0 :a] [0 :b] [0 :b] [1 :a] [1 :a] [1 :b] [1 :b])



(def board
  [[:R :N :B :K :Q :B :N :R]
   [:P :P :P :P :P :P :P :P]
   [:_ :_ :_ :_ :_ :_ :_ :_]
   [:_ :_ :_ :_ :_ :_ :_ :_]
   [:_ :_ :_ :_ :_ :_ :_ :_]
   [:_ :_ :_ :_ :_ :_ :_ :_]
   [:p :p :p :p :p :p :p :p]
   [:r :n :b :k :q :b :n :r]])

(for [x (range 8)
      y (range 8)]
  [(str (get "ABCDEFGH" y) (inc x))
   (get-in board [x y])])
;; => (["A1" :R]
;;     ["B1" :N]
;;     ["C1" :B]
;;     ["D1" :K]
;;     ["E1" :Q]
;;     ["F1" :B]
;;     ["G1" :N]
;;     ["H1" :R]
;;     ["A2" :P]
;;     ["B2" :P]
;;     ["C2" :P]
;;     ["D2" :P]
;;     ["E2" :P]
;;     ["F2" :P]
;;     ["G2" :P]
;;     ["H2" :P]
;;     ["A3" :_]
;;     ["B3" :_]
;;     ["C3" :_]
;;     ["D3" :_]
;;     ["E3" :_]
;;     ["F3" :_]
;;     ["G3" :_]
;;     ["H3" :_]
;;     ["A4" :_]
;;     ["B4" :_]
;;     ["C4" :_]
;;     ["D4" :_]
;;     ["E4" :_]
;;     ["F4" :_]
;;     ["G4" :_]
;;     ["H4" :_]
;;     ["A5" :_]
;;     ["B5" :_]
;;     ["C5" :_]
;;     ["D5" :_]
;;     ["E5" :_]
;;     ["F5" :_]
;;     ["G5" :_]
;;     ["H5" :_]
;;     ["A6" :_]
;;     ["B6" :_]
;;     ["C6" :_]
;;     ["D6" :_]
;;     ["E6" :_]
;;     ["F6" :_]
;;     ["G6" :_]
;;     ["H6" :_]
;;     ["A7" :p]
;;     ["B7" :p]
;;     ["C7" :p]
;;     ["D7" :p]
;;     ["E7" :p]
;;     ["F7" :p]
;;     ["G7" :p]
;;     ["H7" :p]
;;     ["A8" :r]
;;     ["B8" :n]
;;     ["C8" :b]
;;     ["D8" :k]
;;     ["E8" :q]
;;     ["F8" :b]
;;     ["G8" :n]
;;     ["H8" :r])




(->> (for [suite ["♠" "♥" "♦" "♣"]
           rank  (concat (range 2 11) ["A" "J" "Q" "K"])]
       (str rank suite))
     shuffle
     (take 7))
;; => ("10♠" "8♦" "4♥" "6♠" "6♥" "J♣" "10♣")


(into #{}
      (for [ns  (all-ns)
            var (vals (ns-publics ns))
            k   (keys (meta var))]
        k))
;; => #{:redef :no-doc :private :protocol :added :ns :name :special-form :file
;;      :potemkin/body :static :inline-arities :skip-wiki
;;      :nrepl.middleware/descriptor :column :author :const :dynamic :line :macro
;;      :deprecated :declared :url :style/indent :tag :arglists :see-also :doc :forms
;;      :inline}


(for [x (range 8)
      y (range 8)
      :let [pos   (str (get "ABCDEFGH" y) (inc x))
            piece (get-in board [x y])]
      :when (#{:K :k :Q :q} piece)]
  [pos piece])
;; => (["D1" :K] ["E1" :Q] ["D8" :k] ["E8" :q])



(for [idx    (range)
      :let   [x     (mod idx 8)
              y     (quot idx 8)
              piece (get-in board [x y])]
      :while piece
      :when  (#{:K :Q :k :q} piece)
      :let   [pos (str (get "ABCDEFGH" y) x)]]
  [pos piece])
;; => (["D0" :K] ["D7" :k] ["E0" :Q] ["E7" :q])


(into #{}
      (for [ns    (all-ns)
            :let  [n (str (ns-name ns))]
            :when (str/starts-with? n "clojure")
            var   (vals (ns-publics ns))
            [k v] (meta var)
            :when (string? v)]
        k))
;; => #{:added :file :deprecated :tag :doc}



(defn user-table [ids]
  [:table
   [:thead
    [:tr [:th "Name"] [:th "Email"]]]
   `[:tbody
     ~@(for [id ids]
         [:tr
          [:td @(re-frame/subscribe [:user/name id])]
          [:td @(re-frame/subscribe [:user/email id])]])]])




























