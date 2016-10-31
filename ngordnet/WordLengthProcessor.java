package ngordnet;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        long wordLengths = 0;
        long counts = 0;
        for (String item : yearlyRecord.words()) {
            wordLengths += item.length() * yearlyRecord.count(item);
            counts += yearlyRecord.count(item);
        }
        return  wordLengths / (double) counts;
    }
}
