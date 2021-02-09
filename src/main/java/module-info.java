/**
 * A Java-based Entity Component System written (mostly) for the Valley Game Framework/Engine.
 * <p>
 * This ECS stores entities as integers, with verification of components available to it through {@code BitSet}
 * mappings. Each entity can have
 */
module Slope.ECS {
    exports org.lucasstarsz.slope.entity;
    exports org.lucasstarsz.slope.component;
    exports org.lucasstarsz.slope.system;
}