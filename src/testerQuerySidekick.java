import java.io.FileNotFoundException;

public class testerQuerySidekick {
   public static void main(String args[]){
      QuerySidekick testThis = new QuerySidekick();
      try {
         testThis.processOldQueries("src/algorithmQueriesNew.txt");
      } 
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }
}
