package demo2;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList; 

public class noAnnotation {
    void sample() {
        Object ref = new Object();
        System.out.println(ref);
    }

     // Only accepts modifiable lists
    public static void modify(List<String> list) {
        list.add("new element"); // should be OK only for @Modifiable
    }

    // Returns an unmodifiable list
    public static List<String> unmodifiableSource() {
        return List.of("hello", "world");
    }

    public static void main(String[] args) {
        List<String> modifiableList = new ArrayList<>();
        modifiableList.remove("2");
        modify(modifiableList); // ✅ should be OK

        List<String> unmodList = unmodifiableSource();
        unmodList.remove("2"); //❌ should raise a type error
        modify(unmodList); // ❌ should raise a type error
     }
}