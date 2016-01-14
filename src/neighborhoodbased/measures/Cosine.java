package neighborhoodbased.measures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 
 * @author onur
 */
public class Cosine extends Measure {
     private HashMap<Integer, Double> v1;
     private HashMap<Integer, Double> v2;
     public int times_calc = 0;
     public int times_called = 0;

     public void set(HashMap<Integer, Double> v1, HashMap<Integer, Double> v2) {
          this.v1 = v1;
          this.v2 = v2;
     }

     public double similarity(HashMap<Integer, Double> v1,
               HashMap<Integer, Double> v2) {
          if (v1 == null || v2 == null)
               return 0;
          Double sim = this.getFromTable(v1.hashCode(), v2.hashCode());
          if (sim == null) {
               sim = this.calc(v1, v2);
               this.setInTable(v1.hashCode(), v2.hashCode(), sim);
               times_calc++;
          }
          times_called++;
          return sim;
     }

     public double calc(HashMap<Integer, Double> n, HashMap<Integer, Double> m) {
          this.set(n, m);
          return dot(v1, v2) / (vectorLength(v1) * vectorLength(v2));
     }

     public double dot(HashMap<Integer, Double> v1, HashMap<Integer, Double> v2) {
          TreeSet<Integer> set = new TreeSet<Integer>(v1.keySet());
          set.retainAll(v2.keySet());

          double dot = 0;
          for (Integer i : set) {
               dot += v1.get(i) * v2.get(i);
          }
          return dot;
     }

     public double vectorLength(HashMap<Integer, Double> v) {
          double len = 0;

          Iterator<Double> ite = v.values().iterator();
          while (ite.hasNext()) {
               double value = ite.next();
               len += value * value;
          }
          return Math.sqrt(len);
     }

     /**/
     public static void main(String args[]) {

          HashMap<Integer, Integer> v1 = new HashMap<Integer, Integer>();
          HashMap<Integer, Integer> v2 = new HashMap<Integer, Integer>();
          /*
           * v1.put(1, 1); v1.put(2,0); v2.put(1,0); v2.put(2,1);
           */
          int[] john = { 5, 0, 0, 4, 4 };
          int[] paul = { 4, 5, 0, 5, 5 };
          int[] george = { 0, 5, 5, 5, 0 };
          int[] ringo = { 4, 2, 4, 5, 0 };
          Cosine c = new Cosine();
          for (int i = 0; i < john.length; i++)
               v1.put(i, john[i]);
          for (int i = 0; i < ringo.length; i++)
               v2.put(i, ringo[i]);

          // System.out.println(c.similarity(v1, v2));
     }

}
