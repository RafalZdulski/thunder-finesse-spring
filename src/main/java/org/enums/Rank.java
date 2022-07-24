package org.enums;


public enum Rank {
    I(1),
    II(2),
    III(3),
    IV(4),
    V(5),
    VI(6),
    VII(7);

    private int value;

    Rank(int value) {
        this.value = value;
    }

    public static Rank getRank(String rank){
        switch (Integer.parseInt(rank)) {
            case 1: return I;
            case 2: return II;
            case 3: return III;
            case 4: return IV;
            case 5: return V;
            case 6: return VI;
            case 7: return VII;
            default: throw new IllegalArgumentException("no such rank");
        }
    }

    public int getValue() {
        return value;
    }
}
