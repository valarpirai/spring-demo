import java.util.ArrayList;
import java.util.List;

public class OutOfMemorySimulator {
    List<Object> memoryHog = new ArrayList<>();
    void simulateOom() {
        try {
            while (true) {
                // Add large objects to the list
                memoryHog.add(new byte[1024 * 1024]); // Allocates 1MB per object
                System.out.println("Allocated " + memoryHog.size() + " MB");
            }
        } catch (OutOfMemoryError e) {
            System.err.println("Caught OutOfMemoryError: " + e.getMessage());
            // Keep the list alive to analyze in heap dump
            System.out.println("List size at error: " + memoryHog.size() + " MB");
        }
    }

    public static void main(String[] args) {
        new OutOfMemorySimulator().simulateOom();
    }
}

