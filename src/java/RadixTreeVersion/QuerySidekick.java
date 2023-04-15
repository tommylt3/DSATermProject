/*
  Authors (group members): Austin Phillips, Evan Thompson, Fiona Cahalan, and Tommy Gingerelli
  Email addresses of group members:
  Group name: FATE

  Course: CSE2010
  Section: 14

  Description of the file:
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

    // processes old queries from oldQueryFile
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
        
        String thePossibleWords[] = radTree.prefixMatch(last, currWord + (last != null && last.word.length() > i ? last.word.substring(i + 1, last.word.length()) : "" ));
        
        // doesn't use old guesses
        /*
        int k = 0;
        int j = 0;
        while (j < guesses.length) {
            if (k >= thePossibleWords.length) {
                guesses[j] = "";
                j++;
            } else {
                if (!pastGuesses.isWordInTree(thePossibleWords[k])) {
                    guesses[j] = thePossibleWords[k];
                    pastGuesses.addWord(radTree.root, thePossibleWords[k], 1);
                    k++;
                    j++;
                } else {
                    k++;
                }
            }
        }
        */

        int k = 0;
        int j = 0;
        while (j < guesses.length) {
            if (k >= thePossibleWords.length) {
                guesses[j] = "";
                j++;
            } else {
                guesses[j] = thePossibleWords[k];
                k++;
                j++;
            }
        }
        
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
