package final_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class Main {
    public static void main(String[] args) {
        try{
            String real_vs_aritificial = "data/scores_artificial_vs_real.new.csv";
            String real = "data/scores_real_exploration.csv";
            String[] data_name = new String[]{"real_vs_aritificial faults","real faults"};
            String[] data_path = new String[]{real_vs_aritificial,real};
            for (int i=0; i<2; i++){
                CSVreader reader = new CSVreader(data_path[i]);
                List<dataResult> data = reader.getData_info();
                Set<String> FL_techniques = CSVreader.getFLtech(data);
                System.out.println("Data for "+ data_name[i]);
                System.out.println("number of FL techniques is: " + FL_techniques.size());
                System.out.println("The list of the FL techniques:");
                //System.out.println(FL_techniques);
                System.out.println();
            }

            // test the correlation between real and artificial faults
            CSVreader reader = new CSVreader(real_vs_aritificial);
            CSVreader reader2 = new CSVreader(real);
            List<dataResult> all_faults = reader.getData_info();
            List<dataResult> real_faults = CSVreader.getRealFaults(all_faults);
            List<dataResult> artificial_faults = CSVreader.getArtificialFaults(all_faults,real_faults);
            List<dataResult> all_faults2 = reader2.getData_info();

            Set<String> FL_set = CSVreader.getFLtech(all_faults);
            String[] FL_tech = FL_set.toArray(new String[FL_set.size()]);
            Map<String,Double> score_list_real = CSVreader.getScore(real_faults);
            Map<String,Double> score_list_artificial = CSVreader.getScore(artificial_faults);
            double[] avg_score_real = new double[FL_set.size()];
            double[] avg_score_artificial = new double[FL_set.size()];
            for(int j = 0; j<FL_tech.length;j++){
                avg_score_real[j] = score_list_real.getOrDefault(FL_tech[j],0.0);
                avg_score_artificial[j] = score_list_artificial.getOrDefault(FL_tech[j],0.0);
            }
            //Pearson's Correlation
            double corr = new PearsonsCorrelation().correlation(avg_score_real,avg_score_artificial);
            System.out.println("Pearson's correlation coefficients for real and artificial faults is: " + corr);
            System.out.println();

            // cross validation
            CrossValidation cv1 = new CrossValidation(100,artificial_faults,2);
            CVResults result1 = cv1.perform();
            System.out.println("**************************************************************************");

            System.out.println("CV  good result of metric 1:");
            double score1 = result1.CVScore();
            System.out.println("score for the cv:"+score1);
            
            System.out.println("**************************************************************************");
            System.out.println("Top list:"+result1.topList());
            
            System.out.println("**************************************************************************");
            List<String> bestrank = result1.bestRank();
            System.out.println("Best Rank:"+bestrank);
            System.out.println("p value for each neighbors:");
            for(int i =0; i<6;i++){
            	System.out.print(result1.tTest(bestrank.get(i), bestrank.get(i+1), result1.getTrainResult())+";   ");
            }
            System.out.println();
            System.out.println("**************************************************************************");
            System.out.println("correlation coefficient for each iteration: ");
            int i= 0;
            for(double p: result1.pearsonCorrelation()){
            	System.out.println(i+" " +p);
            	i++;
            }
            
            
            /*
            System.out.println("**************************************************************************");
            CrossValidation cv2 = new CrossValidation(10,artificial_faults,2);
            CVResults result2 = cv2.perform();
            
            System.out.println("CV good result of metric 2 :");
            double score2 = result2.CVScore();
            System.out.println("score for the cv:"+score2);
            
          	*/
            

          

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}