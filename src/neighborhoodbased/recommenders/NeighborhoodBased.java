package neighborhoodbased.recommenders;

import java.util.HashMap;
import java.util.PriorityQueue;

import base.ARecommender;
import neighborhoodbased.Neighbour;
import neighborhoodbased.PredictorType;
import neighborhoodbased.measures.Measure;

import data.UIMatrix;

/**
 * 
 * @author onur
 */
public class NeighborhoodBased extends ARecommender {
     int k;
     protected Measure measure;
     PredictorType pre;

     public NeighborhoodBased(int k, Measure m, PredictorType pre) {
          this.k = k;
          this.measure = m;
          this.pre = pre;
     }

     @Override
     public String toString() {
          // TODO Auto-generated method stub
          return "userbased";
     }

     public double predict(int r, int c) {
          // get k neighbours who watched this item
          PriorityQueue<Neighbour> q = this.getKnn(r, c);

          return predict_helper(r, c, q);

     }

     protected double predict_helper(int u, int i, PriorityQueue<Neighbour> q) {
          if (q.isEmpty()) {
               return 3;
          }
          switch (pre) {
          case UNWEIGHTED:
               return unweighted(u, i, q);
          case WEIGHTED:
               return weighted(u, i, q);
          case MEANCENTERING:
               return meancentering(u, i, q);
          case ZSCORE:
               return zscore(u, i, q);
          default:
               System.out.println("hicbirine girmedi");
               return 0;
          }
     }

     private double unweighted(int u, int i, PriorityQueue<Neighbour> q) {
          double totalRates = 0;

          for (Neighbour n : q) {
               totalRates += n.rating;
          }
          return totalRates / q.size();
     }

     private double weighted(int u, int i, PriorityQueue<Neighbour> q) {
          double totalRates = 0;

          double totalSim = 0;
          for (Neighbour n : q) {
               totalRates += (n.similarity * n.rating);
               totalSim += n.similarity;
          }
          return totalRates / totalSim;
     }

     private double meancentering(int u, int i, PriorityQueue<Neighbour> q) {
          double r = matrix.row_mean(u);
          double right = 0;
          for (Neighbour n : q) {
               right += n.rating - matrix.row_mean(n.id);
          }
          return r + right / q.size();
     }

     private double zscore(int u, int i, PriorityQueue<Neighbour> q) {
          double r = matrix.row_mean(u);
          double right = 0;
          for (Neighbour n : q) {
               double user_d = matrix.row_dev(n.id);
               if (user_d == 0) {
                    right += (n.rating - matrix.row_mean(n.id)) / 1;
                    // System.out.println("user_dev is zero!!");
               } else
                    right += (n.rating - matrix.row_mean(n.id)) / user_d;
          }
          return r + (matrix.row_dev(u) * (right / q.size()));
     }

     protected PriorityQueue<Neighbour> getKnn(int r, int c) {
          // get users who rated this item
          HashMap<Integer, Double> r_r = this.matrix.c_r_r.get(c);

          PriorityQueue<Neighbour> q = new PriorityQueue<Neighbour>();
          if (r_r == null)
               return q;

          for (Integer v : r_r.keySet()) {
               if (v == r) {
                    continue;
               }
               double d = this.measure.similarity(this.matrix.r_c_r.get(r),
                         matrix.r_c_r.get(v));
               if (d == 0)
                    continue;
               Neighbour n = new Neighbour(v, d, matrix.r_c_r.get(v).get(c));
               q.offer(n);
               if (q.size() > this.k) {
                    q.poll();
               }
          }
          return q;
     }

     @Override
     public void setMatrix(UIMatrix matrix) {
          // TODO Auto-generated method stub
          this.matrix = matrix;
     }
}
