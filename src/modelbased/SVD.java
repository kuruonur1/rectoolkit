package modelbased;


import base.ARecommender;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import data.Row;
import data.UIMatrix;


public class SVD extends ARecommender {
     double gamma = 0.01;
     double lambda = 0.1;
     double mu = 0;
     public double error = 0;
     HashMap<Integer, double[]> p;
     HashMap<Integer, double[]> q;
     HashMap<Integer, Double> bI;
     HashMap<Integer, Double> bU;
     int f = 10;
     // public List<int[]> trainingData;
     Random random;

     public SVD(List<Row> data, int f, Random random) {
          this.random = random;
          p = new HashMap<Integer, double[]>();
          q = new HashMap<Integer, double[]>();
          bI = new HashMap<Integer, Double>();
          bU = new HashMap<Integer, Double>();
          this.f = f;
          double totalRating = 0;

          for (Row row : data) {
               bU.put(row.u, random.nextDouble() - 0.5);
               p.put(row.u, this.randomRow());
               bI.put(row.i, random.nextDouble() - 0.5);
               q.put(row.i, this.randomRow());
               totalRating += row.r;
          }
          this.mu = totalRating / data.size();
          // System.out.printf("avg rating : %f\n", this.mu);
     }

     private double[] randomRow() {
          double[] r = new double[f];
          for (int i = 0; i < r.length; i++)
               r[i] = random.nextDouble() - 0.5;
          return r;
     }

     public double predict(int u, int i) {
          double sum = 0;
          double[] q_i = q.get(i);
          double[] p_u = p.get(u);

          for (int j = 0; j < this.f; j++)
               sum += (q_i[j] * p_u[j]);

          return mu + bI.get(i) + bU.get(u) + sum;
     }

     public void iterate(int times) {
          for (int t = 0; t < times; t++) {
               error = 0;
               for (Integer user : matrix.r_c_r.keySet()) {
                    for (Integer item : matrix.r_c_r.get(user).keySet()) {

                         int[] row = { user, item };
                         double e = matrix.r_c_r.get(user).get(item)
                                   - predict(row[0], row[1]);
                         error += Math.abs(e);

                         double b_i = bI.get(row[1]);
                         double b_u = bU.get(row[0]);
                         b_i = b_i + gamma * (e - lambda * b_i);
                         b_u = b_u + gamma * (e - lambda * b_u);

                         double[] q_i = q.get(row[1]);
                         double[] tmp = q_i.clone();
                         double[] p_u = p.get(row[0]);

                         for (int i = 0; i < this.f; i++) {
                              q_i[i] = q_i[i] + gamma
                                        * (e * p_u[i] - lambda * q_i[i]);
                              p_u[i] = p_u[i] + gamma
                                        * (e * tmp[i] - lambda * p_u[i]);
                         }

                         bI.put(row[1], b_i);
                         bU.put(row[0], b_u);
                         q.put(row[1], q_i);
                         p.put(row[0], p_u);
                    }
               }
          }
     }

     @Override
     public String toString() {
          // TODO Auto-generated method stub
          return "svd";
     }

     static void print(double[] a) {
          for (int i = 0; i < a.length; i++)
               System.out.printf("%.2f ", a[i]);
          System.out.println();
     }

     @Override
     public void setMatrix(UIMatrix matrix) {
          // TODO Auto-generated method stub
          this.matrix = matrix;
          iterate(30);
     }
}
