import java.io.FileNotFoundException;
import java.util.ArrayList;

/*

  Authors (group members):
  Email addresses of group members:
  Group name:

  Course:
  Section:

  Description of the overall algorithm:


*/


public class QuerySidekick
{
   String[] guesses = new String[5];  // 5 guesses from QuerySidekick
   RadixTree radTree = new RadixTree();
   public QuerySidekick(){}

    // process old queries from oldQueryFile
    //
    // to remove extra spaces with one space
    // str2 = str1.replaceAll("\\s+", " ");
   public void processOldQueries(String oldQueryFile) throws FileNotFoundException{
      FileManager aFile = new FileManager(oldQueryFile);
      ArrayList<String> aFileContents = aFile.readFile();
      for (String word : aFileContents) {
         radTree.addWord(radTree.root , word, 1);
      }
      radTree.printTree();
   }

    // based on a character typed in by the user, return 5 query guesses in an array
    // currChar: current character typed in by the user
    // currCharPosition: position of the current character in the query, starts from 0
   public String[] guess(char currChar, int currCharPosition){
      String[] guesses = new String[5];
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
   
   }

}
