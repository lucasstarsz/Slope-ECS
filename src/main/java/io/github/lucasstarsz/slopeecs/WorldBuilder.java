package io.github.lucasstarsz.slopeecs;

import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.system.ECSSystem;
import io.github.lucasstarsz.slopeecs.util.Pair;

import java.util.*;

public class WorldBuilder {
    private final Map<Class<? extends ECSSystem>, List<?>> systemsMapping;
    private final List<Pair<Integer, List<? extends Component>>> entityGenPairs;

    WorldBuilder() {
        systemsMapping = new LinkedHashMap<>();
        entityGenPairs = new ArrayList<>();
    }

    public WorldBuilder withSystems(Map<Class<? extends ECSSystem>, List<?>> systemsWithArgs) {
        systemsMapping.putAll(systemsWithArgs);
        return this;
    }

    @SafeVarargs
    public final WorldBuilder withSystems(Class<? extends ECSSystem>... systems) {
        for (Class<? extends ECSSystem> system : systems) {
            systemsMapping.put(system, null);
        }
        return this;
    }

    public WorldBuilder withSystems(List<Class<? extends ECSSystem>> systems) {
        systems.forEach(system -> systemsMapping.put(system, null));
        return this;
    }

    public WorldBuilder withEntities(int entityCount, List<? extends Component> components) {
        entityGenPairs.add(Pair.of(entityCount, components));
        return this;
    }

    public WorldBuilder withEntities(int entityCount, Component... components) {
        entityGenPairs.add(Pair.of(entityCount, Arrays.asList(components.clone())));
        return this;
    }

    public WorldBuilder setSystems(Map<Class<? extends ECSSystem>, List<?>> systemsWithArgs) {
        systemsMapping.clear();
        return withSystems(systemsWithArgs);
    }

    public WorldBuilder setSystems(List<Class<? extends ECSSystem>> systems) {
        systemsMapping.clear();
        return withSystems(systems);
    }

    @SafeVarargs
    public final WorldBuilder setSystems(Class<? extends ECSSystem>... systems) {
        systemsMapping.clear();
        return withSystems(systems);
    }

    public WorldBuilder setEntities(int entityCount, List<? extends Component> components) {
        entityGenPairs.clear();
        return withEntities(entityCount, components);
    }

    public WorldBuilder setEntities(int entityCount, Component... components) {
        entityGenPairs.clear();
        return withEntities(entityCount, components);
    }

    public WorldBuilder reset() {
        systemsMapping.clear();
        entityGenPairs.clear();
        return this;
    }

    public WorldBuilder resetSystems() {
        systemsMapping.clear();
        return this;
    }

    public WorldBuilder resetEntities() {
        entityGenPairs.clear();
        return this;
    }

    public World build() {
        int initialEntityCount = 0;
        for (var pair : entityGenPairs) {
            initialEntityCount += pair.val1;
        }

        World world = new World(initialEntityCount);
        world.addSystems(systemsMapping);

        for (var entityPair : entityGenPairs) {
            world.generateEntityIDs(entityPair.val1, entityPair.val2.toArray(new Component[0]));
        }

        return world;
    }
}
