package ngordnet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class NgordnetTests {

    @Test
    public void testWordNet() {
        WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        assertEquals(true, wn.isNoun("leap"));
        assertEquals(true, wn.isNoun("nasal_decongestant"));
        assertEquals(true, wn.isNoun("jump"));
        
        System.out.println("All nouns:");
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }

        System.out.println("Hypnoyms of increase:");
        for (String noun : wn.hyponyms("increase")) {
            System.out.println(noun);
        }

        System.out.println("Hypnoyms of jump:");
        for (String noun : wn.hyponyms("jump")) {
            System.out.println(noun);
        }  

        System.out.println("Hypnoyms of change:");
        WordNet wn2 = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");
        for (String noun : wn2.hyponyms("change")) {
            System.out.println(noun);
        }  
        
        System.out.println("Hypnoyms of animal:");
        WordNet wn3 = new WordNet("./wordnet/synsets.txt", "./wordnet/hyponyms.txt");
        for (String noun : wn3.hyponyms("animal")) {
            System.out.println(noun);
        }
    }
    
    @Test
    public void testYearlyRecordPutCount() {
        HashMap<String, Integer> year = new HashMap<String, Integer>();
        year.put("s1", 361);
        year.put("s2", 235);
        year.put("s3", 157);
        YearlyRecord nextYear = new YearlyRecord(year);
        assertEquals(nextYear.count("s1"), 361);
        assertEquals(nextYear.count("s2"), 235);
        assertEquals(nextYear.count("s3"), 157);
    }

    @Test
    public void testYearlyRecordCount() {
        YearlyRecord year = new YearlyRecord();
        year.put("s1", 287);
        year.put("s2", 361);
        year.put("s3", 404);
        assertEquals(year.count("s1"), 287);
        assertEquals(year.count("s2"), 361);
        assertEquals(year.count("s3"), 404);
    }

    @Test
    public void testYearlyRecordPut() {
        YearlyRecord year = new YearlyRecord();
        year.put("s1", 287);
        year.put("s2", 361);
        year.put("s3", 404);
        assertEquals(year.count("s1"), 287);
        assertEquals(year.count("s2"), 361);
        assertEquals(year.count("s3"), 404);  

        YearlyRecord nextYear = new YearlyRecord();
        nextYear.put("hello", 9);
        nextYear.put("hello2", 99);
        assertEquals(2, nextYear.rank("hello"));
        nextYear.put("hello", 999);
        assertEquals(1, nextYear.rank("hello"));
    }

    @Test
    public void testYearlyRecordRank() {
        YearlyRecord year = new YearlyRecord();
        year.put("s1", 512);
        year.put("s2", 341);
        year.put("s3", 404);
        year.put("one", 1);
        year.put("two", 2);
        year.put("s1", 3);
        assertEquals(year.rank("s1"), 3);
        assertEquals(year.rank("s2"), 2);
        assertEquals(year.rank("s3"), 1);
        assertEquals(year.rank("one"), 5);
        assertEquals(year.rank("two"), 4);
    }

    @Test
    public void testYearlyRecordWords() {
        YearlyRecord year = new YearlyRecord();
        year.put("s1", 512);
        year.put("s2", 361);
        year.put("s3", 404);
        
        ArrayList<String> next = new ArrayList<String>();
        next.add("s2");
        next.add("s3");
        next.add("s1");
        assertEquals(next, year.words());
    }

    @Test
    public void testNGramMap() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
                                    "./ngrams/total_counts.csv");
        System.out.println(ngm.countInYear("quantity", 1736)); // should print 139
        YearlyRecord year = ngm.getRecord(1736);
        System.out.println(year.count("quantity")); // should print 139

        TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
        System.out.println(countHistory.get(1736)); // should print 139

        TimeSeries<Long> totalCountHistory = ngm.totalCountHistory(); 
        System.out.println(totalCountHistory.get(1736)); // should print 8049773

        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
        System.out.println(weightHistory.get(1736));  // should print roughly 1.7267E-5
    
        System.out.println((double) countHistory.get(1736) 
                           / (double) totalCountHistory.get(1736)); 

        ArrayList<String> words = new ArrayList<String>();
        words.add("quantity");
        words.add("quality");        

        TimeSeries<Double> sum = ngm.summedWeightHistory(words);
        System.out.println(sum.get(1736)); // should print roughly 3.875E-5
    }
    
    @Test
    public void testWordLengthProcessor() {
        YearlyRecord year = new YearlyRecord();
        year.put("sheep", 100);
        year.put("dog", 300);
        WordLengthProcessor wlp = new WordLengthProcessor();
        assertEquals(wlp.process(year), 3.5, 0.1);
    }

    public static void main(String... args) {
        jh61b.junit.textui.runClasses(NgordnetTests.class);
    }
}
