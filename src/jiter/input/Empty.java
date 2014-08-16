/*
 * Empty.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.input;

import jiter.fun.Function0;

import java.util.function.Function;

final class Empty<T> extends Input<T> {

    Empty() {
        /* Intentionally blank */
    }

    @Override
    public <S> S match(Function<T, S> elemFunc, Function0<S> emptyFunc, Function0<S> eofFunc) {
        return emptyFunc.apply();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }

}
