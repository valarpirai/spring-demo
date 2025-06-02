import java.util.ArrayList;
import java.util.List;

public class OutOfMemorySimulator1 {
    List<Object> memoryHog = new ArrayList<>();

    void simulateOom() {
        // Create and start a child thread
            try {
                while (true) {
                    // Add large objects to the list
                    memoryHog.add(new byte[1024 * 1024]); // Allocates 1MB per object
                    System.out.println("Allocated " + memoryHog.size() + " MB in thread " +
                            Thread.currentThread().getName());
                }
            } catch (OutOfMemoryError e) {
                System.err.println("Caught OutOfMemoryError in thread " +
                        Thread.currentThread().getName() + ": " + e.getMessage());
                // Keep the list alive to analyze in heap dump
                System.out.println("List size at error: " + memoryHog.size() + " MB");
            }
    }

    public static void main(String[] args) {
        Thread memoryHogThread = new Thread(() -> {
            new OutOfMemorySimulator1().simulateOom();
        }, "MemoryHogThread");

        // Start the child thread
        memoryHogThread.start();

        // Keep main thread alive to observe the child thread
        try {
            memoryHogThread.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
    }
}

