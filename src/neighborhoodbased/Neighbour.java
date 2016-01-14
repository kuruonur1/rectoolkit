package neighborhoodbased;

import java.util.PriorityQueue;

/**
 * 
 * @author onur
 */
public class Neighbour implements Comparable<Neighbour> {

     public int id;
     public double similarity;
     public double rating;

     public Neighbour(int u, double s) {
          this.id = u;
          this.similarity = s;
     }

     public Neighbour(int u, double s, double r) {
          this(u, s);
          this.rating = r;
     }

     @Override
     public int compareTo(Neighbour o) {
          /*
           * if (o.similarity > this.similarity) { return 1; } if (o.similarity
           * < this.similarity) { return -1; }
           */
          if (this.similarity < o.similarity)
               return -1;
          if (this.similarity > o.similarity)
               return 1;
          return 0;
     }

     /**/
     public static void main(String args[]) {
          PriorityQueue<Neighbour> q = new PriorityQueue<Neighbour>();
          q.offer(new Neighbour(1, 0.1));
          q.offer(new Neighbour(2, 0.2));
          q.offer(new Neighbour(3, 0.3));
          System.out.println(q.poll());
     }
}
