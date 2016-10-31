package ngordnet;

import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Justin Liu
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile 
                           + ", and " + hyponymFile + ".");
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        beginUI(ngm, wn);
    }
    
    public static void beginUI(NGramMap ngm, WordNet wn) {
        int start = -1;
        int end = -1;
        while (true) {
            try {
                System.out.print("> ");
                String line = StdIn.readLine();
                if (line == null) {
                    return;
                }
                String[] rawTokens = line.split(" ");
                String command = rawTokens[0];
                String[] tokens = new String[rawTokens.length - 1];
                System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
                switch (command) {
                    case "quit": 
                        return;
                    case "help":
                        In helpIn = new In("/ngordnet/help.txt");
                        String helpStr = helpIn.readAll();
                        System.out.println(helpStr);
                        break;  
                    case "range": 
                        if (Integer.parseInt(tokens[1]) >= Integer.parseInt(tokens[0])) {
                            start = Integer.parseInt(tokens[0]); 
                            end = Integer.parseInt(tokens[1]);
                        } else {
                            System.out.println("Invalid command.");
                        }
                        break;
                    case "count": 
                        String countWord = tokens[0]; 
                        int countYear = Integer.parseInt(tokens[1]);
                        System.out.println(ngm.countInYear(countWord, countYear));
                        break;
                    case "hyponyms": 
                        String hyponymsWord = tokens[0];
                        System.out.println(wn.hyponyms(hyponymsWord));
                        break;
                    case "history": 
                        if (tokens.length == 0) {
                            System.out.println("Invalid command.");
                        } else if (start < 0 || end < 0) {
                            Plotter.plotAllWords(ngm, tokens);
                        } else {
                            Plotter.plotAllWords(ngm, tokens, start, end);
                        }
                        break;
                    case "hypohist": 
                        if (tokens.length == 0) {
                            System.out.println("Invalid command.");
                        } else if (start < 0 || end < 0) {
                            Plotter.plotCategoryWeights(ngm, wn, tokens);
                        } else {
                            Plotter.plotCategoryWeights(ngm, wn, tokens, start, end);
                        }
                        break;
                    case "wordlength": 
                        if (start < 0 || end < 0) {
                            Plotter.plotProcessedHistory(ngm, new WordLengthProcessor());
                        } else {
                            Plotter.plotProcessedHistory(ngm, start, end, 
                                    new WordLengthProcessor());
                        }
                        break;
                    case "zipf": 
                        Plotter.plotZipfsLaw(ngm, Integer.valueOf(tokens[0]));
                        break;
                    default:
                        System.out.println("Invalid command.");  
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException 
                    | NullPointerException e) {
                System.out.println("Invalid command.");  
            }
        }
    }
} 
