package tests;

import java.util.Random;

import base.ARecommender;
import data.DataSet;
import data.Row;


public class Mae implements Eval {

     public double test(ARecommender rec, DataSet dataSet, int trials) {

          double mae = 0;
          for (int trial = 0; trial < trials; trial++) {
               dataSet.init(new Random(), 0.8);
               rec.setMatrix(dataSet.matrix);
               double totalError = 0;
               for (Row row : dataSet.testSet) {
                    totalError += Math.abs(rec.predict(row.u, row.i) - row.r);
               }
               mae += (totalError / dataSet.testSet.size());
          }
          mae /= trials;
          return mae;
     }

     public String toString() {
          return "mae";
     }
}
