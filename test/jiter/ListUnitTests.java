/*
 * ListUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter;

import jiter.coll.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static jiter.matcher.ListMatcher.*;
import static org.junit.Assert.*;

public class ListUnitTests {

    @Test
    public void nilIsEmpty() {
        assertThat(List.nil(), emptyList());
    }

    @Test
    public void listsCreatedWithFrom() {
        assertThat(List.from(), emptyList());

        assertThat(List.from(1,2,3), CoreMatchers.<List<Integer>>allOf(
                contains(1), contains(2), contains(3), hasLength(3)
        ));
    }

    @Test
    public void prependElement() {
        assertEquals(List.from(1), List.nil().prepend(1));
        assertEquals(List.from(1,2), List.from(2).prepend(1));
    }

    @Test
    public void appendElement() {
        assertEquals(List.from(1), List.nil().append(1));
        assertEquals(List.from(1,2), List.from(1).append(2));
    }

    @Test
    public void extendList() {
        List<Integer> el = List.nil();
        List<Integer> nel = List.from(1,2,3);
        assertEquals(el.extend(el), el);
        assertEquals(el.extend(nel), nel);
        assertEquals(nel.extend(el), nel);
        assertEquals(nel.extend(nel), List.from(1,2,3,1,2,3));
    }

    @Test
    public void reverseList() {
        assertEquals(List.nil().reverse(), List.nil());
        assertEquals(List.from(1,2,3).reverse(), List.from(3, 2, 1));
        assertEquals(List.from(1).reverse(), List.from(1));
    }

    @Test
    public void removeElement() {
        assertEquals(List.nil().remove(1), List.nil());
        assertEquals(List.from(1).remove(1), List.nil());
        assertEquals(List.from(1,2,3).remove(2), List.from(1,3));
        assertEquals(List.from(1,2,3).remove(3), List.from(1,2));
    }

    @Test
    public void containsElement() {
        assertFalse(List.nil().contains(1));
        assertTrue(List.from(1).contains(1));
        assertFalse(List.from(1,3,4).contains(2));
        assertTrue(List.from(1,2,3,4).contains(2));
    }

    @Test
    public void lengthIsCorrect() {
        assertEquals(0, List.nil().length());

        List<Integer> lst = List.from(1,2,3,4);
        assertEquals(4, lst.length());
    }

    @Test
    public void mapIsCorrect() {
        assertEquals(List.from(2,4,6,8), List.from(1,2,3,4).map(x -> 2 * x));
    }

    @Test
    public void flatMapIsCorrect() {
        List<String> lst =
            List.from('a', 'b', 'c').flatMap(x ->
                List.from(1, 2).map(y -> "" + x + y));
        assertEquals(List.from("a1", "b1", "c1", "a2", "b2", "c2"), lst);
    }

    @Test
    public void matchIsCorrect() {
        List.nil().match(
            _cons -> { fail(); return false; },
            ()    -> true
        );

        List.from(1,2,3).match(
            _cons -> true,
            ()    -> { fail(); return false; }
        );
    }

}
