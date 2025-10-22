package demo2;
import org.checkerframework.checker.nullness.qual.*;
import org.checkerframework.checker.tainting.qual.*;
import org.checkerframework.checker.modifiability.qual.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList; 
import java.util.Scanner;

public class Main {
    void sample() {
        @NonNull Object ref = new Object();
        System.out.println(ref);
    }

     // Only accepts modifiable lists
    public static void modify(@Modifiable List<String> list) {
        list.add("new element"); // should be OK only for @Modifiable
    }

    // Returns an unmodifiable list
    public static @Unmodifiable List<String> unmodifiableSource() {
        return Collections.unmodifiableList(List.of("hello", "world"));
    }

    public static void main(String[] args) {
        @Modifiable List<String> modifiableList = new @Modifiable ArrayList<>();
        modifiableList.remove("2");
        modify(modifiableList); // ✅ should be OK

        @Unmodifiable List<String> unmodList = unmodifiableSource();
        unmodList.remove("2"); //❌ should raise a type error
        // modify(unmodList); // ❌ should raise a type error


        Scanner scanner = new Scanner(System.in);

        @Tainted String userInput = scanner.nextLine();

        // ❌ This should cause an error: passing tainted input to a sensitive operation
        // sink(userInput);

        // ✅ Safe: we sanitize the input
        @Untainted String cleanInput = sanitize(userInput);
        sink(cleanInput);
     }

    public static void sink(@Untainted String s) {
        System.out.println("Sanitized: " + s);
    }

    public static @Untainted String sanitize(String input) {
        // Very naive sanitizer for demo purposes only
        return (@Untainted String) input.replaceAll("[^a-zA-Z0-9]", "");
    }
}