package final_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVreader {
    private List<dataResult> data_info = new ArrayList<>();

    public CSVreader(String dataPath) throws IOException{
        String line = "";
        String cvsSplitBy = ",";
        List<dataResult> results = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(dataPath));
        Map<fullLabel,Double> all_data = new HashMap<>();
        Map<fullLabel,Double> full_label_num =new HashMap<>();
        while ((line = br.readLine()) != null) {

            line = line.replaceAll(",,",",-,");
            String[] data = line.split(cvsSplitBy);

            if (!data[0].equals("Project")){
                FLTechnique FLTech = new FLTechnique(data[4],data[5],data[6],data[7],data[8],data[9]);
                bugInfo bug = new bugInfo(data[0],data[1]);
                fullLabel full_label = new fullLabel(bug,FLTech);
                all_data.put(full_label, all_data.getOrDefault(full_label, 0.0)+Double.parseDouble(data[11]));
                full_label_num.put(full_label, full_label_num.getOrDefault(full_label, 0.0)+1.0);
            }

        }
        br.close();
        for(fullLabel f:all_data.keySet()){
        	double score = all_data.get(f)/full_label_num.get(f);
        	dataResult result = new dataResult(f.getBugInfo(),f.getFLtech(),score);
            results.add(result);
        }
        this.data_info = results;
    }

    public List<dataResult> getData_info() {
        return this.data_info;
    }


    public static List<dataResult> getRealFaults(List<dataResult> data){
        List<dataResult> results = new ArrayList<>();
        for(dataResult d: data){
            if(Integer.parseInt(d.getBugInfo().getBugNum())<300){
                results.add(d);
            }
        }
        return  results;
    }

    public static List<dataResult> getArtificialFaults(List<dataResult> all_faults, List<dataResult> real_faults){
        List<dataResult> artificial_faults = new ArrayList<>();
        for (dataResult d:all_faults){
            if(!real_faults.contains(d)){
                artificial_faults.add(d);
            }
        }
        return artificial_faults;
    }

    public static Set<String> getFLtech(List<dataResult> data){
        Set<String> FL_techniques = new HashSet<>();
        for(int j=0; j<data.size();j++){
            FL_techniques.add(data.get(j).getFLtech().getLabel());
        }
        return  FL_techniques;
    }


    public static Map<String,Double> getScore(List<dataResult> data){
        Map<String,Double> total_scores = new HashMap<>();
        Map<String,Double> tech_num = new HashMap<>();
        Map<String,Double> avg_scores = new HashMap<>();
        for (dataResult d:data){
            String FL_label = d.getFLtech().getLabel();
            total_scores.put(FL_label,total_scores.getOrDefault(FL_label,0.0)+d.getFLscore());
            tech_num.put(FL_label,tech_num.getOrDefault(FL_label,0.0)+1.0);
        }
        for (String f: total_scores.keySet()){
            double score = total_scores.get(f)/tech_num.get(f);
            avg_scores.put(f,score);
        }
        return avg_scores;
    }



}