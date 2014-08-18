/*
 * IterateeUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter;/*
 * jiter.IterateeUnitTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

import jiter.input.Input;
import jiter.iteratee.Cont;
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

    @Test
    public void doneConstructorArguments() {
        try {
            done(null, Input.eof());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("result must not be null.", e.getMessage());
        }

        try {
            done(1, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("remainingInput must not be null.", e.getMessage());
        }
    }

    @Test
    public void mapDone() {
        Iteratee<Integer, Integer> d = done(13, Input.elem(3)).map(x -> x * 2);
        assertEquals(d, done(26, Input.elem(3)));
    }

    @Test
    public void flatMapDone() {
        Iteratee<Integer, Integer> d;

        // done -> done
        d = done(13, Input.elem(3)).flatMap(x -> done(x * 2, Input.elem(7)));
        assertEquals(d, done(26, Input.elem(3)));

        // done -> cont
        d = done(13, Input.elem(3)).flatMap(x -> cont((Input<Integer> y) -> y.match(
            _elt -> done(x * _elt, Input.empty()),
            () -> { fail("got Empty"); return null; },
            () -> { fail("got Error"); return null; }
        )));
        assertEquals(d, done(39, Input.empty()));

        // done -> error
        try {
            d = done(13, Input.elem(3)).flatMap(x -> error(new RuntimeException("foo")));
            d.run();
            fail();
        } catch (RuntimeException e) {
            assertEquals("foo", e.getMessage());
        }
    }

    @Test
    public void mapError() {
        Iteratee<Integer, Integer> e = error(new RuntimeException("bar"));
        assertEquals(e.map(x -> 2 * x), e);
    }

    @Test
    public void flatMapError() {
        Iteratee<Integer, Integer> e = error(new RuntimeException("bar"));

        assertEquals(e.flatMap(x -> done(13, Input.eof())), e);
        assertEquals(e.flatMap(x -> cont(y -> y.match(_elt -> null, () -> null, () -> null))), e);
        assertEquals(e.flatMap(x -> error(new RuntimeException("asdf"))), e);
    }

    @Test
    public void mapCont() {
        Iteratee<Integer, Integer> c = cont(x -> x.match(
                _elt -> { fail(); return null; },
                ()   -> { fail(); return null; },
                ()   -> done(2, Input.elem(42))
        ));

        Cont<Integer, Integer> fmc = (Cont<Integer, Integer>) c.map(x -> 3 * x);
        assertEquals(done(6, Input.elem(42)), fmc.apply(Input.eof()));
    }

    @Test
    public void flatMapCont() {
        Iteratee<Integer, Integer> c = cont(x -> x.match(
            _elt -> { fail(); return null; },
            ()   -> { fail(); return null; },
            ()   -> done(2, Input.elem(42))
        ));

        Cont<Integer, Integer> fmc =
                (Cont<Integer, Integer>) c.flatMap(x -> done(3 * x, Input.elem(21)));
        assertEquals(done(6, Input.elem(42)), fmc.apply(Input.eof()));
    }

}
