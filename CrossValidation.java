package final_project;

import java.util.*;
import java.util.stream.Collectors;

public class CrossValidation {
	protected List<dataResult> datasample = new ArrayList<>();
	protected int k;
	private int key;
	public CrossValidation(int k, List<dataResult> data,int key){
		this.datasample = data;
		this.k = k;
		this.key=key;
	}

	protected CVResults perform(){
		List<TreeMap<String,Double>> testmap = new ArrayList<TreeMap<String,Double>>();
		List<TreeMap<String,Double>> trainmap = new ArrayList<TreeMap<String,Double>>();
		Set<String> FL_techniques = new HashSet<>();
		Set<String> Bugs = new HashSet<>();
		for (dataResult i:datasample){
        	Bugs.add(i.getBugInfo().getBugLabel());
        	FL_techniques.add(i.getFLtech().getLabel());	
        }
		List<String> BugList = new ArrayList<String>(Bugs);

		
	 
		int length = BugList.size();
		int[] order = new int[length];
	    for (int i = 0; i < length; i++) {
	        order[i] = i;
	    }
	    int flag =0;
	    int subLength = length / 3*2 ;
	    while (flag<k){
	    	System.out.println(flag);
		    Random r = new Random();
		    for(int i=0;i<length;i++)
		    {
		    	int index = r.nextInt(length);
		    	int temp = order[0];
		    	order[0] = order[index];
		    	order[index] = temp;
		    }
		    

		    List<String> data = new ArrayList<>();

		    for (int i : order){
		    	data.add(BugList.get(i));
		    }
	    
	    
	    
	    	List<String> trainbug = data.subList(0, subLength); 
	    	List<dataResult> train = new ArrayList<dataResult>();
	    	List<dataResult> test = new ArrayList<dataResult>();

	    	for(dataResult i : datasample){
	    		if (trainbug.contains(i.getBugInfo().getBugLabel())){
	    			train.add(i);
	    			
	    		}
	    		else{
	    			test.add(i);
	    		}
	    	}

	    	if (key==1){
	    		testmap.add(score1(test));
	    		trainmap.add(score1(train));
	    	}
	    	else if(key==2){
	    		testmap.add(score2(test));
	    		trainmap.add(score2(train));
	    	}
	    	else{
	    		testmap.add(score2_2(test));
	    		trainmap.add(score2_2(train));
	    	}
	    	
	    	
	    	flag=flag+1;
	    	
	    	
	    }
    	CVResults results = new CVResults(trainmap,testmap);

	    return results;
	   
	  
	    
	}
	private double mean(List<dataResult> l){
		double sum=0.0;
		for(dataResult i : l){
			sum=sum+i.getFLscore();
		}
		
		return sum/l.size();
		
	}
	
