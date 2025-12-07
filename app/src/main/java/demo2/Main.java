package demo2;
import org.checkerframework.checker.nullness.qual.*;
import org.jspecify.annotations.NonNull;
import org.checkerframework.checker.modifiability.qual.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList; 
import java.util.Comparator;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Deque;
import java.util.ArrayDeque;

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
        // modifiableList.replaceAll(String::toUpperCase);
        // System.out.println("After replaceAll (modifiable): " + modifiableList);

        // // Sorting still works on the modifiable list
        // modifiableList.sort(Comparator.naturalOrder());
        // System.out.println("After sort (modifiable): " + modifiableList);

        // Now an unmodifiable list: calling replaceAll should throw UnsupportedOperationException
        @Unmodifiable List<String> unmodifiableList = Collections.unmodifiableList(List.of("hello", "world"));
        System.out.println("Unmodifiable list created from unmodList" + unmodifiableList);
        // try {
        //     unmodifiableList.replaceAll(String::toUpperCase); // should throw UnsupportedOperationException
        //     System.out.println("After replaceAll (unmodifiable): " + unmodifiableList);
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling replaceAll on unmodifiable list: " + e);
        //     e.printStackTrace(System.out);
        // }

        // try {
        //     unmodifiableList.sort(Comparator.naturalOrder()); // should throw UnsupportedOperationException
        //     System.out.println("After sort (unmodifiable): " + unmodifiableList);
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling sort on unmodifiable list: " + e);
        //     e.printStackTrace(System.out);
        // }

        @Unmodifiable List<String> unmod2 = Collections.unmodifiableList(modifiableList);
        System.out.println("Unmodifiable list created from modifiableList: " + unmod2);

        // try{
        //     Iterator<String> it = unmod2.iterator();
        //     it.remove(); // should throw UnsupportedOperationException
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling remove on iterator of unmodifiable list: " + e);
        //     e.printStackTrace(System.out);
        // }

        // // Test behavior of EmptySet (which is unmodifiable)
        // @Unmodifiable Set<String> emptySet = Collections.emptySet();
        // System.out.println("EmptySet before remove: " + emptySet);

        // boolean changed = emptySet.remove("anything");
        // System.out.println("EmptySet.remove returned: " + changed); // returns false, no exception. 
        
        // // Test behavior of EmptyList (which is unmodifiable)
        // @Unmodifiable List<String> emptyList = Collections.emptyList();
        // System.out.println("EmptyList before remove: " + emptyList);

        // boolean changedList = emptyList.remove("anything");
        // System.out.println("EmptyList.remove returned: " + changedList); // returns false, no exception. 

        // // Test behavior of singleton Set and singleton List (both unmodifiable)
        // @Unmodifiable Set<String> singletonSet = Collections.singleton("only");
        // System.out.println("SingletonSet before remove: " + singletonSet);

        // try {
        //     singletonSet.remove("only"); // should throw UnsupportedOperationException
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling remove on singleton set: " + e);
        //     e.printStackTrace(System.out);
        // }

        // @Unmodifiable List<String> singletonList = Collections.singletonList("only");
        // System.out.println("SingletonList before remove: " + singletonList);

        // try {
        //     singletonList.remove("only"); // should throw UnsupportedOperationException
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling remove on singleton list: " + e);
        //     e.printStackTrace(System.out);
        // }

        // Demonstrate Map.Entry modifiability===================================================================================
        // Map.Entry<String, String> modEntry = new java.util.AbstractMap.SimpleEntry<>("k", "v");
        // Map.Entry<String, String> immEntry1 = Map.entry("k", "v"); //returns a unmodifiable entry
        // Map.Entry<String, String> immEntryFromMap = Collections.unmodifiableMap(Map.of("k", "v")).entrySet().iterator().next();

        // System.out.println("modEntry before setValue: " + modEntry);
        // modEntry.setValue("ok");
        // System.out.println("modEntry after setValue: " + modEntry);

        
        // try {
        //     immEntry1.setValue("bad");
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException from calling setValue on immEntry1: " + e);
        //     e.printStackTrace(System.out);
        // }

        // try {
        //     // Directly call setValue on an immutable entry extracted from an unmodifiable map
        //     immEntryFromMap.setValue("bad");
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling setValue on entry from unmodifiable map: " + e);
        //     e.printStackTrace(System.out);
        // }

        // Collections.swap(modifiableList, 0, 1); // should be OK
        // System.out.println("After swap (modifiable): " + modifiableList);

        // try {
        //     Collections.swap(unmodifiableList, 0, 1); // throw 
        // } catch (UnsupportedOperationException e) {
        //     System.out.println("Caught UnsupportedOperationException when calling swap on unmodifiable list: " + e);
        //     e.printStackTrace(System.out);
        // }

        // Demonstrate behavior of unmodifiable Deque with asLifoQueue
        Deque<String> modDeque = new ArrayDeque<>();
        modDeque.add("one");
        modDeque.add("two");
        System.out.println("Modifiable deque before asLifoQueue operations: " + modDeque);

        // asLifoQueue on a modifiable deque
        java.util.Queue<String> lifoFromMod = Collections.asLifoQueue(modDeque);
        lifoFromMod.add("three");
        System.out.println("Deque after pushing via LIFO queue (modifiable): " + modDeque);

        // Now wrap the same deque in an unmodifiable view and pass that to asLifoQueue
        try {
            @Unmodifiable Deque<String> unmodDeque = (Deque<String>) Collections.unmodifiableCollection(modDeque);
        } catch (ClassCastException e) {
            System.out.println("Caught ClassCastException when casting unmodifiableCollection to Deque: " + e);
            e.printStackTrace(System.out);
        }
    }
}