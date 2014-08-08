package jiter.iteratee;

import jiter.input.Input;

import java.util.function.Function;

public final class Cont<In, Out> implements Iteratee<In, Out>,
                                            Function<Input<In>, Iteratee<In, Out>>
{

    private Function<Input<In>, Iteratee<In, Out>> k;

    public Cont(Function<Input<In>, Iteratee<In, Out>> k) {
        this.k = k;
    }

    public Iteratee<In, Out> apply(Input<In> in) {
        return k.apply(in);
    }

    @Override
    public Out run() {
        Iteratee<? super In, ? extends Out> next = k.apply(Input.<In>eof());

        return next.match(
                dn -> dn.run(),
                ct -> { throw new RuntimeException("Divergent iteratee"); },
                er -> er.run()
        );
    }

    @Override
    public <NewOut> Iteratee<In, NewOut> map(Function<Out, NewOut> f) {
        return new Cont<>(x -> k.apply(x).map(f));
    }

    @Override
    public <NewOut> Iteratee<In, NewOut> flatMap(Function<Out, Iteratee<In, NewOut>> f) {
        return new Cont<>(x -> k.apply(x).flatMap(f));
    }

    @Override
    public <T> T match(Function<Done<In, Out>, T> doneFunc,
                       Function<Cont<In, Out>, T> contFunc,
                       Function<Error<In, Out>, T> errorFunc) {
        return contFunc.apply(this);
    }
}
