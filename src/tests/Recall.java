package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import base.ARecommender;
import data.DataSet;
import data.Row;


public class Recall implements Eval {
     public String toString() {
          return "recall";
     }

     static class Node implements Comparable<Node> {

          Row ro;
          double rating;

          Node(Row r, double rating) {
               ro = r;
               this.rating = rating;
          }

          @Override
          public int compareTo(Node o) {
               if (this.rating < o.rating)
                    return 1;
               else if (this.rating > o.rating)
                    return -1;

               return 0;
          }
     }

     public double test(ARecommender rec, DataSet dataSet, int trials) {

          double recall = 0;
          int N = 20;

          if (Test.line.hasOption("N"))
               N = Integer.parseInt(Test.line.getOptionValue("N"));

          for (int trial = 0; trial < trials; trial++) {
               dataSet.init(new Random(), 0.986);
               rec.setMatrix(dataSet.matrix);
               int T_len = 0, hits = 0;
               double probe = dataSet.matrix.max_rating * 0.85;
               if (Test.line.hasOption("probe"))
                    probe = Double.parseDouble(Test.line
                              .getOptionValue("probe"));
               // by u
               for (Row row : dataSet.testSet) {
                    if (row.r < probe) {// only consider 5 star ratings
                         continue;
                    }
                    if (dataSet.matrix.r_c_r.get(row.u) == null)// user hasnt
                                                                // watched
                                                                // anything
                         continue;
                    T_len++;// count the number of 5 stars
                    // / get/ all // items // from // trainning // set
                    Set<Integer> items = new HashSet<Integer>(
                              dataSet.matrix.c_r_r.keySet());
                    // get // all // items // user // rated // from // trainning
                    // set
                    HashSet<Integer> rated = new HashSet<Integer>(
                              dataSet.matrix.r_c_r.get(row.u).keySet());
                    // get items unrated by u
                    items.removeAll(rated);
                    ArrayList<Integer> unrateds = new ArrayList<Integer>(items);
                    Collections.shuffle(unrateds, new Random());

                    PriorityQueue<Node> top_n_list = new PriorityQueue<Node>();

                    top_n_list.offer(new Node(row, rec.predict(row.u, row.i)));

                    int K = unrateds.size() < 300 ? unrateds.size() : 300;
                    for (Integer unrated_item : unrateds.subList(0, K)) {
                         top_n_list.offer(new Node(new Row(row.u, unrated_item,
                                   0), rec.predict(row.u, unrated_item)));
                    }
                    if (top_n_list.size() != (K + 1))
                         throw new RuntimeException(String.format(
                                   "u:%d top_n_size:%d\n", row.u,
                                   top_n_list.size()));

                    for (int i = 1; !top_n_list.isEmpty() && i <= N; i++) {
                         Node n = top_n_list.poll();
                         if (n.ro.u == row.u && n.ro.i == row.i) {
                              hits++;
                              break;
                         }
                    }
               }
               System.out.printf("%.2f stars len: %d\n", probe, T_len);
               System.out.printf("hits : %d\n", hits);
               double t_recall = (double) hits / T_len;
               // double precision = t_recall / N;
               recall += t_recall;
          }
          return recall / trials;
     }
}
