/*
 * Enumerator.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.enumerator;

import jiter.iteratee.Iteratee;

public interface Enumerator<From> {

    <To> Iteratee<From, To> apply(Iteratee<From, To> iter);

    default <To> To run(Iteratee<From, To> iter) {
        return this.apply(iter).run();
    }

}
