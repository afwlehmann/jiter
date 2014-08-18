/*
 * Iteratees.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.iteratee;

import jiter.coll.List;
import jiter.coll.Unit;
import jiter.fun.Monoid;
import jiter.fun.Monoids;
import jiter.input.Input;

import java.util.function.Function;

public class Iteratees {

    public static <In, Out> Done<In, Out> done(Out result, Input<In> remainingInput) {
        return new Done<>(result, remainingInput);
    }

    public static <In, Out> Cont<In, Out> cont(Function<Input<In>, Iteratee<In, Out>> k) {
        return new Cont<>(k);
    }

    public static <In, Out> Error<In, Out> error(RuntimeException reason) {
        return new Error<>(reason);
    }

    public static <In, Out> Error<In, Out> error(Error<?, ?> other) {
        return new Error<>(other);
    }

    /*
     * Length
     */

    static class Length {
        static <T> Function<Input<T>, Iteratee<T, Integer>> step(final Integer n) { // surprisingly, "int" is sufficient here (since Java 8?).
            return in -> in.match(
                el -> cont(step(n + 1)),
                () -> cont(step(n)),
                () -> done(n, Input.eof())
            );
        }
    }

    public static <T> Iteratee<T, Integer> length() {
        return cont(Length.step(0));
    }

    /*
     * Sum
     */

    static class Sums {
        static <T> Function<Input<T>, Iteratee<T, T>> step(Monoid<T> mon, final T acc) {
            return in -> in.match(
                el -> cont(step(mon, mon.mappend(acc, el))),
                () -> cont(step(mon, acc)),
                () -> done(acc, Input.eof())
            );
        }
    }

    public static Iteratee<Integer, Integer> sumInt() {
        return cont(Sums.step(Monoids.IntegerMonoid, 0));
    }

    /*
     * Take
     */

    static class Take {
        static <T> Function<Input<T>, Iteratee<T, List<T>>> step(final Integer n, final List<T> acc) {
            return in -> in.match(
                el -> {
                    if (n <= 0) {
                        return done(acc, Input.elem(el));
                    } else {
                        return cont(step(n - 1, acc.append(el)));
                    }
                },
                () -> cont(step(n, acc)),
                () -> done(acc, Input.eof())
            );
        }
    }

    public static <T> Iteratee<T, List<T>> take(int n) {
        return cont(Take.step(n, List.nil()));
    }

    /*
     * Drop
     */

    static class Drop {
        static <T> Function<Input<T>, Iteratee<T, Unit>> step(final Integer n) {
            return in -> in.match(
                el -> n > 0 ? cont(step(n - 1)) : done(Unit.unit(), Input.empty()),
                () -> cont(step(n)),
                () -> done(Unit.unit(), Input.eof())
            );
        }
    }

    public static <T> Iteratee<T, Unit> drop(int n) {
        return cont(Drop.step(n));
    }

    /*
     * Filter
     */

    static class Filter {
        static <T> Function<Input<T>, Iteratee<T, List<T>>> step(final Function<T, Boolean> predicate, final List<T> acc) {
            return in -> in.match(
                el -> cont(step(predicate, predicate.apply(el) ? acc.append(el) : acc)),
                () -> cont(step(predicate, acc)),
                () -> done(acc, Input.eof())
            );
        }
    }

    public static <T> Iteratee<T, List<T>> filter(Function<T, Boolean> predicate) {
        return cont(Filter.step(predicate, List.nil()));
    }

}
