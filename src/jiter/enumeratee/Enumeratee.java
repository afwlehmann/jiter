/*
 * Enumeratee.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.enumeratee;

import jiter.input.Input;
import jiter.iteratee.Done;
import jiter.iteratee.Iteratee;
import jiter.iteratee.Error;

public abstract class Enumeratee<From, To> {

    public abstract <TT> Iteratee<From, Iteratee<To, TT>> apply(Iteratee<To, TT> iter);

    public <TT> Iteratee<From, TT> transform(Iteratee<To, TT> iter) {
        return flatten(apply(iter));
    }

    protected <TT> Iteratee<From, TT> flatten(Iteratee<From, Iteratee<To, TT>> iter) {
        return iter.flatMap( r -> r.match(
               _done -> new Done<>(_done.run(), Input.empty()),
               _cont -> _cont.apply(Input.eof()).match(
                   _innerDone  -> new Done<>(_innerDone.run(), Input.eof()),
                   _innerCont  -> new Error<>(new RuntimeException("Diverging inner iteratee!")),
                   _innerError -> _innerError.<From, TT>self()
               ),
               _error -> _error.<From, TT>self()
            )
        );
    }

}
