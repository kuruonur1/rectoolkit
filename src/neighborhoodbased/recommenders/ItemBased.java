package neighborhoodbased.recommenders;

import base.ARecommender;
import java.util.HashMap;
import java.util.PriorityQueue;

import neighborhoodbased.Neighbour;
import neighborhoodbased.PredictorType;
import neighborhoodbased.measures.Measure;

import data.UIMatrix;

/**
 * 
 * @author onur
 */
public class ItemBased extends ARecommender {
     int k;
     private Measure measure;
     PredictorType pre;

     public ItemBased(int k, Measure m, PredictorType pre) {
          this.k = k;
          this.measure = m;
          this.pre = pre;
     }

     @Override
     public String toString() {
          // TODO Auto-generated method stub
          return "itembased";
     }

     public double predict(int u, int i) {
          // get k neighbours who watched this item
          PriorityQueue<Neighbour> q = this.getKnn(u, i);
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
          double r = matrix.column_mean(i);
          double right = 0;
          for (Neighbour n : q) {
               right += n.rating - matrix.column_mean(n.id);
          }
          return r + right / q.size();
     }

     private double zscore(int u, int i, PriorityQueue<Neighbour> q) {
          double r = matrix.column_mean(i);
          double right = 0;
          for (Neighbour n : q) {
               double item_d = matrix.column_dev(n.id);
               if (item_d == 0) {
                    right += (n.rating - matrix.column_mean(n.id)) / 1;
                    // System.out.println("item_dev is zero!!");
               } else
                    right += (n.rating - matrix.column_mean(n.id)) / item_d;
          }
          return r + (matrix.column_dev(i) * (right / q.size()));
     }

     private PriorityQueue<Neighbour> getKnn(int u, int i) {
          HashMap<Integer, Double> Iu = this.matrix.r_c_r.get(u);

          PriorityQueue<Neighbour> q = new PriorityQueue<Neighbour>();
          if (Iu == null || matrix.c_r_r.get(i) == null)
               return q;

          for (Integer j : Iu.keySet()) {
               if (j == i) {
                    continue;
               }
               double d = this.measure.similarity(this.matrix.c_r_r.get(i),
                         matrix.c_r_r.get(j));
               if (d == 0)
                    continue;
               Neighbour n = new Neighbour(j, d, matrix.c_r_r.get(j).get(u));
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
