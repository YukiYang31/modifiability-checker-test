package demo2;
import org.checkerframework.checker.nullness.qual.*;
import org.jspecify.annotations.NonNull;
import org.checkerframework.checker.modifiability.qual.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList; 
import java.util.Comparator;
import java.util.Map;

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
        return List.of("hello", "world");
    }

    public static void main(String[] args) {
        @Modifiable List<String> modifiableList = new @Modifiable ArrayList<>();
        modifiableList.add("hello");
        modifiableList.add("world");
        System.out.println("Before replaceAll (modifiable): " + modifiableList);
        modifiableList.replaceAll(String::toUpperCase);
        System.out.println("After replaceAll (modifiable): " + modifiableList);

        // Sorting still works on the modifiable list
        modifiableList.sort(Comparator.naturalOrder());
        System.out.println("After sort (modifiable): " + modifiableList);

        // Now an unmodifiable list: calling replaceAll should throw UnsupportedOperationException
        @Unmodifiable List<String> unmodifiable = Collections.unmodifiableList(List.of("hello", "world"));
        System.out.println("Unmodifiable list before replaceAll: " + unmodifiable);
        try {
            unmodifiable.replaceAll(String::toUpperCase); // should throw UnsupportedOperationException
            System.out.println("After replaceAll (unmodifiable): " + unmodifiable);
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException when calling replaceAll on unmodifiable list: " + e);
            e.printStackTrace(System.out);
        }

        try {
            unmodifiable.sort(Comparator.naturalOrder()); // should throw UnsupportedOperationException
            System.out.println("After sort (unmodifiable): " + unmodifiable);
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException when calling sort on unmodifiable list: " + e);
            e.printStackTrace(System.out);
        }

        // Demonstrate Map.Entry modifiability
        Map.Entry<String, String> modEntry = new java.util.AbstractMap.SimpleEntry<>("k", "v");
        Map.Entry<String, String> immEntry1 = Map.entry("k", "v"); //returns a unmodifiable entry
        Map.Entry<String, String> immEntryFromMap = Collections.unmodifiableMap(Map.of("k", "v")).entrySet().iterator().next();

        System.out.println("modEntry before setValue: " + modEntry);
        modEntry.setValue("ok");
        System.out.println("modEntry after setValue: " + modEntry);

        
        try {
            immEntry1.setValue("bad");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException from calling setValue on immEntry1: " + e);
            e.printStackTrace(System.out);
        }

        try {
            // Directly call setValue on an immutable entry extracted from an unmodifiable map
            immEntryFromMap.setValue("bad");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException when calling setValue on entry from unmodifiable map: " + e);
            e.printStackTrace(System.out);
        }

        Collections.swap(modifiableList, 0, 1); // should be OK
        System.out.println("After swap (modifiable): " + modifiableList);

        try {
            Collections.swap(unmodifiable, 0, 1); // throw 
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException when calling swap on unmodifiable list: " + e);
            e.printStackTrace(System.out);
        }

    }
}