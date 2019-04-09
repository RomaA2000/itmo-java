(defn same-length?
  ([] true)
  ([vector1 & vectors]
   (let [cnt (count vector1)] (every? (fn [x] (== (count x) cnt)) vectors))))

(defn is-my-vector? [& input] (every? vector? input))

(defn is-matrix? [input] (and (vector? input) (apply is-my-vector? input) (apply same-length? input)))

(defn is-tensor? [& input] (if (every? (fn [in]  (number? in)) input) true (and
                               (apply is-tensor? (mapcat (fn [tp] (if (vector? tp) tp [tp])) input))
                               (every? (fn [in] (is-my-vector? in)) input)
                               (every? (fn [in] (same-length? in (first input))) input))))

(defn vector-operator [function]
  (fn [& args] {:pre [(apply same-length? args)]} (apply mapv function args)))

(defn operate-scalar [function]
  (fn ([vector] {:pre [(is-my-vector? vector)]} vector) ([vector scalar & scalars]
     {:pre [(and (is-my-vector? vector) (number? scalar) (apply map number? scalars))]}
     (mapv (fn [in] (function in (* scalar (apply * scalars)))) vector))))

(def v+ (vector-operator +))
(def v- (vector-operator -))
(def v* (vector-operator *))

(defn scalar [& input] (apply + (apply v* input)))

(defn transpose [input]
  {:pre [(is-matrix? input)]}
  (apply mapv vector input))

(defn vect
  ([my-vector1]
   {:pre [(and  (== 3 (count my-vector1)) (is-my-vector? my-vector1))]} my-vector1)
  ([my-vector1 my-vector2]
   {:pre [(and (== 3 (count my-vector1)) (is-my-vector? my-vector1)
               (is-my-vector? my-vector2) (same-length? my-vector1 my-vector2))]}
   [(- (* (my-vector1 1) (my-vector2 2)) (* (my-vector1 2) (my-vector2 1)))
    (- (* (my-vector1 2) (my-vector2 0)) (* (my-vector1 0) (my-vector2 2)))
    (- (* (my-vector1 0) (my-vector2 1)) (* (my-vector1 1) (my-vector2 0)))])
  ([my-vector1 my-vector2 & my-vectors] (reduce vect (vect my-vector1 my-vector2) my-vectors)))

(def v*s (operate-scalar *))
(def m+ (vector-operator v+))
(def m- (vector-operator v-))
(def m* (vector-operator v*))
(def m*s (operate-scalar v*s))

(defn m*m
  ([matrix1]
   {:pre [(is-matrix? matrix1)]} matrix1) ([matrix1 matrix2]
   {:pre [(and (is-matrix? matrix1) (is-matrix? matrix2))]}
   (mapv (fn [row1] (mapv (fn [in] (scalar row1 in)) (transpose matrix2))) matrix1))
  ([matrix1 matrix2 & matrix] (reduce m*m (m*m matrix1 matrix2) matrix)))

(defn m*v [matrix1 vector1]
  {:pre [(and (is-matrix? matrix1) (is-my-vector? vector1))]}
  (mapv (fn [in-row] (scalar in-row vector1)) matrix1))

(defn shape [in] (if (not (number? in)) (cons (count in) (shape (first in))) ()))

(defn form-operator [function] (fn ([my-vector1]
     (if (number? my-vector1) (function my-vector1) (mapv (form-operator function) my-vector1)))
    ([my-vector1 my-vector2] (if (number? my-vector1) (function my-vector1 my-vector2)
       (mapv (form-operator function) my-vector1 my-vector2)))))

(defn ap [tensor1 shape1]
  {:pre (= (nthnext shape1 (- (count shape1) (count (shape tensor1)))) (shape tensor1))}
  (if (same-length? (shape tensor1) shape1) tensor1 (vec (repeat (count shape1) (ap tensor1 (rest shape1))))))

(defn tensor-operator [function] (fn ([tensor1] {:pre [(is-tensor? tensor1)]}
                            ((form-operator function) tensor1))
                         ([tensor1 tensor2]
                          {:pre [(and (form-operator tensor1) (is-tensor? tensor2) (is-tensor? tensor1 tensor2))]}
                          ((form-operator function) (ap tensor1 (max-key count (shape tensor1) (shape tensor2)))
                            (ap tensor2 (max-key count (shape tensor1) (shape tensor2)))))
                         ([tensor1 tensor2 & tensors]
                          (reduce (tensor-operator function) ((tensor-operator function) tensor1 tensor2) tensors))))

(def t+ (tensor-operator +))
(def t* (tensor-operator *))
(def t- (tensor-operator -))
