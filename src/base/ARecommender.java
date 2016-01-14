package base;

import data.UIMatrix;

public abstract class ARecommender {

     public abstract double predict(int u, int i);

     protected UIMatrix matrix;

     public abstract void setMatrix(UIMatrix matrix);

     public String info() {
          return "";
     }
}
