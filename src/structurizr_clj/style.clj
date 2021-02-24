(ns structurizr-clj.style)

(defn add-element
  [styles tag]
  (.addElementStyle styles tag))

(defn background
  "Add background to style-item"
  [style-item hex]
  (.background style-item hex))

(defn color
  "Add color to style-item"
  [style-item hex]
  (.color style-item hex))

(defn shape
  "Add shape to style-item"
  [style-item shape-name]
  (.shape style-item  shape-name))
