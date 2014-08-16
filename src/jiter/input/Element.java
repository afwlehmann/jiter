/*
 * Element.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.input;

import jiter.fun.Function0;

import java.util.function.Function;

final class Element<T> extends Input<T> {

    private final T elem;

    Element(T elem) {
        if (elem == null)
            throw new IllegalArgumentException("Only non-null values are allowed!");

        this.elem = elem;
    }

    public T get() {
        return elem;
    }

    @Override
    public <S> S match(Function<T, S> elemFunc, Function0<S> eofFunc, Function0<S> emptyFunc) {
        return elemFunc.apply(elem);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Element))
            return false;

        Element other = (Element) obj;
        return other.elem.equals(elem);
    }

    @Override
    public int hashCode() {
        return elem.hashCode();
    }

    @Override
    public String toString() {
        return "Element(" + elem + ")";
    }

}
