package org.example;
//
import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


class Main {
    public static void main(String[] args) throws Exception {
        // Task 2: Create collection of categories with elements (two-way)
        List<Category> categories = new ArrayList<>();

        Category electronics = new Category("c1", "Electronics");
        Category books = new Category("c2", "Books");
        Category groceries = new Category("c3", "Groceries");

        categories.add(electronics);
        categories.add(books);
        categories.add(groceries);

        // Create some elements using builders; use addElement
        Element e1 = new Element.Builder().id("e1").name("Smartphone").price(699.99).category(electronics).build();
        Element e2 = new Element.Builder().id("e2").name("Laptop").price(1299.99).category(electronics).build();
        Element e3 = new Element.Builder().id("e3").name("Novel").price(19.99).category(books).build();
        Element e4 = new Element.Builder().id("e4").name("Cookbook").price(29.5).category(books).build();
        Element e5 = new Element.Builder().id("e5").name("Milk").price(1.99).category(groceries).build();
        Element e6 = new Element.Builder().id("e6").name("Cheese").price(3.49).category(groceries).build();
        Element e7 = new Element.Builder().id("e7").name("Headphones").price(799.99).category(electronics).build();
        Element e8 = new Element.Builder().id("e8").name("Keyboard").price(599.99).category(electronics).build();
        Element e9 = new Element.Builder().id("e9").name("Witcher").price(15.99).category(books).build();
        Element e10 = new Element.Builder().id("e10").name("Eggs").price(5.99).category(groceries).build();

        // Add elements to categories
        electronics.addElement(e1);
        electronics.addElement(e2);
        electronics.addElement(e7);
        electronics.addElement(e8);
        books.addElement(e3);
        books.addElement(e4);
        books.addElement(e9);
        groceries.addElement(e5);
        groceries.addElement(e6);
        groceries.addElement(e10);

        // Print categories and their elements in original order using nested for-each with lambdas
        System.out.println("== Task 2: Categories and their elements (original order) ==");
        categories.forEach(cat -> {
            System.out.println(cat);                                                                   // will use Category.toString()
            cat.getElements().forEach(el -> System.out.println("  " + el));
        });

        // Task 3: Single Stream pipeline -> Set of all elements from all categories
        Set<Element> allElements = categories.stream()                  // Turns the list of categories into a stream
                .flatMap(cat -> cat.getElements().stream())    // For each category, extract its elements
                .collect(Collectors.toCollection(LinkedHashSet::new)); // collects the elements into a LinkedHashSet (keeps order and removes duplicates).

        System.out.println("\n== Task 3: Set of all elements (collected via single pipeline) ==");
        allElements.stream().forEach(System.out::println);             // print using a second pipeline (per spec)

        //Task 4: Single stream pipeline -> filter by property, sort by another property, print
        System.out.println("\n== Task 4: Filter elements (price > 20) and sort by name ==");
        allElements.stream() // one pipeline
                .filter(el -> el.getPrice() > 20.0)                                             // fiter by price
                .sorted(Comparator.comparing(Element::getName))                                        // sort by different property - name
                .forEach(System.out::println);

        // Task 5: Single pipeline -> transform to DTOs, sort natural order, collect to List
        System.out.println("\n== Task 5: Transform elements -> DTOs, sort (natural), collect to List ==");
        List<ElementDTO> dto = allElements.stream() // single pipeline
                .map(el -> new ElementDTO.Builder()
                        .id(el.getId())
                        .name(el.getName())
                        .price(el.getPrice())
                        .categoryId(el.getCategory() == null ? null : el.getCategory().getId())
                        .build())
                .sorted() // natural order from ElementDTO.compareTo
                .collect(Collectors.toList());

        dto.forEach(System.out::println);

        // Task 6: Serialization: write and read collection of categories
        System.out.println("\n== Task 6: Serialization -> write/read categories to/from binary file ==");
        String filename = "categories.bin";
        // write
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(categories);
            System.out.println("Wrote categories to " + filename);
        }

        // read
        List<Category> readCategories;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            //noinspection unchecked
            readCategories = (List<Category>) obj;
        }
        System.out.println("Read categories back from " + filename);
        readCategories.forEach(cat -> {
            System.out.println(cat);
            cat.getElements().forEach(el -> System.out.println("  " + el));
        });

        // --- Task 7: Parallel Stream API pipelines with custom thread pool and delays ---
        System.out.println("\n== Task 7: Parallel processing of categories with custom ForkJoinPool ==");
        ForkJoinPool pool = new ForkJoinPool(3); // pool sizes
            try {
            pool.submit(() ->
                    categories.parallelStream().forEach(cat -> {
                        System.out.println(Thread.currentThread().getName() + " processing category: " + cat.getName());
                        // Simulate workload by printing its elements one by one with small sleep
                        cat.getElements().forEach(el -> {
                            try {

                                TimeUnit.MILLISECONDS.sleep(3000);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("  [" + Thread.currentThread().getName() + "] " + el);
                        });
                    })
            ).get(); // wait for completion of submitted task
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Pool didn't terminate in time, forcing shutdown.");
                pool.shutdownNow();
            } else {
                System.out.println("Pool terminated normally");
            }
        }
    }
}