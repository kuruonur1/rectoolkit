package neighborhoodbased.measures;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * 
 * @author onur
 */
public class AdjustedCosine extends Measure {

     private double mean(HashMap<Integer, Double> n) {
          double total = 0;
          for (Double i : n.values()) {
               total += i;
          }
          return total / n.values().size();
     }

     @Override
     public double similarity(HashMap<Integer, Double> n,
               HashMap<Integer, Double> m) {
          // TODO Auto-generated method stub

          TreeSet<Integer> set = new TreeSet<Integer>(n.keySet());
          set.retainAll(m.keySet());

          double mean = this.mean(n);
          double dot = 0, v1Len = 0, v2Len = 0;
          for (Integer i : set) {
               double x = n.get(i) - mean, y = m.get(i) - mean;
               dot += x * y;
               v1Len = x * x;
               v2Len = y * y;
          }

          return dot / Math.sqrt(v1Len * v2Len);
     }

}
