(defn operator [op] (fn [& operands] (fn [args] (apply op (mapv (fn [input] (input args)) operands)))))

(def add (operator +))
(def subtract (operator -))
(def multiply (operator *))
(def divide (operator (fn [x & y] (/ (double x) (apply * y)))))
(def negate subtract)
(def constant constantly)
(defn variable [type] (fn [in] (in type)))
(def med (operator (fn [& args] (nth (sort args) (int (/  (count args) 2))))))
(def avg (operator (fn [& args] (/ (apply + args) (count args)))))

(def operation {'+ add '- subtract '* multiply '/ divide 'negate negate 'med med 'avg avg})

(defn parse [input] {:pre [(or (seq? input) (number? input) (symbol? input))]}
    (cond
        (seq? input) (apply (operation (first input)) (map parse (rest input)))
        (number? input) (constant input)
        (symbol? input) (variable (str input))))

(defn parseFunction [input] {:pre [(string? input)]} (parse (read-string input)))

