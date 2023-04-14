import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.text.DecimalFormat;
import java.lang.management.*;

/*

  Author: Taher Patanwala
  Email: tpatanwala2016@my.fit.edu
  Pseudccode: Philip Chan

  Usage: EvalQuerySidekick oldQueryFile newQueryFile 

  Description:

  The goal is to evaluate QuerySidekick.  The program simulates keystokes
  from a user by reading in newQueryFile, asks for guesses from
  QuerySidekick, provides feedback on correctness, and measures
  performance.  QuerySidekick is provided with oldQueryFile for
  initialization.

     a.  Pseudocode for simulating user keystokes.

     QuerySidekick smartQuery = new QuerySidekick() 
     smartQuery.processOldQuerys(oldQueryFile)  // old queries that the system has seen

     while not end of newQueryFile // to simulate new queries being typed in
       while not end of line
         if a query has more than 1 character
           while not end of query and incorrect guesses
   	     guesses = smartQuery.guess(character, characterPosition)
	     measure performance for the first 5 guesses (ignore the rest if any)
	     smartQuery.feedback(correctGuess, query)

     report performance

     b.  Measuring Performance

         * average percentage of skipped characters for a query

         * average time per guess
     
         * memory usage--before EvalQuerySidekick exits.

	 * overall score--accuracy^2/sqrt(time * memory)  
 */

public class EvalQuerySidekick
{
    /*

    */
    public static void main(String[]args) throws IOException{

	if (args.length != 2) 
	    {
		System.err.println("Usage: EvalQuerySidekick oldQueryFile newQueryFile");
		System.exit(-1);
	    }

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
        qs.processOldQueries(args[0]);
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
        File file = new File(args[1]);
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
		System.out.println(Arrays.toString(guesses));
		//To calculate the time taken for each guess operation
		long endTime = bean.getCurrentThreadCpuTime();
		totalElapsedTime = totalElapsedTime + (endTime - startTime);
                    
		//Go through the guesses, to see whether there was a correct guess
		String correctGuess = null;
		for(int indexGuess=0; indexGuess < 5; indexGuess++){
		    //If there was a correct guess, call the feedback method and calculate percentage of characters skipped
		   
		    System.out.println(query.equalsIgnoreCase(guesses[indexGuess]) + " here " + guesses[indexGuess] + " : " + query);
		    if(query.equalsIgnoreCase(guesses[indexGuess])){
		       System.out.println("eh");
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

	QuerySidekick qs2 = qs;  // keep qs used to avoid garbage collection of qs
    }


    /*
     * return peak memory usage in bytes
     *
     * adapted from

     * https://stackoverflow.com/questions/34624892/how-to-measure-peak-heap-memory-usage-in-java 
     */
    private static long peakMemoryUsage() 
    {

    List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
    long total = 0;
    for (MemoryPoolMXBean memoryPoolMXBean : pools)
        {
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
