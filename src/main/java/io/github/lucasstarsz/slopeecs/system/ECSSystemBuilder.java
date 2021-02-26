package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.IComponent;

import java.util.BitSet;
import java.util.LinkedHashMap;

/**
 * An {@link ECSSystem} builder, providing an easy way to create ECS systems.
 * <p>
 * <h3>About</h3>
 * Before this class, developers were required to handle the signature (as a {@link BitSet}) of the system themselves,
 * requiring more work as well as an understanding of bits, bitwise operations, and of using the {@code BitSet} class,
 * like so:
 * <pre>{@code
 * // Assume TransformComponent, VelocityComponent, and TransformSystem are all classes
 * // where TransformComponent and VelocityComponent implement IComponent, and
 * // TransformSystem extends ECSSystem.
 * //
 * // Also assume our World variable is called world, and it has all specified components
 * // already registered.
 *
 * TransformSystem transformSystem = world.registerSystem(TransformSystem.class);
 *
 * BitSet transformSystemSignature = new BitSet();
 * transformSystemSignature.set(world.getComponentType(TransformComponent.class));
 * transformSystemSignature.set(world.getComponentType(VelocityComponent.class));
 * world.setSystemSignature(TransformSystem.class, transformSystemSignature);
 *
 * // we can now use transformSystem
 * // hopefully we didn't forget anything...
 * }</pre>
 * <p>
 * This was too much of a hassle for a developer to deal with, unless they're willing to get into the nitty-gritty of
 * the ECS. Furthermore, it raises the entry barrier for newer developers who lack experience with bits and bit
 * operations.
 * <p>
 * As a result, this builder class was developed, allowing the user a more readable interface for creating a system. The
 * result:
 * <pre>{@code
 * // assume the same as last time
 *
 * TransformSystem transformSystem = new ECSSystemBuilder<>(world, TransformSystem.class)
 *         .withComponent(TransformComponent.class)
 *         .withComponent(VelocityComponent.class)
 *         .build();
 *
 * // we can now use transformSystem, woo!
 * }</pre>
 * For more information on how to create systems, check the
 * <a href="https://github.com/lucasstarsz/Slope-ECS/wiki/Systems" target="_blank">wiki page for systems</a>.
 *
 * @param <T> The generic type of the system to create. Uses of {@code T} must extend {@code ECSSystem}.
 * @author Andrew Dey
 */
public class ECSSystemBuilder<T extends ECSSystem> {

    /** The ECS world this builder will create a system for. */
    private final World world;

    /** The class of the system to create. */
    private final Class<T> systemClass;
    /** The signature of the system, used to store component bits. */
    private final BitSet systemSignature;

    /** Arguments to construct the system with, if necessary. */
    private final LinkedHashMap<Class<?>, Object> arguments;
    /** The system to create and eventually return. */
    private T system;

    /**
     * Constructs an ECSSystemBuilder with the specified {@link World} and the class of the system to create.
     * <p>
     * <h3>About</h3>
     * This constructs a builder that will, on command, build an {@link ECSSystem} that is the type of whatever system
     * class is specified. The method assumes that you are using a no-args constructor to instantiate the system. And as
     * such, it calls {@link #ECSSystemBuilder(World, Class, LinkedHashMap)}, where the last parameter is null.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // assume class SomeSystem exists
     * ECSSystemBuilder<SomeSystem> someSystemBuilder = new ECSSystemBuilder<>(world, SomeSystem.class);
     * }</pre>
     *
     * @param world       The ecs world to create a system for.
     * @param systemClass The class of the system to create.
     * @author Andrew Dey
     */
    public ECSSystemBuilder(World world, Class<T> systemClass) {
        this(world, systemClass, null);
    }

