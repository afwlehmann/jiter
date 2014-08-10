package jiter.fun;

import jiter.coll.List;

public class Monoids {

    public static final Monoid<Integer> IntegerMonoid = new Monoid<Integer>() {
        @Override
        public Integer mzero() {
            return 0;
        }

        @Override
        public Integer mappend(Integer a, Integer b) {
            return a + b;
        }
    };

    public static <T> Monoid<List<T>> ListMonoid() {
        return new Monoid<List<T>>() {
            @Override
            public List<T> mzero() {
                return List.nil();
            }

            @Override
            public List<T> mappend(List<T> a, List<T> b) {
                return a.extend(b);
            }
        };
    }

    public static final Monoid<String> StringMonoid = new Monoid<String>() {
        @Override
        public String mzero() {
            return "";
        }

        @Override
        public String mappend(String a, String b) {
            return a + b;
        }
    };

}