	private TreeMap<String, Double> score1(List<dataResult> l){
		Set<String> FL_techniques = new HashSet<>();
		HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		for(dataResult i:l){
            FL_techniques.add(i.getFLtech().getLabel());
		}
		
		for (String tech : FL_techniques){
			List<dataResult> techlist = PickOneTech(tech,l);
			map.put(tech, mean(techlist));
		}
		
		sorted_map.putAll(map);
	  
		
		
		return sorted_map;
	}
	
	
	private TreeMap<String, Double> score2(List<dataResult> l){
		

		Set<String> FL_techniques = new HashSet<>();
		
		Set<String> Bugs = new HashSet<>();
		HashMap<String, Double> bugmap = new HashMap<String, Double>();

        
        HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
        
        
        HashMap<String, Double> techcount = new HashMap<String, Double>();
       
        
        for (dataResult i:l){
        	Bugs.add(i.getBugInfo().getBugLabel());
        	FL_techniques.add(i.getFLtech().getLabel());	
        }
        
        for(String tech: FL_techniques){
        	techcount.put(tech, 0.0);
        }
		//System.out.println("FL_techniques" +FL_techniques.size());

        for(String tech: FL_techniques){
        	map.put(tech, 0.0);
        }
        
        for(String bug : Bugs){
        	List<dataResult> buglist = PickOneBug(bug,l);
        	for (String tech : FL_techniques){
    			List<dataResult> techlist = PickOneTech(tech,buglist);
    			if (techlist.size()>0){
    				techcount.put(tech, techcount.get(tech)+1.0);
    			}
    			bugmap.put(tech, mean(techlist));
    		}
			//System.out.println("bugmap"+bugmap.size());
			ValueComparator bvcbug = new ValueComparator(bugmap);
			TreeMap<String, Double> sorted_bugmap = new TreeMap<String, Double>(bvcbug);
        	sorted_bugmap.putAll(bugmap);
			//System.out.println("sorted_bugmap"+sorted_bugmap.size());
        	List<String> list = new ArrayList<String>(sorted_bugmap.keySet());
			//System.out.println("sorted tech label"+list.size());
/*
			for (int i = 0; i< list.size();i++){
				map.put(list.get(i),map.get(list.get(i))+i);
				//System.out.println(list.get(i)+ " " + i);
			}
*/
			for (String tech : FL_techniques){
				double index=1.0;
				for (String s : list){

					if (s.equals(tech)){
						map.put(tech,map.get(tech)+index);
						break;
					}
					index=index+1.0;
				}
				//System.out.println(tech+ " " + index);
			}

        }
		for(String s : map.keySet()){
        	map.put(s,map.get(s)/techcount.get(s));
        }

        sorted_map.putAll(map);
        return sorted_map;
        
	}
	
	private List<dataResult>  PickOneBug(String s,List<dataResult> dataset){
		List<dataResult> result = new ArrayList();
		for(dataResult i : dataset)
			if(i.getBugInfo().getBugLabel().equals(s)){
				result.add(i);
			}
		
		return result;
	}
	private List<dataResult> PickOneTech(String s,List<dataResult> dataset){
		List<dataResult> result = new ArrayList<>();
		for(dataResult i : dataset)
			if(i.getFLtech().getLabel().equals(s)){
				result.add(i);
			}
		
		return result;
	}

	public TreeMap<String, Double> score2_2(List<dataResult> data){
		Set<String> bug_name = new HashSet<>();
		Map<String,Map<String, Integer>> bug_score_list = new HashMap<>();
		Set<String> all_tech = CSVreader.getFLtech(this.datasample);
		for(dataResult d: data){
			bug_name.add(d.getBugInfo().getBugLabel());
		}
		for(String bug: bug_name){
			List<dataResult> bug_result = PickOneBug(bug, data);
			Set<String> bug_tech_name = CSVreader.getFLtech(bug_result);
			Map<String, Double> flscore_bug = new HashMap<>();
			for(String b:all_tech){
				if(!bug_tech_name.contains(b)){
					flscore_bug.put(b,1.0);
				}
				else{
					List<dataResult> bug_tech = PickOneTech(b,bug_result);
					double flscore = mean(bug_tech);
					flscore_bug.put(b,flscore);
				}
			}
			List<String> tech_name_list = new ArrayList<>(all_tech);
			Collections.sort( tech_name_list, ( o1, o2 ) -> flscore_bug.get(o1).compareTo(flscore_bug.get(o2)));
			Map<String,Integer> tech_rank_score = new HashMap<>();
			for(int i=0;i<tech_name_list.size();i++){
				tech_rank_score.put(tech_name_list.get(i),i);
			}
			bug_score_list.put(bug,tech_rank_score);
		}
		
		TreeMap<String, Double> r = new TreeMap<>();
		for(String fl: all_tech){
			double sum=0.0;
			for(String bug:bug_name){
				sum += bug_score_list.get(bug).get(fl);
			}
			r.put(fl,sum/bug_name.size());
		}

		LinkedHashMap<String,Double> results = r.entrySet().stream().sorted((e1,e2)-> e1.getValue().compareTo(e2.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		TreeMap<String,Double> sorted = new TreeMap<>(results);

		return sorted;

	}

}