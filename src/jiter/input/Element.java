package jiter.input;

final class Element<T> extends Input<T> {

    private final T elem;

    Element(T elem) {
        this.elem = elem;
    }

    public T get() {
        return elem;
    }

}
