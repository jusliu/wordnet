package ngordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.princeton.cs.introcs.In;

public class NGramMap {
    
    private TimeSeries<Long> totalCountHistory;
    private HashMap<Integer, YearlyRecord> records;
    private HashMap<String, TimeSeries<Integer>> countHistories;
    
    public NGramMap(String wordsFilename, String countsFilename) {
        records = new HashMap<Integer, YearlyRecord>();
        countHistories = new HashMap<String, TimeSeries<Integer>>();
        In wordsRead = new In(wordsFilename);
        String[] wordsLines = wordsRead.readAllLines();
        for (int i = 0; i < wordsLines.length; i++) {
            String[] elements = wordsLines[i].split("\t");
            if (!records.containsKey(Integer.valueOf(elements[1]))) {
                records.put(Integer.valueOf(elements[1]), new YearlyRecord());
            }
            records.get(Integer.valueOf(elements[1])).put(elements[0], 
                    Integer.valueOf(elements[2]));
            if (!countHistories.containsKey(elements[0])) {
                countHistories.put(elements[0], new TimeSeries<Integer>());
            }
            countHistories.get(elements[0]).put(Integer.valueOf(elements[1]), 
                    Integer.valueOf(elements[2]));
        }
        
        totalCountHistory = new TimeSeries<Long>();
        In countsRead = new In(countsFilename);
        String[] countsLines = countsRead.readAllLines();
        for (int i = 0; i < countsLines.length; i++) {
            String[] elements = countsLines[i].split(",");
            totalCountHistory.put(Integer.valueOf(elements[0]), Long.valueOf(elements[1]));
        }
    }
    
    public TimeSeries<Integer> countHistory(String word) {
        return countHistories.get(word);
    }
    
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        return new TimeSeries<Integer>(countHistories.get(word), startYear, endYear);
    }
    
    public int countInYear(String word, int year) {
        return records.get(year).count(word);
    }
    
    public YearlyRecord getRecord(int year) {
        YearlyRecord record = records.get(year);
        YearlyRecord recordCopy = new YearlyRecord();
        ArrayList<String> recordWords = (ArrayList<String>) record.words();
        ArrayList<Number> recordCounts = (ArrayList<Number>) record.counts();
        for (int i = 0; i < recordWords.size(); i++) {
            recordCopy.put(recordWords.get(i), (int) recordCounts.get(i));
        }
        return recordCopy;
    }
    
    public TimeSeries<Double> processedHistory(int startYear, 
            int endYear, YearlyRecordProcessor yrp) {
        return new TimeSeries<Double>(processedHistory(yrp), startYear, endYear);
    }
    
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> processed = new TimeSeries<Double>();
        for (int year : records.keySet()) {
            processed.put(year, yrp.process(records.get(year)));
        }
        return processed;
    }
    
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        for (String item : words) {
            sum = sum.plus(weightHistory(item));
        }
        return sum;
    }
    
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
            int startYear, int endYear) {
        return new TimeSeries<Double>(summedWeightHistory(words), startYear, endYear);
    }
    
    public TimeSeries<Long> totalCountHistory() {
        return totalCountHistory;
    }
    
    public TimeSeries<Double> weightHistory(String word) {
        if (countHistory(word) == null) {
            return new TimeSeries<Double>();
        }
        TimeSeries<Long> temp = new TimeSeries<Long>();
        TimeSeries<Long> countHistoryLong = new TimeSeries<Long>();
        for (int item : countHistory(word).keySet()) {
            temp.put(item, totalCountHistory.get(item));
            countHistoryLong.put(item, countHistory(word).get(item).longValue());
        }
        return countHistoryLong.dividedBy(temp);
    }
    
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        return new TimeSeries<Double>(weightHistory(word), startYear, endYear);
    }
}
