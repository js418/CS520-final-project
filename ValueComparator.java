package final_project;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<String> {
	 Map<String, Double> base;

	    public ValueComparator(Map<String, Double> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(String a, String b) {
	        if (base.get(a) < base.get(b)) {
	            return -1;
	        }
			if (base.get(a) > base.get(b)){
	            return 1;
	        }
	        return 0;
	    }
}
