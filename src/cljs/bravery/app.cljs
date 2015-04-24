(ns bravery.app
  (:require [reagent.core :as reagent]
            [clojure.pprint :refer [pprint]]
            [clojure.string :refer [split]]))

(def state (reagent/atom {}))

(defn all-pairs
  "Given a input sequence, output sequence of all pairs of elements
   from the input sequence"
  [coll]
  (for [x coll y coll] [x y]))

(defn get-letters
  "Calculate intermediate Brave values from two sequences of
   experimental data"
  [v1 v2]
  (loop [v1 v1
         v2 v2
         a 0
         b 0
         c 0
         d 0]
    (if (and (seq v1) (seq v2))
      (let [b1 (= "+" (first v1))
            b2 (= "+" (first v2))
            na (if (and b1       b2)       (inc a) a)
            nb (if (and (not b1) b2)       (inc b) b)
            nc (if (and b1       (not b2)) (inc c) c)
            nd (if (and (not b1) (not b2)) (inc d) d)]
        (recur (rest v1)
               (rest v2)
               na nb nc nd))
      {:a a :b b :c c :d d})))

(defn zipper
  "The function that is applied to a pair of input rows."
  [v1 v2]
  {:names [(second v1) (second v2)]
   :letters (get-letters (drop 2 v1) (drop 2 v2))})

(def sqrt js/Math.sqrt)

(defn brave
  "Calculate Brave factor from map of intermediate Brave values."
  [{{:keys [a b c d]} :letters :as pair}]
  (let [m1 (+ a c)
        m1s (+ b d)
        m2 (+ a b)
        m2s (+ c d)
        n (+ m1 m1s)
        ns (+ m2 m2s)
        kb (/ (- (* a n) (* m1 m2))
              (* (sqrt (* m1 m1s))
                 (sqrt (* m2 m2s))))]
    (when-not (= n ns) (js/alert "sums don't match"))
    (assoc pair
           :kb kb)))2

(defn calc [csv]
  "Get table in CSV format (string) and, for all pairs of experiments,
   calculate result map with Brave factor. Otput is the sequence of
   result maps."
  (let [lines (split csv #"\n")
        table (map #(split % #",") (drop 2 lines))
        pairs (all-pairs table)
        zipped (mapv #(apply zipper %) pairs)
        result (mapv brave zipped)]
    result))

(defn root
  "Root react component"
  []
  [:div.container
   [:h1 "Paste CSV here:"]
   [:textarea#csv {:key "csv"
                   :style {:height 400
                           :width 600}}]
   [:button.btn.btn-success
    {:on-click (fn []
                 (let [el (js/document.getElementById "csv")]
                   (swap! state assoc :result (calc el.value))))}
    "Go!"]
   (when (:result @state)
     [:div
      [:p (str "total number of pairs: " (count (:result @state)))]
      [:pre (pprint (:result @state))]])])

(defn start! 
  "Main program entry point"
  []
  (reagent/render-component
    [root]
    js/document.body))

; Main

(enable-console-print!)

(set! (.-onload js/window) start!)
