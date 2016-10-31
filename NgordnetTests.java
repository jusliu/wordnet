import ngordnet.*;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class NgordnetTests {

    @Test
    public void testWordNet() {
        WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        assertEquals(true, wn.isNoun("jump"));
        assertEquals(true, wn.isNoun("leap"));
        assertEquals(true, wn.isNoun("nasal_decongestant"));

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
    public void testPutCount() {
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
    public void testCount() {
        YearlyRecord year = new YearlyRecord();
        year.put("s1", 287);
        year.put("s2", 361);
        year.put("s3", 404);
        assertEquals(year.count("s1"), 287);
        assertEquals(year.count("s2"), 361);
        assertEquals(year.count("s3"), 404);
    }

    @Test
    public void testPut() {
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
    public void testRank() {
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
    public void testWords() {
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
    public void testConstructorGet() {
        TimeSeries<Double> series = new TimeSeries<Double>();
        series.put(1992, 3.6);
        series.put(1993, 9.2);
        series.put(1994, 10.0);
        assertEquals(series.get(1992), 3.6, 0.1);
        assertEquals(series.get(1993), 9.2, 0.1);
        assertEquals(series.get(1994), 10.0, 0.1);

        TimeSeries<Double> series2 = new TimeSeries<Double>(series, 1993, 1994);
        assertEquals(series2.get(1993), 9.2, 0.1);
        assertEquals(series2.get(1994), 10.0, 0.1);
        assertEquals(series2.get(1992), null);

        TimeSeries<Double> series3 = new TimeSeries<Double>(series);
        assertEquals(series3.get(1992), 3.6, 0.1);
        assertEquals(series3.get(1993), 9.2, 0.1);
        assertEquals(series3.get(1994), 10.0, 0.1);
    }

    @Test
    public void testDividedBy() {
        TimeSeries<Double> series = new TimeSeries<Double>();
        series.put(1992, 3.0);
        series.put(1993, 9.0);
        series.put(1994, 10.0);

        TimeSeries<Double> series2 = new TimeSeries<Double>();
        series2.put(1992, 6.0);
        series2.put(1993, 18.0);
        series2.put(1994, 20.0);

        TimeSeries<Double> series3 = new TimeSeries<Double>();
        series3 = series2.dividedBy(series);
        assertEquals(series3.get(1992), 2.0, 0.1);
        assertEquals(series3.get(1993), 2.0, 0.1);
        assertEquals(series3.get(1994), 2.0, 0.1);
    }

    @Test
    public void testPlus() {
        TimeSeries<Double> series = new TimeSeries<Double>();
        series.put(1992, 3.0);
        series.put(1993, 9.0);

        TimeSeries<Double> series2 = new TimeSeries<Double>();
        series2.put(1992, 6.0);
        series2.put(1993, 18.0);
        series2.put(1994, 20.0);

        TimeSeries<Double> series3 = new TimeSeries<Double>();
        series3 = series2.plus(series);
        assertEquals(series3.get(1992), 9.0, 0.1);
        assertEquals(series3.get(1993), 27.0, 0.1);
        assertEquals(series3.get(1994), 20.0, 0.1);
    }
    
    @Test
    public void testCountYear() {
        NGramMap ngm = new NGramMap("./ngrams/very_short.csv", "./ngrams/total_counts.csv");
        assertEquals(175702, ngm.countInYear("airport", 2007));
        assertEquals(173294, ngm.countInYear("airport", 2008));
        assertEquals(677820, ngm.countInYear("request", 2006));
        assertEquals(108634, ngm.countInYear("wandered", 2007));
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