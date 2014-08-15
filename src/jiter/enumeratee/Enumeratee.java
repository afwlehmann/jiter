/*
 * Enumeratee.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.enumeratee;

import jiter.input.Input;
import jiter.iteratee.Iteratee;

import static jiter.iteratee.Iteratees.done;
import static jiter.iteratee.Iteratees.error;

public abstract class Enumeratee<From, To> {

    public abstract <TT> Iteratee<From, Iteratee<To, TT>> apply(Iteratee<To, TT> iter);

    public <TT> Iteratee<From, TT> transform(Iteratee<To, TT> iter) {
        return flatten(apply(iter));
    }

    protected <TT> Iteratee<From, TT> flatten(Iteratee<From, Iteratee<To, TT>> iter) {
        return iter.flatMap( r -> r.match(
               _done -> done( _done.run(), Input.empty() ),
               _cont -> _cont.apply(Input.eof()).match(
                   _innerDone  -> done( _innerDone.run(), Input.eof() ),
                   _innerCont  -> error( new RuntimeException("Diverging inner iteratee!") ),
                   _innerError -> error( _innerError )
               ),
               _error -> error( _error )
            )
        );
    }

}
