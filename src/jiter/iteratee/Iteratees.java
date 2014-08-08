package jiter.iteratee;

import jiter.input.Input;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Iteratees {

    /*
     * Length
     */

    static class Length {
        static <T> Function<Input<T>, Iteratee<T, Integer>> step(final Integer n) { // surprisingly, "int" is sufficient here (since Java 8?).
            return in -> in.match(
                el -> new Cont<>(step(n + 1)),
                () -> new Cont<>(step(n)),
                () -> new Done<>(n, Input.eof())
            );
        }
    }

    public static <T> Iteratee<T, Integer> length() {
        return new Cont<>(Length.step(0));
    }

    /*
     * Sum
     */

    static class Sums {
        static Function<Input<Integer>, Iteratee<Integer, Integer>> step(final Integer acc) {
            return in -> in.match(
                el -> new Cont<>(step(acc + el)),
                () -> new Cont<>(step(acc)),
                () -> new Done<>(acc, Input.eof())
            );
        }
    }

    public static Iteratee<Integer, Integer> sumInt() {
        return new Cont<>(Sums.step(0));
    }

    /*
     * Take & Drop
     */

    static class Take {
        static <T> Function<Input<T>, Iteratee<T, List<T>>> step(final Integer n, final List<T> acc) {
            return in -> in.match(
                el -> {
                    if (n <= 0) {
                        return new Done<>(acc, Input.elem(el));
                    } else {
                        acc.add(el);
                        return new Cont<>(step(n-1, acc));
                    }
                },
                () -> new Cont<>(step(n, acc)),
                () -> new Done<>(acc, Input.eof())
            );
        }

        static <T> Function<Input<T>, Iteratee<T, Void>> drop(final Integer n) {
            return in -> in.match(
                el -> n > 0 ? new Cont<>(drop(n-1)) : new Done<>(null, Input.empty()),
                () -> new Cont<>(drop(n)),
                () -> new Done<>(null, Input.eof())
            );
        }
    }

    public static <T> Iteratee<T, List<T>> take(int n) {
        LinkedList<T> lst = new LinkedList<>();
        return new Cont<>(Take.step(n, lst));
    }

    public static <T> Iteratee<T, Void> drop(int n) {
        return new Cont<>(Take.drop(n));
    }

}
