package io.github.lucasstarsz.slopeecs.entity;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;

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

    public World getWorld() {
        return world;
    }

    public int getID() {
        return id;
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
