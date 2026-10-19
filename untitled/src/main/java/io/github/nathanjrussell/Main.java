package io.github.nathanjrussell;

public class Main {
    public static void main(String[] args) {
        ZMod a = new ZMod(3, 7);
        ZMod b = new ZMod(4, 7);

        System.out.println("Working modulo " + a.getModulus());
        System.out.println("a = " + a.getValue());
        System.out.println("b = " + b.getValue());
        System.out.println("a + b = " + a.add(b).getValue());
        System.out.println("a * b = " + a.multiply(b).getValue());
        System.out.println("negate(a) = " + a.negate().getValue());
        System.out.println("zero = " + a.zero().getValue());
        System.out.println("one = " + a.one().getValue());
        System.out.println("a + negate(a) = " + a.add(a.negate()).getValue());
        System.out.println("a equals b = " + a.equals(b));
        System.out.println("a equals -4 modulo 7 = " + a.equals(new ZMod(-4, 7)));

        // These elements have the same value but belong to different rings.
        try {
            ZMod otherRing = new ZMod(3, 5);
            a.equals(otherRing);
        } catch (IllegalArgumentException exception) {
            System.out.println("Cannot compare different rings. " + exception.getMessage());
        }
    }
}
