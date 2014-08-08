package jiter.input;

public class Input<T> {

    private static final EOF<?> _eof = new EOF<>();
    private static final Empty<?> _empty = new Empty<>();

    @SuppressWarnings("unchecked")
    public static <T> EOF<T> eof() {
        return (EOF<T>) _eof;
    }

    @SuppressWarnings("unchecked")
    public static <T> Empty empty() {
        return (Empty<T>) _empty;
    }

    public static <T> Element<T> elem(T t) {
        return new Element<T>(t);
    }

}
