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
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

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
        System.out.println("Before any modification (modifiable): " + modifiableList);
        // modifiableList.replaceAll(String::toUpperCase);
        // System.out.println("After replaceAll (modifiable): " + modifiableList);

        // // Sorting still works on the modifiable list
        // modifiableList.sort(Comparator.naturalOrder());
        // System.out.println("After sort (modifiable): " + modifiableList);

        // Now an unmodifiable list: calling replaceAll should throw UnsupportedOperationException
        // @Unmodifiable List<String> unmodifiableList = Collections.unmodifiableList(List.of("hello", "world"));
        @Unmodifiable List<String> unmodifiableList = List.of("hello", "world");

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

        
        // mapEntryDemo();

        // testingSwap(modifiableList, unmodifiableList); 

        // modifiableDequeDemo();

        // queueDemo();

        testLinkedTransferQueueDrainToUOE();
        testLinkedTransferQueueDrainToMaxUOE();
    }

    public static void mapEntryDemo() {
        // Demonstrate Map.Entry modifiability===================================================================================
        System.out.println("\n--- Map.Entry Modifiability Demo ---");
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
    }

    public static void testingSwap(List<String> modifiableList, List<String> unmodifiableList) {
        Collections.swap(modifiableList, 0, 1); // should be OK
        System.out.println("After swap (modifiable): " + modifiableList);

        try {
            Collections.swap(unmodifiableList, 0, 1); // throw 
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException when calling swap on unmodifiable list: " + e);
            e.printStackTrace(System.out);
        }
    }

    // Demonstrate behavior of unmodifiable Deque with asLifoQueue
    public static void modifiableDequeDemo() {
        System.out.println("\n--- Modifiable Deque Demo ---");
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
            // java.util.Collections.UnmodifiableCollection cannot be cast to class java.util.Deque. 
            System.out.println("Caught ClassCastException when casting unmodifiableCollection to Deque: " + e);
            e.printStackTrace(System.out);
        }
    }


    public static void queueDemo() {
        System.out.println("\n--- Queue Demo ---");

        // 1. Queue (LinkedList)
        Queue<String> queue = new LinkedList<>();
        queue.offer("First");
        queue.offer("Second");
        System.out.println("Queue (LinkedList): " + queue);

        // 2. Deque (ArrayDeque)
        Deque<String> deque = new ArrayDeque<>();
        deque.offerFirst("Head");
        deque.offerLast("Tail");
        System.out.println("Deque (ArrayDeque): " + deque);

        // 3. BlockingQueue (ArrayBlockingQueue)
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.offer("Block1");
        System.out.println("BlockingQueue (ArrayBlockingQueue): " + blockingQueue);

        // 4. TransferQueue (LinkedTransferQueue)
        TransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        transferQueue.offer("Transfer1");
        System.out.println("TransferQueue (LinkedTransferQueue): " + transferQueue);

        // 5. BlockingDeque (LinkedBlockingDeque)
        BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();
        blockingDeque.offerFirst("BDequeHead");
        System.out.println("BlockingDeque (LinkedBlockingDeque): " + blockingDeque);

        // 6. ArrayQueue (using ArrayDeque as representative)
        // Note: Java does not have a class specifically named "ArrayQueue", ArrayDeque is the standard array-based queue.
        Queue<String> arrayQueue = new ArrayDeque<>();
        arrayQueue.offer("ArrayQ1");
        System.out.println("ArrayQueue (ArrayDeque): " + arrayQueue);

        // 7. PriorityQueue
        Queue<String> priorityQueue = new PriorityQueue<>();
        priorityQueue.offer("Banana");
        priorityQueue.offer("Apple"); 
        System.out.println("PriorityQueue: " + priorityQueue); 

        System.out.println("\n--- UnsupportedOperationException Cases ---");
        
        // Case 1: Modifying an unmodifiable Collection view of a Queue
        // Modification to unmodifiable views throws UnsupportedOperationException
        try {
            Collection<String> unmodifiableQueue = Collections.unmodifiableCollection(queue);
            System.out.println("Attempting to add to unmodifiable queue view...");
            unmodifiableQueue.add("This will fail");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (unmodifiableCollection.add): " + e);
        }

        // Case 2: Iterator remove on Unmodifiable Collection
        try {
            Collection<String> unmodifiableQueue = Collections.unmodifiableCollection(queue);
            Iterator<String> it = unmodifiableQueue.iterator();
            if(it.hasNext()) {
                 System.out.println("Attempting to remove from unmodifiable queue iterator...");
                 it.next();
                 it.remove();
            }
        } catch (UnsupportedOperationException e) {
             System.out.println("Caught Expected UnsupportedOperationException (iterator.remove): " + e);
        }
    }

    public static void testLinkedTransferQueueDrainToUOE() {
        System.out.println("\nTesting LinkedTransferQueue.drainTo with unmodifiable collection...");
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
        queue.add("element");
        
        Collection<String> unmodifiableSink = Collections.unmodifiableList(new ArrayList<>());
        
        try {
            queue.drainTo(unmodifiableSink);
            System.out.println("Failed: drainTo did not throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            System.out.println("Passed: drainTo threw UnsupportedOperationException");
        }
    }

    public static void testLinkedTransferQueueDrainToMaxUOE() {
        System.out.println("\nTesting LinkedTransferQueue.drainTo(max) with unmodifiable collection...");
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
        queue.add("element");
        
        Collection<String> unmodifiableSink = Collections.unmodifiableList(new ArrayList<>());
        
        try {
            queue.drainTo(unmodifiableSink, 10);
            System.out.println("Failed: drainTo(max) did not throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            System.out.println("Passed: drainTo(max) threw UnsupportedOperationException");
        }
    }
}