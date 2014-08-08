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
    public <NewOut> Iteratee<In, NewOut> map(Function<? super Out, NewOut> f) {
        return new Done(f.apply(this.run()), remainingInput);
    }

    @Override
    public <NewOut> Iteratee<In, NewOut> flatMap(Function<? super Out, Iteratee<In, NewOut>> f) {
        Iteratee<In, NewOut> next = f.apply(this.run());
        if (next instanceof Done) {
            return new Done( next.run(), remainingInput );
        } else if (next instanceof Cont) {
            return
        }
    }
}

