package jiter.iteratee;

import jiter.input.Input;

import java.util.function.Function;

public final class Done<In, Out> implements Iteratee<In, Out> {

    private final Out result;
    private final Input<In> remainingInput;

    public Done(Out result, Input<In> remainingInput) {
        this.result = result;
        this.remainingInput = remainingInput;
    }

    @Override
    public Out run() {
        return result;
    }

    @Override
    public <NewOut> Iteratee<In, NewOut> map(Function<Out, NewOut> f) {
        return new Done<>(f.apply(this.run()), remainingInput);
    }

    @Override
    public <NewOut> Iteratee<In, NewOut> flatMap(Function<Out, Iteratee<In, NewOut>> f) {
        Iteratee<In, NewOut> next = f.apply(this.run());
        return next.match(
          dn -> new Done<In, NewOut>( next.run(), remainingInput ),
          ct -> ct.apply( remainingInput ),
          er -> er
        );
    }

    @Override
    public <T> T match(Function<Done<In, Out>, T> doneFunc,
                       Function<Cont<In, Out>, T> contFunc,
                       Function<Error<In, Out>, T> errorFunc) {
        return doneFunc.apply(this);
    }
}

