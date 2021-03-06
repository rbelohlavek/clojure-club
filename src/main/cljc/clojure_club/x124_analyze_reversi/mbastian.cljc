(ns clojure-club.x124-analyze-reversi.mbastian)

(defn boardwalk [board player-color start-cell dir]
  (let [f (disj #{'b 'w} player-color) g #{player-color}]
    (when (= 'e (get-in board start-cell))
      (loop [c (mapv + start-cell dir) res []]
        (let [x (get-in board c)]
          (cond
            (f x) (recur (mapv + c dir) (conj res c))
            (g x) res))))))

;TODO - Does assert work in cljs?
(defn analyze [board color]
  (into {}
        (let [directions [[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]]]
          (for [i (range (count board)) j (range (count (board i))) d directions
                :let [b (boardwalk board color [i j] d)]
                :when (not-empty b)]
            [[i j] (set b)]))))

(assert (= {[1 3] #{[1 2]}, [0 2] #{[1 2]}, [3 1] #{[2 1]}, [2 0] #{[2 1]}}
           (analyze '[[e e e e]
                 [e w b e]
                 [e b w e]
                 [e e e e]] 'w)))

(assert (= {[3 2] #{[2 2]}, [3 0] #{[2 1]}, [1 0] #{[1 1]}}
           (analyze '[[e e e e]
                 [e w b e]
                 [w w w e]
                 [e e e e]] 'b)))

(assert (= {[0 3] #{[1 2]}, [1 3] #{[1 2]}, [3 3] #{[2 2]}, [2 3] #{[2 2]}}
           (analyze '[[e e e e]
                 [e w b e]
                 [w w b e]
                 [e e b e]] 'w)))

(assert (= {[0 3] #{[2 1] [1 2]}, [1 3] #{[1 2]}, [2 3] #{[2 1] [2 2]}}
           (analyze '[[e e w e]
                 [b b w e]
                 [b w w e]
                 [b w w w]] 'b)))