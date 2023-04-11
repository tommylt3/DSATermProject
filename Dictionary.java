import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;

public class Dictionary { 
	// variable declaration 
	private HashMap<String,Integer> dictionary = new HashMap<String,Integer>();
   	private int wordCount = 0;
   	private int maxCount = 0;
   	private int uniqueWordCount = 0;
   	private String maxCountWord = null;
   	public Dictionary(){
      
   }
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
   // Reads from JSON file and creates a new dictionary 
   public Dictionary updateFromJSON(String file) throws FileNotFoundException {
      System.out.println("Pending update from : " + file);
      // String is created to store the input file, this is later used to create the new dictionary object 
      StringBuilder jsonCopy = new StringBuilder();
      Scanner sc = new Scanner(new File(file));
      // Reads the input file 
      if(!sc.hasNext()) {
    	  System.out.println("Empty Input File - new object created");
    	  return new Dictionary();
    	  
      }
      while(sc.hasNext()) {
         jsonCopy.append(sc.nextLine());
      }
      // Creates the gson object to create dictionary object using the json file that was the input
      Gson gson = new Gson();
      System.out.println("Update Complete from : " + file);
      return gson.fromJson(jsonCopy.toString(), Dictionary.class);  
   }
   // Updates / Creates a json file for the dictionary object its called for
   public void updateToJSON(String file) throws IOException {
      System.out.println("Exporting to : " + file);
      Gson gson = new Gson();
      String json = gson.toJson(this);
      FileWriter writer = new FileWriter(file);
      writer.write(json);
      writer.close();
      System.out.println("Exporting Complete : " + file);
   }
   
   // Dictionary to text file 
   public void toTextFile(String fileName) throws IOException {
	   FileWriter writer = new FileWriter(fileName);
	   for(String word : dictionary.keySet()) {
          int count = dictionary.get(word);
          writer.write(word + " " + count + "\n");
       }
   }
   public void addFromText(String fileName) throws FileNotFoundException {
	   Scanner sc = new Scanner(new File(fileName));
	   while(sc.hasNext()) {
		   this.addWord(sc.next());
	   }
   }

}
