/*
 * List.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.coll;

import jiter.fun.Function0;

import java.util.function.Function;

/**
 * Immutable singly linked list.
 * @param <T>
 */
public abstract class List<T> {

    private static final Nil<?> nil = new Nil<Unit>();

    public abstract T head();

    public abstract List<T> tail();

    public abstract boolean isEmpty();

    public abstract <S> S match(Function<Cons<T>, S> consFun, Function0<S> nilFun);

    @SuppressWarnings("unchecked")
    public static <S> Nil<S> nil() {
        return (Nil<S>) nil;
    }

    public static <S> Cons<S> cons(S head, List<S> tail) {
        return new Cons<S>(head, tail);
    }

    public Cons<T> prepend(T elem) {
        return cons(elem, this);
    }

    public Cons<T> append(T elem) {
        return this.match(
            cs -> cons(cs.head(), cs.tail().append(elem)),
            () -> cons(elem, nil())
        );
    }

    public List<T> extend(List<T> other) {
        return this.match(
            cs -> cs.tail().extend(other).prepend(cs.head()),
            () -> other
        );
    }

    public List<T> remove(T elem) {
        return this.match(
            cs -> cs.head().equals(elem) ? cs.tail() : cons(cs.head(), cs.tail().remove(elem)),
            () -> nil()
        );
    }

    public List<T> reverse() {
        return this.<List<T>>foldLeft(List.nil(), acc -> elt -> acc.prepend(elt));
    }

    public boolean contains(T elem) {
        return this.match(
            cs -> cs.head().equals(elem) ? true : cs.tail().contains(elem),
            () -> false
        );
    }

    public <S> List<S> map(Function<T, S> f) {
        return this.match(
            cs -> cs.tail().map(f).prepend(f.apply(cs.head())),
            () -> nil()
        );
    }

    public <S> List<S> flatMap(Function<T, List<S>> f) {
        return this.match(
            cs -> f.apply(cs.head()).extend(cs.tail().flatMap(f)),
            () -> nil()
        );
    }

    public <S> S foldLeft(S initial, Function<S, Function<T, S>> f) {
        return this.match(
            cs -> cs.tail().foldLeft(f.apply(initial).apply(cs.head()), f),
            () -> initial
        );
    }

    public <S> S foldRight(Function<T, Function<S, S>> f, S initial) {
        return this.match(
            cs -> f.apply(cs.head()).apply(cs.tail().foldRight(f, initial)),
            () -> initial
        );
    }

    @SafeVarargs
    public static <S> List<S> from(S... xs) {
        List<S> result = List.<S>nil();
        for (int i = xs.length-1; i >= 0; i--)
            result = result.prepend(xs[i]);
        return result;
    }

    public int length() {
        return this.match(
            cs -> 1 + cs.tail().length(),
            () -> 0
        );
    }

    @Override
    public String toString() {
        return this.match(
            cs -> cs.head().toString() + cs.tail.match(csPrime -> ", " + cs.tail().toString(), () -> ""),
            () -> ""
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof List))
            return false;

        @SuppressWarnings("unchecked")
        List<T> other = (List<T>) obj;
        Boolean otherContainsAllItemsFromThisList =
                this.foldLeft(true, acc -> elt -> acc & other.contains(elt));

        return otherContainsAllItemsFromThisList && this.length() == other.length();
    }

    @Override
    public int hashCode() {
        return this.foldLeft(37, acc -> elt -> acc * 37 + elt.hashCode());
    }

    public static class Cons<T> extends List<T> {
        private final T       head;
        private final List<T> tail;

        private Cons(T head, List<T> tail) {
            if (head == null)
                throw new IllegalArgumentException("head must not be null.");
            if (tail == null)
                throw new IllegalArgumentException("tail must not be null.");

            this.head = head;
            this.tail = tail;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public <S> S match(Function<Cons<T>, S> consFun, Function0<S> nilFun) {
            return consFun.apply(this);
        }
    }

    public static class Nil<T> extends List<T> {
        private Nil() {
        }

        @Override
        public T head() {
            throw new RuntimeException("Nil has no head.");
        }

        @Override
        public List<T> tail() {
            throw new RuntimeException("Nil has no tail.");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public <S> S match(Function<Cons<T>, S> consFun, Function0<S> nilFun) {
            return nilFun.apply();
        }
    }

}


