package jiter.coll;

/**
 * Unit singleton.
 */
public class Unit {

    private static final Unit unit = new Unit();

    Unit() {

    }

    public static Unit unit() {
        return unit;
    }

}
