package data;

import java.util.HashMap;

public class UIMatrix {
     public HashMap<Integer, HashMap<Integer, Double>> r_c_r = new HashMap<Integer, HashMap<Integer, Double>>();
     public HashMap<Integer, HashMap<Integer, Double>> c_r_r = new HashMap<Integer, HashMap<Integer, Double>>();
     private HashMap<Integer, Double> c_means = new HashMap<Integer, Double>();
     private HashMap<Integer, Double> r_means = new HashMap<Integer, Double>();
     private HashMap<Integer, Double> c_devs = new HashMap<Integer, Double>();
     private HashMap<Integer, Double> r_devs = new HashMap<Integer, Double>();
     public double max_rating = 0;

     public int row_count() {
          return r_c_r.keySet().size();
     }

     public int column_count() {
          return c_r_r.keySet().size();
     }

     public void set(int u, int i, double r) {
          if (r > max_rating)
               max_rating = r;
          if (this.r_c_r.get(u) == null) {
               this.r_c_r.put(u, new HashMap<Integer, Double>());
          }

          this.r_c_r.get(u).put(i, r);

          if (this.c_r_r.get(i) == null) {
               this.c_r_r.put(i, new HashMap<Integer, Double>());
          }

          this.c_r_r.get(i).put(u, r);
     }

     public double column_mean(int i) {
          if (c_means.get(i) == null) {
               double total = 0;
               for (Double r : c_r_r.get(i).values())
                    total += r;
               c_means.put(i, total / c_r_r.get(i).values().size());
          }
          return c_means.get(i);
     }

     public double column_dev(int i) {
          if (c_devs.get(i) == null) {
               double total = 0;
               double mean = column_mean(i);
               for (Double r : c_r_r.get(i).values())
                    total += Math.pow(r - mean, 2);
               c_devs.put(i, Math.sqrt(total));
          }
          return c_devs.get(i);
     }

     public double row_dev(int u) {
          if (r_devs.get(u) == null) {
               double total = 0;
               double mean = row_mean(u);
               for (Double r : r_c_r.get(u).values())
                    total += Math.pow(r - mean, 2);
               r_devs.put(u, Math.sqrt(total));
          }
          return r_devs.get(u);
     }

     public double row_mean(int u) {
          if (r_means.get(u) == null) {
               double total = 0;
               for (Double r : r_c_r.get(u).values())
                    total += r;
               r_means.put(u, total / r_c_r.get(u).values().size());
          }
          return r_means.get(u);
     }

}
