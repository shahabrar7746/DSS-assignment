import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class SetPrac {
    public static void main(String[] args) {

        Set<Integer> hs = new TreeSet<>();
        hs.add(1);
        hs.add(2);
        hs.add(4);
        hs.add(5);
        hs.add(34);
        hs.add(32);
        hs.add(10);
        hs.add(9);
        hs.add(7);
        System.out.println(hs);
        Map<Integer, String> map1 = new HashMap<>();
        map1.put(1, "1");
        Set<Map.Entry<Integer, String>> entries = map1.entrySet();


        System.out.println("Entries: "+ entries);
        Set<Integer> integers = map1.keySet();
        int i = map1.hashCode();
        System.out.println(i);

        System.out.println(hs.remove(10));
        System.out.println(hs);
        System.out.println(hs.contains(10));
        System.out.println(hs.isEmpty());

/*
NOT recommended use ->> ConcurrentSkipListSet instead
        Set<Integer> integers1 = Collections.synchronizedSet(hs);
        System.out.println(integers1);
        System.out.println(integers1.add(10));
        System.out.println(integers1.add(20));
        System.out.println(integers1);

        synchronized (integers1) {
            for (Integer j : integers1) {
                System.out.println(j);
            }
        }

*/
        Set<Integer> set1 = new ConcurrentSkipListSet<>();
        //synchronized

    }
}
