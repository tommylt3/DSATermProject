import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class FileManager {
   public class file {
      String pathName;
      String filePath;

      public String getPathName() {
         return this.pathName;
      }

      public void setPathName(String pathName) {
         this.pathName = pathName;
      }

      public String getFilePath() {
         return this.filePath;
      }

      public void setFilePath(String filePath) {
         this.filePath = filePath;
      }

      public file(String pathName){
         String currentDir = System.getProperty("user.dir");
         this.pathName = pathName;
         this.filePath = currentDir + "/src/data/" + pathName + ".txt";
      }
   }
   // End Of Nested Node Class

   // Class Properties
   file theFile = new file(null);
   File actualFile;

   public FileManager (String pathName){
      file theTemp = new file(pathName);
      this.theFile = theTemp;
      File tempFile = new File(theTemp.getFilePath());
      actualFile = tempFile;
   }

   public ArrayList<String> readFile() throws FileNotFoundException{
      Scanner fileRead = new Scanner(this.actualFile);
      ArrayList<String> theWholeFile = new ArrayList<>();
      while(fileRead.hasNextLine()){
         theWholeFile.add(fileRead.nextLine());
      }
      fileRead.close();
      return theWholeFile;
   }

   public void writeFile(ArrayList<String> temp) throws IOException{
         FileWriter fileWrite = new FileWriter(actualFile, true);
         for (String string : temp) {
            fileWrite.append(string);
         }
         fileWrite.close();
   }
}
