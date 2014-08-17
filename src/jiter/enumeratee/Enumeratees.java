/*
 * Enumeratees.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.enumeratee;

import jiter.input.Input;
import jiter.iteratee.Iteratee;

import java.util.function.Function;

import static jiter.iteratee.Iteratees.cont;
import static jiter.iteratee.Iteratees.done;

public class Enumeratees {

    public static abstract class CheckDone<From, To> extends Enumeratee<From, To> {
        @Override
        public <TT> Iteratee<From, Iteratee<To, TT>> apply(Iteratee<To, TT> iter) {
            return iter.match(
                _done -> done(_done, Input.empty()),
                _cont -> kontinue(x -> _cont.apply(x)),
                _err  -> done(_err, Input.empty())
            );
        }

        abstract <TT> Iteratee<From, Iteratee<To, TT>> kontinue(Function<Input<To>, Iteratee<To, TT>> k);
    }

    public static <F, T> Enumeratee<F, T> map(Function<F, T> f) {
        return new CheckDone<F, T>() {
            @Override
            <TT> Iteratee<F, Iteratee<T, TT>> kontinue(Function<Input<T>, Iteratee<T, TT>> k) {
                return cont(input -> input.match(
                    _elt -> k.apply(Input.elem(f.apply(_elt))).match(
                        _done -> done(_done, Input.empty()),
                        _cont -> kontinue(k),
                        _err  -> done(_err, Input.empty())
                    ),
                    ()   -> kontinue(k),
                    ()   -> done(k.apply(Input.eof()), Input.eof())
                ));
            }
        };
    }

}
