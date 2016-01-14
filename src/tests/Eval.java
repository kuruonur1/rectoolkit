package tests;

import base.ARecommender;
import data.DataSet;

public interface Eval {
     double test(ARecommender rec, DataSet dataSet, int trials);
}
