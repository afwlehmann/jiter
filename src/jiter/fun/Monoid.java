/*
 * Monoid.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.fun;

public interface Monoid<T> {

    T mzero();

    T mappend(T a, T b);

}
