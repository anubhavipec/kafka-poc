import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {


        int numRequests = 10000;
        int numThreads = 30;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> performIORequest("dsg"), executorService);
            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        long startTime = System.currentTimeMillis();


        /*
        .thenRun waits for all CompletableFUtures present in allFutures to complete and then apply callback functions to it.
         */
        allFutures.thenRun(() -> {

            //Applies below callback after all of them have been completed
            System.out.println("Now applying call back");
            List<String> results = new ArrayList<>();

            for (CompletableFuture<String> future : futures) {
                try {
                    String result = future.get();
                    results.add(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            // Process the results as needed
            System.out.println("Results: " + results);
            System.out.println("Execution time: " + executionTime + " ms");
        });

        executorService.shutdown();
    }

    private static String performIORequest(String a) {
        // Simulating an IO request that takes 800 ms on average
        try {
            System.out.println("Running in "+Thread.currentThread().toString());
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the result of the IO request
        return "IO request result";
    }

}






