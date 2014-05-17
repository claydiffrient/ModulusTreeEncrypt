package edu.byui.cs480.modulustree;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by clay on 5/16/14.
 */
public class Utilities {
    /**
     * Taken from:
     *  http://stackoverflow.com/questions/17640754/zipping-streams-using-jdk8-with-lambda-java-util-stream-streams-zip
     *
     * It was originally included in one version of the Streams API, but was removed.  This is a modified version of
     * the original source code for that function.
     */
    public static<A, B, C> Stream<C> zip(Stream<? extends A> a,
                                        Stream<? extends B> b,
                                        BiFunction<? super A, ? super B, ? extends C> zipper) {
        Objects.requireNonNull(zipper);
        @SuppressWarnings("unchecked")
        Spliterator<A> aSpliterator = (Spliterator<A>) Objects.requireNonNull(a).spliterator();
        @SuppressWarnings("unchecked")
        Spliterator<B> bSpliterator = (Spliterator<B>) Objects.requireNonNull(b).spliterator();

        // Zipping looses DISTINCT and SORTED characteristics
        int both = aSpliterator.characteristics() & bSpliterator.characteristics() &
                ~(Spliterator.DISTINCT | Spliterator.SORTED);
        int characteristics = both;

        long zipSize = ((characteristics & Spliterator.SIZED) != 0)
                ? Math.min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
                : -1;

        Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
        Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
        Iterator<C> cIterator = new Iterator<C>() {
            @Override
            public boolean hasNext() {
                return aIterator.hasNext() && bIterator.hasNext();
            }

            @Override
            public C next() {
                return zipper.apply(aIterator.next(), bIterator.next());
            }
        };

        Spliterator<C> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
        return (a.isParallel() || b.isParallel())
                ? StreamSupport.stream(split, true)
                : StreamSupport.stream(split, false);
    }
}
