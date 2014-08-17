/*
 * Main.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.main;

import jiter.coll.List;
import jiter.enumeratee.Enumeratee;
import jiter.enumeratee.Enumeratees;
import jiter.enumerator.Enumerator;
import jiter.enumerator.Enumerators;
import jiter.iteratee.Iteratee;
import jiter.iteratee.Iteratees;

public class Main {

    public static void main(String[] args) {

        List<Integer> lst = List.nil();
        for (int i = 0; i < 50; i++)
            lst = lst.prepend(i);

        Enumerator<Integer> enumer = Enumerators.enumerate(lst);

        System.out.println(enumer.run(Iteratees.length()));
        System.out.println(enumer.run(Iteratees.sumInt()));
        System.out.println(enumer.run(Iteratees.take(5)));

        Iteratee<Integer, List<Integer>> drop5Take5 =
                Iteratees.<Integer>drop(5).flatMap(x -> Iteratees.<Integer>take(5));

        System.out.println(enumer.run(drop5Take5));

        List<Integer> foo = List.from(28, 29);
        foo = foo.prepend(28).prepend(29).map(x -> 2*x);
        System.out.println(foo);

        System.out.println(foo.extend(foo));

        final jiter.coll.List<Character> bar = List.from('a', 'b', 'c');
        System.out.println(bar.flatMap(x -> bar.map(y -> "[" + x + ',' + y + "]")));

        System.out.println(foo.append(13));

        System.out.println(bar.length());

        Iteratee<Integer, List<Integer>> evenInts = Iteratees.filter(x -> x % 2 == 0);
        System.out.println(enumer.run(evenInts));

        System.out.println(lst.foldLeft(0, acc -> elt -> acc + elt));
        System.out.println(lst.foldRight(elt -> acc -> acc + elt, 0));

        Enumerator<String> es = Enumerators.enumerate(List.from("1", "2", "3"));
        Enumeratee<String, Integer> er = Enumeratees.map(s -> Integer.valueOf(s));
        Iteratee<Integer, Integer> is = Iteratees.sumInt();
        System.out.println(es.run(er.transform(is)));
        System.out.println(es.through(er).run(is));

    }

}
