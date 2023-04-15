import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Test {

   public static void main(String[] args) throws FileNotFoundException {
      RadixTree hold = new RadixTree();
      String fileName = "TestNew.txt";
      hold = buildRadix(fileName);
      String prefix = "Austin";
      hold.printBestGuesses(prefix);
      // Currently if the prefix guess is a word that word will return twice in the output array, something wrong with the 
      // 

   }
   public static RadixTree buildRadix(String fileName) throws FileNotFoundException {
      RadixTree out = new RadixTree();
      Scanner sc = new Scanner(new File(fileName));
      while(sc.hasNextLine()) {
         String hold = sc.nextLine();
         out.insert(hold,1);
      }
      return out;
   }

}