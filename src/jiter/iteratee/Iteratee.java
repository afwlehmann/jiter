package jiter.iteratee;

import java.util.function.Function;

public interface Iteratee<In, Out> {

    Out run();

    <NewOut> Iteratee<In, NewOut> map(Function<? super Out, NewOut> f);

    <NewOut> Iteratee<In, NewOut> flatMap(Function<? super Out, Iteratee<In, NewOut>> f);

}
