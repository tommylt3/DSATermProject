import java.io.FileNotFoundException;

public class testerQuerySidekick {
   public static void main(String args[]){
      QuerySidekick testThis = new QuerySidekick();
      try {
         testThis.processOldQueries("algorithmQueriesOld.txt");
      } 
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }

   }
}
