package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // ===== Main Program =====
    public static void main(String[] args) throws Exception {
        int threadCount = 2;

        Threads.TaskQueue queue = new Threads.TaskQueue();
        Threads.ResultStore store = new Threads.ResultStore();
        List<Thread> workers = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new Threads.Worker(queue, store));
            t.start();
            workers.add(t);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter numbers to process or type 'exit' to quit, 'results' to get calculated results. ");

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) break;
            if (line.equalsIgnoreCase("results")) {
                System.out.println("Results:");
                for (String r : store.getAll()) {
                    System.out.println(r);
                }
                continue;
            }
            try {
                int n = Integer.parseInt(line);
                queue.submit(n);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }

        queue.shutdown(); // Sets flag and wakes up waiting threads
        for (Thread t : workers) {
            t.join(); // Sets flag and wakes up waiting threads
        }
        for (String r : store.getAll()) {
            System.out.println(r);
        }

    }
}
