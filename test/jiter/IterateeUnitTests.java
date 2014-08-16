/*
 * IterateeUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter;/*
 * jiter.IterateeUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

import jiter.input.Input;
import jiter.iteratee.Iteratee;
import static jiter.iteratee.Iteratees.done;
import static jiter.iteratee.Iteratees.cont;
import static jiter.iteratee.Iteratees.error;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IterateeUnitTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void doneProducesResult() {
        Iteratee<?, Integer> d = done(13, Input.eof());
        assertEquals(13, (int) d.run());
    }

    @Test
    public void runningDivergingContThrowsException() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Diverging iteratee!");

        Iteratee<?, ?> c = cont(x -> cont(y -> null));
        c.run();
    }

    @Test
    public void runningErrorThrowsException() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Raise hell");

        Iteratee<?, ?> e = error(new RuntimeException("Raise hell"));
        e.run();
    }

}
