package io.github.lucasstarsz.slopeecs.entity;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;

import java.util.BitSet;

public class Entity {
    private final World world;
    private final int id;

    private Entity(World world, Component... components) {
        this.world = world;
        id = world.generateEntityID(components);
    }

    private Entity(World world, int entityID) {
        this.world = world;
        this.id = entityID;
    }

    public World world() {
        return world;
    }

    public int id() {
        return id;
    }

    public BitSet signature() {
        return world.entityManager().getSignature(id);
    }

    public void addComponents(Component... components) {
        world.addComponents(id, components);
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        world.componentManager().removeComponent(id, componentClass);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return world.componentManager().getComponent(id, componentClass);
    }

    public boolean isAlive() {
        return world.entityManager().isAlive(id);
    }

    public static Entity create(World world, Component... components) {
        return new Entity(world, components);
    }

    static Entity create(World world, int entityID) {
        return new Entity(world, entityID);
    }

    public static void destroy(Entity entity) {
        entity.world.entityManager().destroyEntity(entity.id);
    }
}
