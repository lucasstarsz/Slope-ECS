package io.github.lucasstarsz.slopeecs.util;

public class Pair<V1, V2> {
    public final V1 val1;
    public final V2 val2;

    public Pair(V1 v1, V2 v2) {
        val1 = v1;
        val2 = v2;
    }

    public static <V1, V2> Pair<V1, V2> of(V1 v1, V2 v2) {
        return new Pair<>(v1, v2);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "val1=" + val1 +
                ", val2=" + val2 +
                '}';
    }
}
