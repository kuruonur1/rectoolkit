package tests;

import java.io.IOException;
import java.util.Formatter;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import base.ARecommender;
import modelbased.SVD;
import data.DataSet;
import neighborhoodbased.PredictorType;
import neighborhoodbased.measures.Cosine;
import neighborhoodbased.recommenders.Hybrid;
import neighborhoodbased.recommenders.ItemBased;
import neighborhoodbased.recommenders.UserBased;


public class Test {
     public static CommandLine line = null;

     /**
      * @param args
      * @throws IOException
      * @throws NumberFormatException
      */
     public static Options getOptions() {
          Options options = new Options();
          options.addOption(OptionBuilder.hasArg().create("eval"));
          options.addOption(OptionBuilder.hasArg().create("rec"));
          options.addOption(OptionBuilder.hasArg().create("pre"));
          options.addOption(OptionBuilder.hasArg().create("dat"));
          options.addOption(OptionBuilder.hasArg().create("trials"));
          options.addOption(OptionBuilder.hasArg().create("N"));
          options.addOption(OptionBuilder.hasArg().create("probe"));
          options.addOption(OptionBuilder.hasArg().create("hsentinel"));
          return options;
     }

     public static void main(String[] args) throws NumberFormatException,
               IOException {
          // TODO Auto-generated method stub
          CommandLineParser parser = new GnuParser();
          try {
               // parse the command line arguments
               line = parser.parse(Test.getOptions(), args);
          } catch (Exception exp) {
               // oops, something went wrong
               System.err.println("Parsing failed.  Reason: "
                         + exp.getMessage());
               System.exit(1);
          }
          long start_time = System.currentTimeMillis();
          Eval eval = null;
          if (line.getOptionValue("eval").equals("recall"))
               eval = new Recall();
          else if (line.getOptionValue("eval").equals("mae"))
               eval = new Mae();

          DataSet dataSet = DataSet.read(line.getOptionValue("dat"));
          ARecommender arec = null;
          PredictorType pre = null;
          if (!line.hasOption("pre"))
               pre = PredictorType.UNWEIGHTED;
          else {
               if (line.getOptionValue("pre").equals("unweighted"))
                    pre = PredictorType.UNWEIGHTED;
               else if (line.getOptionValue("pre").equals("weighted"))
                    pre = PredictorType.WEIGHTED;
               else if (line.getOptionValue("pre").equals("meancentering"))
                    pre = PredictorType.MEANCENTERING;
               else if (line.getOptionValue("pre").equals("zscore"))
                    pre = PredictorType.ZSCORE;

          }
          if (line.getOptionValue("rec").equals("userbased"))
               arec = new UserBased(10, new Cosine(), pre);
          else if (line.getOptionValue("rec").equals("itembased"))
               arec = new ItemBased(10, new Cosine(), pre);
          else if (line.getOptionValue("rec").equals("svd"))
               arec = new SVD(dataSet.data, 10, new Random());
          else if (line.getOptionValue("rec").equals("hybrid_itembased"))
               arec = new Hybrid(new ItemBased(10, new Cosine(), pre), 10,
                         new Cosine(), pre);
          else if (line.getOptionValue("rec").equals("hybrid_svd"))
               arec = new Hybrid(new SVD(dataSet.data, 10, new Random()), 10,
                         new Cosine(), pre);

          int trials = 5;
          if (line.hasOption("trials"))
               trials = Integer.parseInt(line.getOptionValue("trials"));

          double res = eval.test(arec, dataSet, trials);
          long end_time = System.currentTimeMillis();
          Formatter formatter = new Formatter(String.format(
                    "%s_%s_%s_%s.result", eval.toString(), arec.toString(),
                    pre, dataSet.toString()));
          formatter.format("%s : %f\n", eval.toString(), res);
          formatter.format("%d seconds\n", (end_time - start_time) / 1000);
          formatter.format("add_info:\n %s", arec.info());
          formatter.close();

     }
}
