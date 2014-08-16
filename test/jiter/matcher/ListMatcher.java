/*
 * ListMatcher.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter.matcher;

import jiter.coll.List;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ListMatcher {

    public static <T> TypeSafeMatcher<List<T>> contains(final T elt) {
        return new TypeSafeMatcher<List<T>>() {
            @Override
            protected boolean matchesSafely(List<T> tList) {
                return tList.contains(elt);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contain " + elt);
            }
        };
    }

    public static <T> TypeSafeMatcher<List<T>> hasLength(final int l) {
        return new TypeSafeMatcher<List<T>>() {
            @Override
            protected boolean matchesSafely(List<T> tList) {
                return tList.length() == l;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("have length " + l);
            }
        };
    }

    public static <T> TypeSafeMatcher<List<T>> containsAnyOf(final List<T> xs) {
        return new TypeSafeMatcher<List<T>>() {
            @Override
            protected boolean matchesSafely(List<T> tList) {
                return xs.foldLeft(false, acc -> elt -> acc | tList.contains(elt));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contain any of " + xs);
            }
        };
    }

    public static <T> TypeSafeMatcher<List<T>> containsAllOf(final List<T> xs) {
        return new TypeSafeMatcher<List<T>>() {
            @Override
            protected boolean matchesSafely(List<T> tList) {
                return xs.foldLeft(true, acc -> elt -> acc & tList.contains(elt));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contain all of " + xs);
            }
        };
    }

    public static <T> TypeSafeMatcher<List<T>> emptyList() {
        return new TypeSafeMatcher<List<T>>() {
            @Override
            protected boolean matchesSafely(List<T> tList) {
                return tList.isEmpty();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("empty");
            }
        };
    }

}
