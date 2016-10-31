package ngordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.introcs.In;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Set;


public class WordNet {
    
    private Digraph graph;
    private HashMap<Integer, String> synsets;
    private TreeSet<Integer> hypIDs;
    
    public WordNet(String synsetFilename, String hyponymFilename) {
        In synRead = new In(synsetFilename);
        String[] synLines = synRead.readAllLines();
        synsets = new HashMap<Integer, String>(synLines.length);
        for (int i = 0; i < synLines.length; i++) {
            String[] elements = synLines[i].split(",");
            synsets.put(i, elements[1]);
        }
    
        In hypRead = new In(hyponymFilename);
        String[] hypLines = hypRead.readAllLines();
        graph = new Digraph(synsets.size());
        for (String item : hypLines) {
            String[] elements = item.split(",");
            for (int u = 1; u < elements.length; u++) {
                graph.addEdge(Integer.valueOf(elements[0]), Integer.valueOf(elements[u]));
            }
        }
        hypIDs = new TreeSet<Integer>();
    }
    
    public boolean isNoun(String word) {
        return nouns().contains(word);
    }
    
    public Set<String> nouns() {
        TreeSet<String> nouns = new TreeSet<String>();
        for (int i = 0; i < synsets.size(); i++) {
            String[] elements = synsets.get(i).split(" ");
            for (String item : elements) {
                nouns.add(item);
            }
        }
        return nouns;
    }
    
    public Set<String> hyponyms(String word) {
        updateHyps(word);
        TreeSet<String> hyps = new TreeSet<String>();
        while (!hypIDs.isEmpty()) {
            int item = hypIDs.first();
            hypIDs.remove(item);
            String[] elements = synsets.get(item).split(" ");
            for (int i = 0; i < elements.length; i++) {
                hyps.add(elements[i]);
            }
        }
        return hyps;
    }
    
    private void updateHyps(String word) {
        for (int i = 0; i < synsets.size(); i++) {
            String[] elements = synsets.get(i).split(" ");
            for (int u = 0; u < elements.length; u++) {
                if (elements[u].equals(word)) {
                    updateHypIDs(i);
                }
            }
        }
    }
    
    private void updateHypIDs(int synID) {
        hypIDs.add(synID);
        Iterable<Integer> vertices = graph.adj(synID);
        for (int item : vertices) {
            if (!hypIDs.contains(item)) {
                hypIDs.add(item);
                updateHypIDs(item);
            }
        }
    }
} 
