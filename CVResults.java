package final_project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.inference.TTest;

public class CVResults {
	private List<TreeMap<String,Double>> testmap = new ArrayList<TreeMap<String,Double>>();
	private List<TreeMap<String,Double>> trainmap = new ArrayList<TreeMap<String,Double>>();
	private List<TreeMap<String,Double>> bestmap = new ArrayList<TreeMap<String,Double>>();
	public List<Integer> bestindex = new ArrayList<Integer>();
	public CVResults(List<TreeMap<String,Double>> trainmap,List<TreeMap<String,Double>> testmap){
		this.testmap = testmap;
		this.trainmap = trainmap;
	}
	
	public List<TreeMap<String,Double>> getTestResult(){
		return testmap;
	}
	
	public List<TreeMap<String,Double>> getTrainResult(){
		return trainmap;
	}
	

	public List<TreeMap<String,Double>> getBestResult(){
		return bestmap;
	}
	
	
	public  double CVScore(){
		double score =0.0;
		for(int i=0;i<trainmap.size();i++){
			if (trainmap.get(i).firstKey().equals(testmap.get(i).firstKey())||tTest(trainmap.get(i).firstKey(),testmap.get(i).firstKey(),trainmap)>0.05){
				bestmap.add(trainmap.get(i));
				bestindex.add(i);
				score=score+1.0;
			}
		}
		System.out.println("***********************************************************************************");

		for(TreeMap<String,Double> i : bestmap){
			List<String> s = new ArrayList<String>(i.keySet());
			for(String j : s){
				 
				System.out.print(j+","+i.get(j)+",");
				
				
			}
			System.out.println();
		}
		return score/trainmap.size();
		
	}
	
	public double tTest(String winner, String loser, List<TreeMap<String,Double>> maplist){
		double[] winnerScoreList = new double[maplist.size()];
		double[] loserScoreList = new double[maplist.size()];
		int index= 0;
		
		for (TreeMap<String,Double>i : maplist){
			
			winnerScoreList[index] = i.get(winner);
			loserScoreList[index] = i.get(loser);
			index = index + 1;
		}
		TTest test = new TTest();
		double pvalue = test.pairedTTest(winnerScoreList, loserScoreList);
		return pvalue;
		
	}
	public Set<String> topList(){
		String top = bestmap.get(0).firstKey();
		Set<String> result = new HashSet<>() ;
		result.add(top);
		for (TreeMap<String,Double>map : bestmap){
			for (String s: map.keySet()){
				if(tTest(top,s,trainmap)>0.05){
					result.add(s);
				}else{
					break;
				}
			}
		}
		return result;
		
	}
	
	public List<String> bestRank(){
		List<String> ranklist = new ArrayList<String>();
		String result = new String();
		for(TreeMap<String,Double> i: bestmap){
			
			ranklist.add(i.keySet().toString());
		}
		result =ranklist.get(0);
		int most = Collections.frequency(ranklist, result);
		int index = 0;
		int flag = 0;
		for (String s : ranklist){
			if(Collections.frequency(ranklist, s)>most){
				most = Collections.frequency(ranklist, s);
				result = s;
				flag =index;
			}
			index++;
		}
		
		System.out.println("Frequency:"+most);
		List<String> bestrank = new ArrayList<String>(bestmap.get(flag).keySet());
		System.out.println("best index:"+bestindex);
		
		return bestrank;
	}
	
	public double[] pearsonCorrelation(){
		double[] result = new double[trainmap.size()];
		
		Set<String> techList = trainmap.get(0).keySet();
		double[] train = new double[techList.size()];
		double[] test = new double[techList.size()];
		for(int i=0; i< trainmap.size();i++){
			int index = 0;
			for(String s : techList ){
				train[index]=trainmap.get(i).get(s);
				test[index] = testmap.get(i).get(s);
				index++;
			}
			double corr = new PearsonsCorrelation().correlation(train, test);
			result[i]=corr;
			
		}
		return result;
		
	}
	
}
