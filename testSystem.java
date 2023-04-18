import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.List;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.lang.management.*;

public class testSystem {
   public static void main(String[] args) throws IOException {

      PrintStream o = new PrintStream(new FileOutputStream("results.txt", true));
      System.setOut(o);
      String newFile = "florida_beachQueriesNew.txt";
      String oldFile = "florida_beachQueriesOld.txt";
   
      // for getting cpu time
      ThreadMXBean bean = ManagementFactory.getThreadMXBean();        
      if (!bean.isCurrentThreadCpuTimeSupported())
          {
         System.err.println("cpu time not supported, use wall-clock time:");
                   System.err.println("Use System.nanoTime() instead of bean.getCurrentThreadCpuTime()");
         System.exit(-1);
         }
          
           //Preprocessing in QuerySidekick
      System.out.println("Preprocessing in QuerySidekick...");
           long startPreProcTime = bean.getCurrentThreadCpuTime();
           QuerySidekick qs = new QuerySidekick();
           qs.processOldQueries(oldFile);
           long endPreProcTime = bean.getCurrentThreadCpuTime();
   
      // report time and memory spent on preprocessing
      double cpuPreProc = (endPreProcTime - startPreProcTime)/1E9;
           DecimalFormat df = new DecimalFormat("0.####E0");
      System.out.println("cpu time in seconds (not part of score): "  + 
               df.format(cpuPreProc));
   
      if (cpuPreProc > 600)  // longer than 10 minutes
          {
         System.err.println("Preprocessing took more than 10 minutes, too long.");
         System.exit(-1);
          }
      
           Runtime runtime = Runtime.getRuntime();
      runtime.gc();
           System.out.println("memory in bytes (not part of score): " + df.format((double)peakMemoryUsage()));
   
   
      // simulating new queries
           File file = new File(newFile);
           Scanner input = new Scanner(file);
           
           double totalPercSkipped = 0.0;
           double totalQueries = 0.0;
           double totalGuessess = 0.0;
           double totalElapsedTime = 0.0;
           
      System.out.println("QuerySidekick is guessing...");
           //Perform operations for each line in the file
           while(input.hasNextLine()){
           String query = input.nextLine(); 
          totalQueries++;
          //remove extra spaces with one space
          query = query.replaceAll("\\s+", " ");
          //Remove punctuation from each query
          //query = query.replaceAll("[^a-zA-Z]", "");
   
          //Stores the number of characters in the query.
          int noOfCharactersInQuery = query.length();
          int indexCharacter = 0;
          boolean isCorrectGuess = false;
   
          //Go through every character in the query, and stop if a correct guess was made.
          while(indexCharacter < noOfCharactersInQuery && !isCorrectGuess){
         totalGuessess++;
         //Record start time of the guess
         long startTime = bean.getCurrentThreadCpuTime();
         //Each character is passed to the QuerySidekick program to return 3 gussess
         String[] guesses = qs.guess(query.charAt(indexCharacter), indexCharacter);
         //To calculate the time taken for each guess operation
         long endTime = bean.getCurrentThreadCpuTime();
         totalElapsedTime = totalElapsedTime + (endTime - startTime);
                       
         //Go through the guesses, to see whether there was a correct guess
         String correctGuess = null;
         for(int indexGuess=0; indexGuess < 5; indexGuess++){
             //If there was a correct guess, call the feedback method and calculate percentage of characters skipped
             if(query.equalsIgnoreCase(guesses[indexGuess])){
            isCorrectGuess = true;
            correctGuess = guesses[indexGuess];
            //Calculates the percentage of characters skipped
            totalPercSkipped += ((noOfCharactersInQuery-1-indexCharacter)*100.)/noOfCharactersInQuery;
            break;
             }
         }
         //This is to call feedback
         //If the character entered was the last character in the query, then pass the correct query to the feedback
         startTime = bean.getCurrentThreadCpuTime();
         if(indexCharacter == noOfCharactersInQuery - 1)
             qs.feedback(isCorrectGuess, query);
         else
             qs.feedback(isCorrectGuess, correctGuess);
         endTime = bean.getCurrentThreadCpuTime();
         totalElapsedTime = totalElapsedTime + (endTime - startTime);
   
         //Increment counter to check next character in the query
         indexCharacter++;
          }
      }
   
      input.close();
      
           //Calculate the accuracy
           double accuracy = totalPercSkipped/totalQueries;
           System.out.printf("Accuracy: %.4f\n",accuracy);
   
      if (totalElapsedTime <= 0) // too small to be measured, unlikely 
          totalElapsedTime = 1;
           //Convert elapsed time into seconds, and calculate the Average time
           double avgTime = (totalElapsedTime/1.0E9)/totalGuessess;
           
           //To format the Average time upto 4 decimal places.
           //DecimalFormat df = new DecimalFormat("0.####E0"); // moved to near initialization
           System.out.println("Average time per guess in seconds: " + df.format(avgTime));
           
           // Get the Java runtime
           // Runtime runtime = Runtime.getRuntime();  // moved to near initialization
           // Run the garbage collector
           runtime.gc();
           // Calculate the used memory
           long memory = peakMemoryUsage();
      if (memory <= 0) // too small to be measured, very unlikely
          memory = 1;
           System.out.println("Used memory in bytes: " + df.format((double)memory));
           //OverAll Score
           System.out.printf("Overall Score: %.4f\n",accuracy*accuracy/Math.sqrt(avgTime * memory));
           System.out.println();
   
      QuerySidekick qs2 = qs;  // keep qs used to avoid garbage collection of qs
      qs2.equals(qs);
      }
   
   
       /*
        * return peak memory usage in bytes
        *
        * adapted from
   
        * https://stackoverflow.com/questions/34624892/how-to-measure-peak-heap-memory-usage-in-java 
        */
      private static long peakMemoryUsage() {
   
      List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
      long total = 0;
      for (MemoryPoolMXBean memoryPoolMXBean : pools) {
           if (memoryPoolMXBean.getType() == MemoryType.HEAP)
           {
               long peakUsage = memoryPoolMXBean.getPeakUsage().getUsed();
               // System.out.println("Peak used for: " + memoryPoolMXBean.getName() + " is: " + peakUsage);
               total = total + peakUsage;
           }
           }
   
         return total;
      }

}

