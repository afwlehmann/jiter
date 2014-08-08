package jiter.enumerator;

import jiter.input.Input;
import jiter.iteratee.Iteratee;

import java.util.Iterator;

public class IterableEnumerator<From> implements Enumerator<From> {

    private final Iterator<From> _iterator;

    private IterableEnumerator(Iterable<From> it) {
        this._iterator = it.iterator();
    }

    @Override
    public <To> Iteratee<From, To> apply(Iteratee<From, To> iter) {
        if (_iterator.hasNext()) {
            Input<From> in = Input.elem(_iterator.next());
            return iter.match(
                dn -> dn,
                ct -> this.apply( ct.apply(in) ),
                er -> er
            );
        }
        return iter;
    }

    public static <T> IterableEnumerator<T> from(Iterable<T> it) {
        return new IterableEnumerator<T>(it);
    }

}
