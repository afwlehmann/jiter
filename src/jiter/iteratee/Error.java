/*
 * Error.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.iteratee;

import java.util.function.Function;

public final class Error<In, Out> implements Iteratee<In, Out> {

    private final RuntimeException reason;

    public Error(RuntimeException reason) {
        this.reason = reason;
    }

    @Override
    public Out run() {
        throw reason;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <NewOut> Iteratee<In, NewOut> map(Function<Out, NewOut> f) {
        return (Iteratee<In, NewOut>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <NewOut> Iteratee<In, NewOut> flatMap(Function<Out, Iteratee<In, NewOut>> f) {
        return (Iteratee<In, NewOut>) this;
    }

    @Override
    public <T> T match(Function<Done<In, Out>, T> doneFunc,
                       Function<Cont<In, Out>, T> contFunc,
                       Function<Error<In, Out>, T> errorFunc) {
        return errorFunc.apply(this);
    }

    @SuppressWarnings("unchecked")
    public <NewIn, NewOut> Error<NewIn, NewOut> self() {
        return (Error<NewIn, NewOut>) this;
    }
}
