package jiter.input;

import jiter.fun.Function0;

import java.util.function.Function;

public abstract class Input<T> {

    private static final EOF<?> _eof = new EOF<>();
    private static final Empty<?> _empty = new Empty<>();

    @SuppressWarnings("unchecked")
    public static <T> EOF<T> eof() {
        return (EOF<T>) _eof;
    }

    @SuppressWarnings("unchecked")
    public static <T> Empty<T> empty() {
        return (Empty<T>) _empty;
    }

    public static <T> Element<T> elem(T t) {
        return new Element<>(t);
    }

    public abstract <S> S match(Function<T, S> elemFunc, Function0<S> emptyFunc, Function0<S> eofFunc);

}
