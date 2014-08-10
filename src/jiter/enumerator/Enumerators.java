package jiter.enumerator;

import jiter.coll.List;
import jiter.input.Input;
import jiter.iteratee.Iteratee;

public class Enumerators {

    public static <T> Enumerator<T> enumerate(List<T> xs) {
        return new Enumerator<T>() {
            <To> Iteratee<T, To> auxApply(List<T> xs, Iteratee<T, To> iter) {
                return xs.match(
                        _cons -> iter.match(
                                _done -> _done,
                                _cont -> auxApply(_cons.tail(), _cont.apply(Input.elem(_cons.head()))),
                                _err  -> _err
                        ),
                        () -> iter
                );
            }

            @Override
            public <To> Iteratee<T, To> apply(Iteratee<T, To> iter) {
                return auxApply(xs, iter);
            }
        };
    }

}
