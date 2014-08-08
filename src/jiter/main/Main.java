package jiter.main;

import java.util.LinkedList;
import java.util.List;

import jiter.enumerator.IterableEnumerator;
import jiter.iteratee.Iteratee;
import jiter.iteratee.Iteratees;

public class Main {

    public static void main(String[] args) {

        LinkedList<Integer> lst = new LinkedList<>();
        for (int i = 0; i < 50; i++)
            lst.add(i);

        IterableEnumerator<Integer> enumer = IterableEnumerator.from(lst);
        Iteratee<Integer, Integer> iter = Iteratees.length();

        //System.out.println(enumer.run(iter));
        //System.out.println(enumer.run(Iteratees.sumInt()));
        //System.out.println(enumer.run(Iteratees.take(5)));

        Iteratee<Integer, List<Integer>> dropFiveTake5 =
                Iteratees.<Integer>drop(5).flatMap(x -> Iteratees.<Integer>take(5));

        System.out.println(enumer.run(dropFiveTake5));
    }

}
