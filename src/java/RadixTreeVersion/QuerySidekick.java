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
import java.util.ArrayList;
import java.util.Arrays;

public class QuerySidekick
{
    String[] guesses = new String[5];  // 5 guesses from QuerySidekick
    int charIdx = 0;      // index/position of char in word of last node
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
        // Returns node with currChar given the node with the last char. This may be the same node if last contains a word instead of a character
        RadixTree.Node temp = radTree.findNode(currChar, last, charIdx+ 1); 
        // if same node, increment charIdxto track which character in the word matches currChar
        if (temp == last) {
            charIdx++;
        } else {    // else it is the first character of a word because it's a new node
            charIdx= 0;
        }
        last = temp;

        // if possible, use existing guesses
        if (last != null && last.oldGuesses[charIdx] != null) {
            guesses = last.oldGuesses[charIdx];
            // store guesses in past guesses radix tree
            for (int j = 0; j < guesses.length; j++) {
                if (guesses[j].length() > 0)
                    pastGuesses.addWord(guesses[j], 1);
            }
        } else {        // else generate guesses and save them
            guesses = new String[5];
            ArrayList<RadixTree.Pair> thePossibleWords = radTree.prefixMatch(last, currWord + (last != null && last.word.length() > charIdx? last.word.substring(charIdx+ 1, last.word.length()) : "" ));
            int k = 0;
            int j = 0;
            while (j < guesses.length) {
                // if there are not enough guesses, fill remaining spots with empty strings
                if (k >= thePossibleWords.size()) {
                    guesses[j] = "";
                    j++;
                } else {
                    // if guess has not been made before, use as guess
                    if (!pastGuesses.isWordInTree(thePossibleWords.get(k).word)) {
                        guesses[j] = thePossibleWords.get(k).word;
                        pastGuesses.addWord(thePossibleWords.get(k).word, 1);
                        k++;
                        j++;
                    } else {
                        k++;
                    }
                }
            }
            
            // store guesses to be made in node for reuse later
            if (last != null) {
                last.oldGuesses[charIdx] = guesses;
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
            charIdx= 0;
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
