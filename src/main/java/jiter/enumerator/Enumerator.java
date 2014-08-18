/*
 * Enumerator.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.enumerator;

import jiter.enumeratee.Enumeratee;
import jiter.iteratee.Iteratee;

public interface Enumerator<From> {

    <To> Iteratee<From, To> apply(Iteratee<From, To> iter);

    default <To> To run(Iteratee<From, To> iter) {
        return this.apply(iter).run();
    }

    default <To> Enumerator<To> through(Enumeratee<From, To> enumeratee) {
        return new Enumerator<To>() {
            @Override
            public <TT> Iteratee<To, TT> apply(Iteratee<To, TT> iter) {
                return Enumerator.this.apply( enumeratee.apply(iter) ).run();
            }
        };
    }

}
