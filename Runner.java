import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Runner {

   public static void main(String[] args) throws IOException {
      // TODO Auto-generated method stub
	   //trainDictionray("test3.json","algorithmQueriesOld.txt","test3.json");
	   Dictionary wordHolder = new Dictionary();
	   wordHolder.addFromText("florida_beachQueriesOld.txt");
	   wordHolder.printDictionary();
   }                                                                                  
   // Method that pulls a file given a parameter, pulls the file and creates a dictionary object with that 
   // reads the 2nd parameter which is a text file, and exports the finished dictionary object to a json 
   public static void trainDictionray(String startJSON, String textFile, String endFile) throws IOException {
	   // add warning that start file and end file are the same name and that an overwrite will occur 
	   if(startJSON.equals(endFile)) {
		 System.out.println("Overwrite will occur. Enter 1 to execute this overwrite");  
		 int hold = System.in.read();
		 if(hold != 49) {
			 System.out.println("Overwrite canceled");
			 return;
		 }
	   } 
	   Scanner sc = new Scanner(new File(textFile));
	   Dictionary test = new Dictionary();
	   test = test.updateFromJSON(startJSON);
	   while(sc.hasNext()) {
		   test.addWord(sc.next());
	   }
	   test.updateToJSON(endFile);
	   
   }

}
