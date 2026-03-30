package org.example;
import java.util.*;

public class Threads {

    // ===== Shared Task Queue =====
    static class TaskQueue {

        private final LinkedList<Integer> queue = new LinkedList<>();
        private boolean shutdown = false;

        public synchronized void submit(int n) {
            queue.add(n);
            notifyAll(); // wake up waiting worker thread
        }

        public synchronized Integer take() throws InterruptedException {
            // Wait until a task is available or shutdown is called
            while (queue.isEmpty() && !shutdown) {
                wait();
            }
            // If shutdown AND no more tasks → worker should exit
            if (queue.isEmpty() && shutdown) {
                return null;
            }
            return queue.removeFirst();
        }

        public synchronized void shutdown() {
            shutdown = true;
            notifyAll();
        }
    }

    // ===== Shared Result Store =====
    static class ResultStore {
        private final List<String> results = new ArrayList<>();

        public synchronized void add(String s) {
            results.add(s);
        }
        // Returns a *copy* of all results to avoid exposing internal state
        public synchronized List<String> getAll() {
            return new ArrayList<>(results);
        }
    }

    // ===== Prime Checker =====
    static class PrimeChecker {
        public int primeFactorCount(int n) {

            // simulating long computing times
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (n <= 1) return 0;
            int count = 0;

            // distinct count factors of 2
            if (n % 2 == 0) {
                count++; // found distinct prime factor 2
                while (n % 2 == 0) {
                    n /= 2;  // remove all multiples of 2
                }
            }

            // distinct count odd factors
            for (int i = 3; i * i <= n; i += 2) {
                if (n % i == 0) {
                    count++;
                    while (n % i == 0) {
                        n /= i;
                    }
                }
            }
            // if n is still > 1, it's a prime factor
            if (n > 1) count++;
            return count;
        }
    }

    // ===== Worker Thread =====
    static class Worker implements Runnable {
        private final TaskQueue queue;
        private final ResultStore store;

        private final PrimeChecker primeChecker = new PrimeChecker();

        public Worker(TaskQueue q, ResultStore s) {
            this.queue = q;
            this.store = s;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Wait for a task or shutdown
                    Integer task = queue.take();
                    if (task == null) break;

                    int primeCount = primeChecker.primeFactorCount(task);

                    store.add("Number: " + task + ", Prime: " + primeCount);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
