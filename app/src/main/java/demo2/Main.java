// to run without Checker Framework
// ./gradlew run  -PskipCheckerFramework

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

        // mapSetViewDemo();

        // testingSwap(modifiableList, unmodifiableList); 

        // modifiableDequeDemo();

        // queueDemo();

        // testLinkedTransferQueueDrainToUOE();
        // testLinkedTransferQueueDrainToMaxUOE();
        
        // weakHashMapDemo();
        // identityHashMapDemo();
        // concurrentHashMapDemo();

        testUnmodifiableCastEscape();
    }

    public static void testUnmodifiableCastEscape() {
        System.out.println("\n=== testUnmodifiableCastEscape ===");
        ArrayList<String> myList = new ArrayList<>();
        myList.add("hello");
    
        // Checker correctly infers @Unmodifiable here.
        @Unmodifiable List<String> view = Collections.unmodifiableList(myList);
    
        // Because ArrayList is treated as @Modifiable by the checker, this downcast is considered
        // "safe" — no cast.unsafe warning is issued, even though the underlying object is an
        // unmodifiable wrapper and will throw UnsupportedOperationException (or ClassCastException)
        // at runtime.
        try {
            ArrayList<String> backToMod = (ArrayList<String>) view;
        
            // The checker allows this because backToMod is typed as @Modifiable ArrayList.
            // At runtime, this throws UnsupportedOperationException.
            backToMod.add("Boom");
        } catch (Exception e) {
            System.out.println("Caught exception in testUnmodifiableCastEscape: " + e);
            e.printStackTrace(System.out);
        }
    }


    public static void mapSetViewDemo() {
        System.out.println("\n=== mapSetViewDemo ===");
        // Modifiable Map
        Map<String, String> modMap = new java.util.HashMap<>();
        modMap.put("k1", "v1");
        modMap.put("k2", "v2");
        System.out.println("Modifiable map: " + modMap);

        Set<String> keys = modMap.keySet();
        System.out.println("Calling keySet() on modifiable map.");
        try {
            keys.remove("k1");
            System.out.println("Successfully removed 'k1' from keySet. Map is now: " + modMap);
        } catch (UnsupportedOperationException e) {
            System.out.println("Failed to remove from keySet: " + e);
        }

        // Unmodifiable Map
        Map<String, String> unmodMap = Collections.unmodifiableMap(modMap);
        System.out.println("Unmodifiable map: " + unmodMap);
        
        Set<String> unmodKeys = unmodMap.keySet();
        System.out.println("Calling keySet() on unmodifiable map.");
        try {
            unmodKeys.remove("k2");
            System.out.println("Successfully removed 'k2' from unmodifiable keySet (Unexpected)");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught expected UnsupportedOperationException removing from unmodifiable keySet");
        }
    }


    public static <K,V> Map.Entry<K,V> pickFirst(Map.Entry<K,V> a, Map.Entry<K,V> b) {
        return a; // only reads, no setValue
    }

    // Existing methods...
    public static void concurrentHashMapDemo() {
        System.out.println("\n--- ConcurrentHashMap Demo ---");
        java.util.concurrent.ConcurrentHashMap<String, String> chm = new java.util.concurrent.ConcurrentHashMap<>();
        chm.put("one", "1");
        chm.put("two", "2");

        // The JavaDoc says: "Bulk operations on Map.Entry objects do not support method setValue."
        // These bulk operations include forEach, search, reduce.
        
        System.out.println("Testing forEachEntry(parallelismThreshold, action)...");
        try {
            // parallelismThreshold = Long.MAX_VALUE effectively runs it sequentially, but it uses the Bulk Operation logic.
            chm.forEachEntry(Long.MAX_VALUE, entry -> {
                // System.out.println("Entry passed to action: " + entry);
                try {
                    entry.setValue("modified");
                } catch (UnsupportedOperationException e) {
                   System.out.println("Caught Expected UnsupportedOperationException inside forEachEntry for key " + entry.getKey() + ": " + e);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception caught during bulk operation loop: " + e);
        }

        System.out.println("Testing searchEntries(parallelismThreshold, searchFunction)...");
        try {
            String result = chm.searchEntries(Long.MAX_VALUE, entry -> {
                try {
                    entry.setValue("modifiedBySearch");
                } catch (UnsupportedOperationException e) {
                    System.out.println("Caught Expected UnsupportedOperationException inside searchEntries for key " + entry.getKey() + ": " + e);
                }
                return null; // Return null to continue searching
            });
        } catch (Exception e) {
            System.out.println("Exception caught during searchEntries: " + e);
        }

        System.out.println("Testing reduceEntries(parallelismThreshold, transformer, reducer)...");
        try {
            chm.reduceEntries(Long.MAX_VALUE, 
                entry -> {
                    try {
                        entry.setValue("modifiedByReduce");
                    } catch (UnsupportedOperationException e) {
                        System.out.println("Caught Expected UnsupportedOperationException inside reduceEntries (transformer) for key " + entry.getKey() + ": " + e);
                    }
                    return entry; 
                },
                (e1, e2) -> e1 // Dummy reducer
            );
        } catch (Exception e) {
            System.out.println("Exception caught during reduceEntries: " + e);
        }

        System.out.println("Testing reduceEntriesToDouble(parallelismThreshold, transformer, basis, reducer)...");
        try {
            chm.reduceEntriesToDouble(Long.MAX_VALUE,
                entry -> {
                    try {
                        entry.setValue("modifiedByReduceToDouble");
                    } catch (UnsupportedOperationException e) {
                        System.out.println("Caught Expected UnsupportedOperationException inside reduceEntriesToDouble for key " + entry.getKey() + ": " + e);
                    }
                    return 0.0;
                },
                0.0,
                Double::sum
            );
        } catch (Exception e) {
             System.out.println("Exception caught during reduceEntriesToDouble: " + e);
        }

        System.out.println("Testing reduceEntriesToInt(parallelismThreshold, transformer, basis, reducer)...");
        try {
            chm.reduceEntriesToInt(Long.MAX_VALUE,
                entry -> {
                    try {
                        entry.setValue("modifiedByReduceToInt");
                    } catch (UnsupportedOperationException e) {
                        System.out.println("Caught Expected UnsupportedOperationException inside reduceEntriesToInt for key " + entry.getKey() + ": " + e);
                    }
                    return 0;
                },
                0,
                Integer::sum
            );
        } catch (Exception e) {
             System.out.println("Exception caught during reduceEntriesToInt: " + e);
        }

        System.out.println("Testing reduceEntriesToLong(parallelismThreshold, transformer, basis, reducer)...");
        try {
            chm.reduceEntriesToLong(Long.MAX_VALUE,
                entry -> {
                    try {
                        entry.setValue("modifiedByReduceToLong");
                    } catch (UnsupportedOperationException e) {
                        System.out.println("Caught Expected UnsupportedOperationException inside reduceEntriesToLong for key " + entry.getKey() + ": " + e);
                    }
                    return 0L;
                },
                0L,
                Long::sum
            );
        } catch (Exception e) {
             System.out.println("Exception caught during reduceEntriesToLong: " + e);
        }

        System.out.println("\n--- Invariance Demo ---");
        java.util.concurrent.ConcurrentHashMap<String,String> m = new java.util.concurrent.ConcurrentHashMap<>();
        m.put("k1","v1");
        m.put("k2","v2");
        
        // To verify it compiles/runs in this environment:
        Map.Entry<String,String> ent = m.reduceEntries(1L, Main::pickFirst); 
        System.out.println("ReduceEntries(1L, Main::pickFirst) usage compiled and ran." + ent);
        

        // Contrast this with standard Iterator
        System.out.println("Testing standard iterator on entrySet()...");
        Iterator<Map.Entry<String, String>> it = chm.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            try {
                entry.setValue("modifiedByIterator");
                System.out.println("Successfully modified entry via standard iterator: " + entry);
            } catch (UnsupportedOperationException e) {
                System.out.println("Caught Unexpected UnsupportedOperationException (iterator): " + e);
            }
        }

        // Test Spliterator via Stream
        System.out.println("Testing setValue on entry obtained via stream (Spliterator)...");
        try {
             if (!chm.isEmpty()) {
                 Map.Entry<String, String> entry = chm.entrySet().stream().findFirst().get();
                 entry.setValue("modifiedByStream");
                 System.out.println("Successfully modified entry via stream: " + entry);
             }
        } catch (UnsupportedOperationException e) {
             System.out.println("Caught Expected UnsupportedOperationException (entry from stream/spliterator setValue): " + e);
        }
    }

    public static void identityHashMapDemo() {
        System.out.println("\n--- IdentityHashMap Demo ---");
        java.util.IdentityHashMap<String, String> identityMap = new java.util.IdentityHashMap<>();
        String k1 = new String("key");
        String k2 = new String("key"); 
        identityMap.put(k1, "value1");
        identityMap.put(k2, "value2");
        System.out.println("IdentityHashMap created. Size: " + identityMap.size()); 

        // 1. IdentityHashMap itself is mutable
        identityMap.remove(k1);
        System.out.println("IdentityHashMap after remove(k1): " + identityMap);

        // Test optional remove(key, value)
        // Note: IdentityHashMap compares values by reference equality too. 
        // Since we used string literals "value2", they are interned and refer to the same object.
        boolean removed = identityMap.remove(k2, "value2"); 
        System.out.println("remove(k2, \"value2\") returned: " + removed);
        if (removed) {
             System.out.println("Verified: remove(key, value) is supported.");
        } else {
             System.out.println("Failed to remove(key, value) - possibly reference mismatch or UOE?");
        }
        
        System.out.println("IdentityHashMap after operations: " + identityMap);

        // 2. Views (keySet, values, entrySet) usually throw UOE on add operations
        try {
            System.out.println("Attempting to add to keySet of IdentityHashMap...");
            identityMap.keySet().add("newKey"); 
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (keySet.add): " + e);
        }

        try {
            System.out.println("Attempting to add to values of IdentityHashMap...");
            identityMap.values().add("newValue");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (values.add): " + e);
        }

         try {
            System.out.println("Attempting to add to entrySet of IdentityHashMap...");
            identityMap.entrySet().add(new java.util.AbstractMap.SimpleEntry<>("newKey", "newValue"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (entrySet.add): " + e);
        }

        // Refill map for iterator tests
        identityMap.put("k3", "v3");

        // 3. EntrySet Spliterator returns SimpleImmutableEntry (unlike Iterator which returns mutable Entry)
        try {
             System.out.println("Attempting to setValue on entry obtained via stream (Spliterator)...");
             if (!identityMap.isEmpty()) {
                 Map.Entry<String, String> entry = identityMap.entrySet().stream().findFirst().get();
                 entry.setValue("modified");
             }
        } catch (UnsupportedOperationException e) {
             System.out.println("Caught Expected UnsupportedOperationException (entry from stream/spliterator setValue): " + e);
        }
        
        // 4. EntrySet Iterator returns mutable Entry
        try {
             System.out.println("Attempting to setValue on entry obtained via iterator...");
             Iterator<Map.Entry<String, String>> it = identityMap.entrySet().iterator();
             if (it.hasNext()) {
                 Map.Entry<String, String> entry = it.next();
                 entry.setValue("modified");
                 System.out.println("Successfully modified entry from iterator: " + entry);
             }
        } catch (UnsupportedOperationException e) {
             System.out.println("Caught Unexpected UnsupportedOperationException (entry from iterator setValue): " + e);
        }
    }

    public static void weakHashMapDemo() {
        System.out.println("\n--- WeakHashMap Demo ---");
        java.util.WeakHashMap<String, String> weakMap = new java.util.WeakHashMap<>();
        weakMap.put("key", "value");
        System.out.println("WeakHashMap created and populated: " + weakMap);

        // 1. WeakHashMap itself is mutable
        weakMap.put("key2", "value2");
        System.out.println("WeakHashMap after put: " + weakMap);

        // 2. Views (keySet, values, entrySet) usually throw UOE on add operations for Maps
        try {
            System.out.println("Attempting to add to keySet of WeakHashMap...");
            weakMap.keySet().add("newKey"); 
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (keySet.add): " + e);
        }

        try {
            System.out.println("Attempting to add to values of WeakHashMap...");
            weakMap.values().add("newValue");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (values.add): " + e);
        }

         try {
            System.out.println("Attempting to add to entrySet of WeakHashMap...");
            weakMap.entrySet().add(new java.util.AbstractMap.SimpleEntry<>("newKey", "newValue"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught Expected UnsupportedOperationException (entrySet.add): " + e);
        }

        // 3. Wrapped with Collections.unmodifiableMap
        Map<String, String> unmodifiableWeakMap = Collections.unmodifiableMap(weakMap);
        try {
            System.out.println("Attempting to put into unmodifiable wrapper of WeakHashMap...");
            unmodifiableWeakMap.put("fail", "fail");
        } catch (UnsupportedOperationException e) {
             System.out.println("Caught Expected UnsupportedOperationException (unmodifiableMap.put): " + e);
        }
    }

    public static void mapEntryDemo() {
        // Demonstrate Map.Entry modifiability===================================================================================
        System.out.println("\n--- Map.Entry Modifiability Demo ---");
        Map.Entry<String, String> modEntry = new java.util.AbstractMap.SimpleEntry<>("k", "v");
        Map.Entry<String, String> immEntry1 = Map.entry("k", "v"); //returns a unmodifiable entry
        Map.Entry<String, String> immEntryFromMap = Collections.unmodifiableMap(Map.of("k", "v")).entrySet().iterator().next();
        Map.Entry<String, String> simpleImmutableEntry = new java.util.AbstractMap.SimpleImmutableEntry<>("k", "v");


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

        // Test AbstractMap.SimpleImmutableEntry
        try {
            simpleImmutableEntry.setValue("bad");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught UnsupportedOperationException from calling setValue on SimpleImmutableEntry: " + e);
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