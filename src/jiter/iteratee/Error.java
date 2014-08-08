package jiter.iteratee;

public final class Error<In, Out> implements Iteratee<In, Out> {

    private final RuntimeException reason;

    public Error(RuntimeException reason) {
        this.reason = reason;
    }

    @Override
    public Out run() {
        throw reason;
    }

}
