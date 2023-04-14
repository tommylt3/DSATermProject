import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Dictionary { 
	// variable declaration 
	private HashMap<String,Integer> dictionary = new HashMap<String,Integer>();
   	private int wordCount = 0;
   	private int maxCount = 0;
   	private int uniqueWordCount = 0;
   	private String maxCountWord = null;
   	public Dictionary(){}
      
   // prints the dictionary
   public void printDictionary() {
	  System.out.println("Words Counted: " + wordCount);
	  System.out.println("Max Count: " + maxCount + " " + maxCountWord);
	  System.out.println("Unique Word Count: " + uniqueWordCount);
      for(String word : dictionary.keySet()) {
         int count = dictionary.get(word);
         System.out.println(word + " " + count);
      }
   }
   // sets the dictionary to the input of this method 
   public void setDictionary(HashMap<String,Integer> input) {
	   this.dictionary = input;
   }
   // Returns the hashmap of this dictionary class 
   public HashMap<String,Integer> getDictionary() {
	   return this.dictionary;
   }
   // If the word exist the words frequency is incremented 
   // if the word does not exist it is added with a frequency of 1 
   public void addWord(String input) {
      int hold = 1;
      wordCount++;
      if(dictionary.get(input) == null) {
         dictionary.put(input, 1);
         uniqueWordCount++;
      } else {
         hold = dictionary.get(input) + 1;
         dictionary.put(input, hold);
      }
      if(hold > maxCount) {
         maxCount = hold;
         maxCountWord = input;
      }
    }
   // Dictionary to text file 
   public void toTextFile(String fileName) throws IOException {
	   FileWriter writer = new FileWriter(fileName);
	   for (String word : dictionary.keySet()) {
          int count = dictionary.get(word);
          writer.write(word + " " + count + "\n");
       }
   }
}
