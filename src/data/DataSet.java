package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class DataSet {
     public UIMatrix matrix;
     public List<Row> testSet;
     public List<Row> data;
     String name;

     public DataSet(List<Row> d) {
          data = d;
     }

     public static DataSet read(String path) throws NumberFormatException,
               IOException {
          DataSet d = new DataSet(new ArrayList<Row>());
          d.name = path.split("\\.")[0];
          BufferedReader reader = new BufferedReader(new FileReader(path));

          for (String line = reader.readLine(); line != null; line = reader
                    .readLine()) {
               StringTokenizer tokenizer = new StringTokenizer(line);
               // StringTokenizer tokenizer = new StringTokenizer(line, "::");

               Row row = new Row(Integer.parseInt(tokenizer.nextToken()),
                         Integer.parseInt(tokenizer.nextToken()),
                         Double.parseDouble(tokenizer.nextToken()));

               if (row.r > 0 && row.r < 20)
                    d.data.add(row);
          }
          reader.close();
          return d;
     }

     public String toString() {
          return name;
     }

     public void init(Random random, double training_perc) {
          Collections.shuffle(data, random);
          List<Row> trainingData = data.subList(0,
                    (int) (data.size() * training_perc));
          System.out.println("training len:" + trainingData.size());
          matrix = new UIMatrix();
          for (Row instance : trainingData) {
               matrix.set(instance.u, instance.i, instance.r);
          }
          testSet = data.subList(trainingData.size(), data.size());
          System.out.printf("test len: %d\n", testSet.size());
          System.out.printf("#users : %d\n", matrix.r_c_r.keySet().size());
          System.out.printf("#items : %d\n", matrix.c_r_r.keySet().size());
     }
}
