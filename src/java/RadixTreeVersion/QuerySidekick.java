/*

  Authors (group members):
  Email addresses of group members:
  Group name:

  Course:
  Section:

  Description of the overall algorithm:


*/

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileReader;

public class QuerySidekick
{
    String[] guesses = new String[5];  // 5 guesses from QuerySidekick
    int i = 0;
    RadixTree radTree = new RadixTree();
    RadixTree.Node last = radTree.root;
    String currWord = "";
    RadixTree pastGuesses = new RadixTree();
    int lines = 0;

    // initialization of ...
    public QuerySidekick() {}

    public void processOldQueries(String oldQueryFile) throws FileNotFoundException{
        Scanner reader = new Scanner(new FileReader(oldQueryFile));
        while (reader.hasNextLine()) {
            radTree.addWord(radTree.root, reader.nextLine().replaceAll("\\s+", " ").toLowerCase(), 1);
        }
        System.gc();
    }


    // based on a character typed in by the user, return 5 query guesses in an array
    // currChar: current character typed in by the user
    // currCharPosition: position of the current character in the query, starts from 0

    public String[] guess(char currChar, int currCharPosition){
        currWord = currWord + currChar;
        RadixTree.Node temp = radTree.findNode(currChar, last, i + 1);
        if (temp == last) {
            i++;
        } else {
            i = 0;
        }
        last = temp;
        // System.out.printf("%ncurrent Node: %s i: %d%n" , last != null && last.word != null ? last.word : "", i);
        
        String thePossibleWords[] = radTree.prefixMatch(last, currWord + (last != null && last.word.length() > i ? last.word.substring(i + 1, last.word.length()) : "" ));
        
        /*
        System.out.println("results: " + thePossibleWords.length);
        for (int k = 0; k < thePossibleWords.length && k < 5; k++) {
            System.out.println(thePossibleWords[k]);
        }
        */
        
        
        int k = 0;
        int j = 0;
        while (j < guesses.length) {
            if (j >= thePossibleWords.length) {
                guesses[j] = "";
                j++;
            } else {
                if (!pastGuesses.isWordInTree(thePossibleWords[j])) {
                    guesses[j] = thePossibleWords[j];
                    i++;
                    j++;
                }
                i++;
            }
        }
        
        
        /*
        for (int j = 0; j < guesses.length; j++) {
            if (j >= thePossibleWords.length) {
                guesses[j] = "";
            } else {
                guesses[j] = thePossibleWords[j];
                // pastGuesses.addWord(radTree.root, thePossibleWords[j], 1);
            }
        }
        */
        
        return guesses;
    }


    // feedback on the 5 guesses from the user
    // isCorrectGuess: true if one of the guesses is correct
    // correctQuery: 3 cases:
    // a.  correct query if one of the guesses is correct
    // b.  null if none of the guesses is correct, before the user has typed in 
    //            the last character
    // c.  correct query if none of the guesses is correct, and the user has 
    //            typed in the last character
    // That is:
    // Case       isCorrectGuess      correctQuery   
    // a.         true                correct query
    // b.         false               null
    // c.         false               correct query
    public void feedback(boolean isCorrectGuess, String correctQuery){
        if (!isCorrectGuess && correctQuery == null){
            // The Guesses For The Word Aren't Done But Aren't Right
        } else {
            i = 0;
            last = radTree.root;
            currWord = "";
            pastGuesses = new RadixTree();
            lines++;
            if (lines > 100) {
                lines = 0;
                System.gc();
            }
        }
    }
}
