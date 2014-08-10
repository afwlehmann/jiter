package jiter.fun;

public interface Monoid<T> {

    T mzero();

    T mappend(T a, T b);

}
