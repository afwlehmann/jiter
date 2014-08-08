package jiter.iteratee;

import java.util.function.Function;
import jiter.input.Input;

public final class Cont<In, Out> implements Iteratee<In, Out>,
                                            Function<In, Iteratee<In, Out>>
{

    private Function<? super Input<In>, ? extends Iteratee<In, Out>> k;

    public Cont(Function<? super Input<In>, ? extends Iteratee<In, Out>> k) {
        this.k = k;
    }

    @Override
    public Iteratee<In, Out> apply(In in) {
        return k.apply(in);
    }

    @Override
    public Out run() {
        Iteratee<In, Out> next = k.apply(Input.<In>eof());

        if (next instanceof Cont) {
            throw new RuntimeException("Divergent iteratee");
        }

        return next.run();
    }
}
