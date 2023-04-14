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
    RadixTree tree;
    String currWord;
    ArrayList<Node> listGuesses = new ArrayList<Node>();
    // initialization of ...
    public QuerySidekick()
    {
       
      this.tree = new RadixTree();
      this.currWord = "";
      
    }

    // process old queries from oldQueryFile
    //
    // to remove extra spaces with one space
    // str2 = str1.replaceAll("\\s+", " ");
    public void processOldQueries(String oldQueryFile) throws FileNotFoundException
    {
       tree.buildRadix(oldQueryFile);
    }

    // based on a character typed in by the user, return 5 query guesses in an array
    // currChar: current character typed in by the user
    // currCharPosition: position of the current character in the query, starts from 0
    public String[] guess(char currChar, int currCharPosition)
    {
       this.currWord = this.currWord + currChar;
       System.out.println(this.currWord);
       listGuesses = tree.findPrefix(this.currWord);
       for(int i = 0; i < guesses.length; i++) {
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
    public void feedback(boolean isCorrectGuess, String correctQuery)        
    { 
       if(isCorrectGuess) {
          this.currWord = "";
       }
       

    }

}