    /**
     * Constructs an ECSSystemBuilder with the specified ecs world and the class of the system to create, as well as
     * arguments to instantiate the system if needed.
     * <p>
     * <h3>About</h3>
     * For general information about the method, see {@link #ECSSystemBuilder(World, Class)}. For information about the
     * {@code arguments} parameter, see {@link ECSSystemManager#registerSystem(Class, LinkedHashMap)}.
     * <p>
     * <h3>Example Usages</h3>
     * <pre>{@code
     * // Assume classes SomeSystem and CustomClass exist.
     * // Assume the SomeSystem class has a constructor that takes a String, int, and CustomClass instance.
     *
     * LinkedHashMap arguments = new LinkedHashMap<>() {{
     *     put(String.class, "parameter 1");
     *     put(int.class, 42);
     *     put(CustomClass.class, new CustomClass());
     * }};
     *
     * ECSSystemBuilder<SomeSystem> someSystemBuilder = new ECSSystemBuilder<>(world, SomeSystem.class, arguments);
     * }</pre>
     *
     * @param world       The ecs world to create a system for.
     * @param systemClass The class of the system to create.
     * @param arguments   The arguments to construct the system.
     * @author Andrew Dey
     */
    public ECSSystemBuilder(World world, Class<T> systemClass, LinkedHashMap<Class<?>, Object> arguments) {
        this.world = world;
        this.systemSignature = new BitSet();
        this.systemClass = systemClass;
        this.arguments = arguments;

        construct();
    }

    /** Instantiates the system by registering it in the {@link #world}. */
    private void construct() {
        system = world.registerSystem(systemClass, arguments);
    }

    /**
     * Adds a possible component to the system.
     * <p>
     * <h3>About</h3>
     * Each component added in this way decides what entities are allowed in the resulting system. All entities in a
     * given system are guaranteed to have the components added in this way.
     * <p>
     * <h3>Example Usages</h3>
     * Without method chaining
     * <pre>{@code
     * // assume classes SomeSystem, SomeComponent exist
     * ECSSystemBuilder<SomeSystem> someSystemBuilder = new ECSSystemBuilder<>(world, SomeSystem.class);
     *
     * someSystemBuilder = someSystemBuilder.withComponent(SomeComponent.class);
     * }</pre>
     * With method chaining
     * <pre>{@code
     * // assume classes SomeSystem, SomeComponent exist
     * ECSSystemBuilder<SomeSystem> someSystemBuilder = new ECSSystemBuilder<>(world, SomeSystem.class)
     *              .withComponent(SomeComponent.class);
     * }</pre>
     *
     * @param componentClass The class of the component to add.
     * @param <U>            The generic type of the component to register. Uses of {@code U} must implement {@code
     *                       IComponent}.
     * @return The builder, in order to chain method calls.
     * @author Andrew Dey
     */
    public <U extends IComponent> ECSSystemBuilder<T> withComponent(Class<U> componentClass) {
        if (system == null) {
            throw new IllegalStateException(
                    "Cannot add components until the system has been constructed by calling \"construct()\"."
            );
        }

        systemSignature.set(world.getComponentType(componentClass), true);
        return this;
    }

    /**
     * Finalizes the creation and signature of the system, returning it for use.
     * <p>
     * <h3>About</h3>
     * After creating the builder with the system we want to create, and adding the required components, we use this
     * method to finalize the creation of the system and its signature. Note that you don't need to add a component in
     * order to build the result system -- systems with no components in their signature are just as valid!
     * <p>
     * The resulting system is ready for use -- its signature is already set based on the other parts implemented.
     * <p>
     * <h3>Example Usages</h3>
     * Without method chaining
     * <pre>{@code
     * // assume classes SomeSystem, SomeComponent exist
     * ECSSystemBuilder<SomeSystem> someSystemBuilder = new ECSSystemBuilder<>(world, SomeSystem.class);
     * someSystemBuilder = someSystemBuilder.withComponent(SomeComponent.class);
     *
     * SomeSystem someSystem = someSystemBuilder.build();
     * }</pre>
     * With method chaining
     * <pre>{@code
     * // assume classes SomeSystem, SomeComponent exist
     * SomeSystem someSystem = new ECSSystemBuilder<>(world, SomeSystem.class)
     *              .withComponent(SomeComponent.class)
     *              .build();
     * }</pre>
     *
     * @return The resulting {@link ECSSystem}.
     * @author Andrew Dey
     */
    public T build() {
        world.setSystemSignature(systemClass, systemSignature);
        return system;
    }
}
