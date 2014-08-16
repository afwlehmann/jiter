/*
 * IterateesUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter;/*
 * jiter.IterateesUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

import jiter.coll.List;
import jiter.iteratee.Iteratee;
import jiter.iteratee.Iteratees;
import org.junit.Test;

import static jiter.enumerator.Enumerators.enumerate;
import static jiter.matcher.ListMatcher.containsAnyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

public class IterateesUnitTests {

    private static final List<Integer> emptyList = List.nil();

    private static List<Integer> nonEmptyList = List.nil();
    static {
        for (int i = 0; i < 1000; i++)
            nonEmptyList = nonEmptyList.prepend((int)(Math.random() * 50));
    }

    @Test
    public void lengthOnEmptyList() {
        assertEquals(0, (int) enumerate(emptyList).run(Iteratees.length()));
    }

    @Test
    public void lengthOnNonEmptyList() {
        assertEquals(nonEmptyList.length(), (int) enumerate(nonEmptyList).run(Iteratees.length()));
    }

    @Test
    public void lengthOnNonEmptyListWithChaining() {
        Iteratee<Integer, Integer> mult = enumerate(nonEmptyList).apply(Iteratees.length());
        assertEquals(2 * nonEmptyList.length(), (int) enumerate(nonEmptyList).run(mult));
    }

    @Test
    public void filterOnEmptyList() {
        Iteratee<Integer, List<Integer>> iterT = Iteratees.filter(x -> true);
        assertEquals(List.nil(), enumerate(emptyList).run(iterT));

        Iteratee<Integer, List<Integer>> iterF = Iteratees.filter(x -> false);
        assertEquals(List.nil(), enumerate(emptyList).run(iterF));
    }

    @Test
    public void filterOnNonEmptyList() {
        Iteratee<Integer, List<Integer>> iterT = Iteratees.filter(x -> true);
        assertEquals(nonEmptyList.length(), enumerate(nonEmptyList).run(iterT).length());

        Iteratee<Integer, List<Integer>> iterF = Iteratees.filter(x -> false);
        assertEquals(List.nil(), enumerate(nonEmptyList).run(iterF));

        Iteratee<Integer, List<Integer>> evenIter = Iteratees.filter(x -> x % 2 == 0);
        Iteratee<Integer, List<Integer>> oddIter  = Iteratees.filter(x -> x % 2 == 1);
        List<Integer> evens = enumerate(nonEmptyList).run(evenIter);
        List<Integer> odds  = enumerate(nonEmptyList).run(oddIter);
        assertThat(evens, not(containsAnyOf(odds)));
        assertThat(odds, not(containsAnyOf(evens)));
        assertEquals(nonEmptyList.length(), evens.length() + odds.length());
    }

}
