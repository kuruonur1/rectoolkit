package neighborhoodbased.measures;

import java.util.HashMap;

/**
 * 
 * @author onur
 */
public abstract class Measure {
     HashMap<Integer, HashMap<Integer, Double>> table;

     public Measure() {
          this.table = new HashMap<Integer, HashMap<Integer, Double>>();
     }

     protected void setInTable(int x, int y, double d) {
          int n = x, m = y;
          if (x > y) {
               n = y;
               m = x;
          }
          table.get(n).put(m, d);
     }

     protected Double getFromTable(int x, int y) {
          int n = x, m = y;
          if (x > y) {
               n = y;
               m = x;
          }
          HashMap<Integer, Double> h = this.table.get(n);
          if (h == null) {
               h = new HashMap<Integer, Double>();
               table.put(n, h);
          }
          return h.get(m);
     }

     public abstract double similarity(HashMap<Integer, Double> n,
               HashMap<Integer, Double> m);
}
