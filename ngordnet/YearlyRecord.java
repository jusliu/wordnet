package ngordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class YearlyRecord {
    
    private HashMap<String, Integer> data;
    private ArrayList<Number> ranks;
    private ArrayList<String> words;
    private boolean ranksUpdated;
    
    public YearlyRecord() {
        ranks = new ArrayList<Number>();
        words = new ArrayList<String>();
        ranksUpdated = false;
        data = new HashMap<String, Integer>();
    }
    
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        ranks = new ArrayList<Number>();
        words = new ArrayList<String>();
        ranksUpdated = false;
        data = new HashMap<String, Integer>();
        TreeSet<String> keys = new TreeSet<String>();
        keys.addAll(otherCountMap.keySet());
        while (!keys.isEmpty()) {
            String key = keys.first();
            put(key, otherCountMap.get(key));
            keys.remove(key);
        }
    }
    
    public int count(String word) {
        if (word == null || data.get(word) == null) {
            return 0;
        }
        return data.get(word);
    }
    
    public Collection<Number> counts() {
        if (!ranksUpdated) {
            updateRanks();
        }
        return ranks;
    }
    
    public void put(String word, int count) {
        data.put(word, count);
        words.add(word);
        ranksUpdated = false;
    }
    
    public int rank(String word) {
        if (!ranksUpdated) {
            updateRanks();
        }
        return ranks.size() - ranks.indexOf(data.get(word));
    }
    
    public int size() {
        return data.size();
    }
    
    public Collection<String> words() {
        if (!ranksUpdated) {
            updateRanks();
        }
        return words;
    }
    
    private void updateRanks() {
        class RecordComparator implements Comparator<String> {
            HashMap<String, Integer> data;
            public RecordComparator(HashMap<String, Integer> map) {
                data = map;
            }
            
            public int compare(String s1, String s2) {
                if (data.get(s1) < data.get(s2)) {
                    return -1;
                }
                return 1;
            }
        }
        TreeMap<String, Integer> counts = new TreeMap<String, Integer>(new RecordComparator(data));
        counts.putAll(data);
        ranks = new ArrayList<Number>(counts.values());
        words = new ArrayList<String>(counts.keySet());
        ranksUpdated = true;
    }
    
}

/*
package ngordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class YearlyRecord {
    
    private HashMap<String, Integer> data;
    
    public YearlyRecord() {
        data = new HashMap<String, Integer>();
    }
    
    @SuppressWarnings("unchecked")
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        data = (HashMap<String, Integer>) otherCountMap.clone();
    }
    
    public int count(String word) {
        return data.get(word);
    }
    
    public Collection<Number> counts() {
        CustomRecordComparator comp = new CustomRecordComparator(data);
        TreeMap<String, Integer> counts = new TreeMap<String, Integer>(comp);
        counts.putAll(data);
        return new ArrayList<Number>(counts.values());
    }
    
    public void put(String word, int count) {
        data.put(word, count);
    }
    
    public int rank(String word) {
        CustomRecordComparator comp = new CustomRecordComparator(data);
        TreeMap<String, Integer> counts = new TreeMap<String, Integer>(comp);
        counts.putAll(data);
        return counts.size() - new ArrayList<Number>(counts.values()).indexOf(data.get(word));
    }
    
    public int size() {
        return data.size();
    }
    
    public Collection<String> words() {
        return data.keySet();
    }
}

class CustomRecordComparator implements Comparator<String> {

    HashMap<String, Integer> data;
    public CustomRecordComparator(HashMap<String, Integer> data) {
        this.data = data;
    }
    
    public int compare(String s1, String s2) {
        if (data.get(s1) >= data.get(s2)) {
            return 1;
        } else {
            return -1;
        }
    }
}
*/


 /*
package ngordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public class YearlyRecord {
    
    private HashMap<String, Integer> data;
    private ArrayList<String> ranks; 
    
    public YearlyRecord() {
        data = new HashMap<String, Integer>();
        ranks = new ArrayList<String>()y
    }
    
    @SuppressWarnings("unchecked")
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        //data = (HashMap<String, Integer>) otherCountMap.clone();
        data = new HashMap<String, Integer>();
        TreeSet<String> keys = new TreeSet<String>();
        keys.addAll(otherCountMap.keySet());
        ranks = new ArrayList<String>();
        while (!keys.isEmpty()) {
            String key = keys.first();
            put(key, otherCountMap.get(key));
            keys.remove(key);
        }
    }
    
    private void updateRanks(String word, int count) {
        int rank = 1;
        for (Number item : new ArrayList<Number>(counts())) {
            if ((int) item > count) {
                rank++;
            }
        }
        ranks.add(rank-1, word);
    }
    
    public int count(String word) {
        return data.get(word);
    }
    
    public Collection<Number> counts() {
        Collection<Integer> counts = data.values();
        return new ArrayList<Number>(counts);
    }
    
    public void put(String word, int count) {
        data.put(word, count);
        updateRanks(word, count);
    }
    
    public int rank(String word) {
        int count = data.get(word);
        int rank = 1;
        for (Number item : new ArrayList<Number>(counts())) {
            if ((int) item > count) {
                rank++;
            }
        }
        return rank;
    }
    
    public int size() {
        return data.size();
    }
    
    public Collection<String> words() {
        return data.keySet();
    }
}
*/
