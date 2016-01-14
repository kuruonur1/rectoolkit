package tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import data.DataSet;
import neighborhoodbased.measures.Cosine;


public class Deneme {
     public static void main(String args[]) throws NumberFormatException,
               IOException {
          DataSet dataSet = DataSet.read(args[0]);
          dataSet.init(new Random(1), .8);
          Cosine cos = new Cosine();
          double t = 0;
          int c = 0;
          for (Integer u : dataSet.matrix.r_c_r.keySet()) {
               HashMap<Integer, Double> i_r = dataSet.matrix.r_c_r.get(u);
               for (Integer v : dataSet.matrix.r_c_r.keySet()) {
                    HashMap<Integer, Double> j_r = dataSet.matrix.r_c_r.get(u);
                    t += cos.calc(i_r, j_r);
                    c++;
               }
          }
          System.out.println(t / c);
     }
}
