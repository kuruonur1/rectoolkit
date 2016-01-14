package neighborhoodbased.recommenders;

import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

import base.ARecommender;
import neighborhoodbased.Neighbour;
import neighborhoodbased.PredictorType;
import neighborhoodbased.measures.Measure;
import tests.Test;

import data.UIMatrix;

/**
 * 
 * @author onur
 */
public class Hybrid extends UserBased {
     HashMap<Integer, PriorityQueue<Neighbour>> topNNTable;
     ARecommender arec;
     int topknn_used = 0;
     int predict_called = 0;
     double hsentinel = 0.3;

     public Hybrid(ARecommender a, int k, Measure m, PredictorType pre) {
          super(k, m, pre);
          this.topNNTable = new HashMap<Integer, PriorityQueue<Neighbour>>();
          arec = a;
          if (Test.line.hasOption("hsentinel"))
               hsentinel = Double.parseDouble(Test.line
                         .getOptionValue("hsentinel"));
     }

     @Override
     public String info() {
          return String.format(
                    "perc: %.2f  topknn_used:%d predict_called:%d\n",
                    (double) topknn_used / predict_called, topknn_used,
                    predict_called);
     }

     public String toString() {
          // TODO Auto-generated method stub
          return "hybrid-" + arec.toString();
     }

     public double predict(int u, int i) {
          predict_called++;
          PriorityQueue<Neighbour> knn = this.getKnn(u, i);
          /*
           */
          if (knn.isEmpty()) {
               PriorityQueue<Neighbour> topknn = this.getTopKnn(u, i);
               topknn_used++;
               return predict_helper(u, i, topknn);
          } else {
               // Neighbour n = Collections.max(topknn);
               Neighbour n2 = Collections.max(knn);
               // if (n.similarity > n2.similarity + 0.2) {
               if (n2.similarity < hsentinel) {
                    PriorityQueue<Neighbour> topknn = this.getTopKnn(u, i);
                    topknn_used++;
                    return predict_helper(u, i, topknn);
               } else {
                    return predict_helper(u, i, knn);
               }
          }
     }

     public PriorityQueue<Neighbour> getTopKnn(int u, int i) {

          PriorityQueue<Neighbour> q = topNNTable.get(u);
          if (q == null) {
               q = new PriorityQueue<Neighbour>();

               for (Integer v : matrix.r_c_r.keySet()) {
                    if (v == u) {
                         continue;
                    }
                    /**/
                    double d = this.measure.similarity(
                              this.matrix.r_c_r.get(u), matrix.r_c_r.get(v));
                    if (d == 0)
                         continue;
                    q.offer(new Neighbour(v, d));
                    if (q.size() > this.k) {
                         q.poll();
                    }
               }
               topNNTable.put(u, q);
          }
          for (Neighbour n : q) {
               if (matrix.r_c_r.get(n.id) == null)
                    n.rating = matrix.column_mean(i);
               else if (matrix.r_c_r.get(n.id).get(i) == null)
                    n.rating = arec.predict(n.id, i);
               else
                    n.rating = matrix.r_c_r.get(n.id).get(i);
          }
          // System.out.printf("compared_users : %d\n", compared_users);
          return q;

     }

     @Override
     public void setMatrix(UIMatrix matrix) {
          // TODO Auto-generated method stub
          super.setMatrix(matrix);
          arec.setMatrix(matrix);
     }
}
