package jiter.iteratee;

import java.util.function.Function;

public interface Iteratee<In, Out> /* implements Function<Input<In>, Iteratee<In, Out>> */ {

    Out run();

    // TODO: Variance

    <NewOut> Iteratee<In, NewOut> map(Function<Out, NewOut> f);

    <NewOut> Iteratee<In, NewOut> flatMap(Function<Out, Iteratee<In, NewOut>> f);

    <T> T match( Function<Done<In, Out>, T> doneFunc,
                 Function<Cont<In, Out>, T> contFunc,
                 Function<Error<In, Out>, T> errorFunc );

}
