package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;
import io.github.lucasstarsz.slopeecs.util.Defaults;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


public class SystemManager {

    private final World world;

    private final Map<String, Integer> systemTypes;
    private final Deque<Integer> systemTypeQueue;
    private int nextSystemType;

    private final Map<Integer, ECSSystem> systems;
    private final Map<Integer, SystemMetadata> dataMappings;

    public SystemManager(World world) {
        this.world = world;

        systemTypes = new HashMap<>();
        systemTypeQueue = new ArrayDeque<>(Defaults.initialSystemCount);
        for (int i = 0; i < Defaults.initialSystemCount; i++) {
            systemTypeQueue.push(nextSystemType++);
        }

        systems = new LinkedHashMap<>();
        dataMappings = new LinkedHashMap<>();
    }

    public <T extends ECSSystem> T addSystem(Class<T> systemClass) {
        T system = instantiateSystem(systemClass, null);
        return createSystemSignature(system);
    }

    public <T extends ECSSystem> T addSystem(Class<T> systemClass, List<?> systemArgs) {
        T system = instantiateSystem(systemClass, systemArgs);
        return createSystemSignature(system);
    }

    public ECSSystem[] addSystems(Map<Class<? extends ECSSystem>, List<?>> systemsMap) {
        ECSSystem[] generatedSystems = new ECSSystem[systemsMap.size()];

        int i = 0;
        for (var systemPair : systemsMap.entrySet()) {
            Class<? extends ECSSystem> systemClass = systemPair.getKey();
            List<?> systemArgs = systemPair.getValue();

            ECSSystem system = instantiateSystem(systemClass, systemArgs);
            int systemType = systemTypes.get(systemClass.getTypeName());

            systems.put(systemType, system);
            generatedSystems[i] = system;
        }

        return createSystemSignatures(generatedSystems);
    }

    public <T extends ECSSystem> void removeSystem(Class<T> systemClass) {
        String systemType = systemClass.getTypeName();
        if (!isRegistered(systemClass)) {
            throw new IllegalStateException("A system with the class " + systemType + " has not been registered.");
        }

        int systemTypeInt = systemTypes.get(systemType);
        systems.remove(systemTypeInt);
        systemTypes.remove(systemType);
        dataMappings.remove(systemTypeInt);

        // recycle the system type, in case we have use for it later
        systemTypeQueue.push(systemTypeInt);
    }

    @SafeVarargs
    public final <T extends ECSSystem> void removeSystems(Class<T>... systemClasses) {
        for (Class<T> systemClass : systemClasses) {
            removeSystem(systemClass);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ECSSystem> T getSystem(Class<T> systemClass) {
        String systemType = systemClass.getTypeName();
        if (!isRegistered(systemClass)) {
            throw new IllegalStateException("A system with the class " + systemType + " has not been registered.");
        }

        return (T) systems.get(systemTypes.get(systemType));
    }

    public <T extends ECSSystem> boolean isRegistered(Class<T> systemClass) {
        return systemTypes.containsKey(systemClass.getTypeName());
    }

    public <T extends ECSSystem> SystemMetadata getSystemMetadata(Class<T> systemClass) {
        String systemType = systemClass.getTypeName();
        if (!isRegistered(systemClass)) {
            throw new IllegalStateException("A system with the class " + systemType + " has not been registered.");
        }

        return dataMappings.get(systemTypes.get(systemType));
    }

    public void entityChanged(int entity, BitSet signature) {
        if (dataMappings.size() > 1000) {
            dataMappings.values().parallelStream()
                    .forEach(systemMetadata -> systemMetadata.entityChanged(entity, signature));
        } else {
            for (SystemMetadata metadata : dataMappings.values()) {
                metadata.entityChanged(entity, signature);
            }
        }
    }

    public void entityDestroyed(int entity) {
        destroyEntitiesInMetadata(entity);
    }

    public void entitiesDestroyed(int... entities) {
        destroyEntitiesInMetadata(entities);
    }

    public <T extends ECSSystem> void runSystem(Class<T> systemClass) {
        String systemType = systemClass.getTypeName();
        if (!isRegistered(systemClass)) {
            throw new IllegalStateException("A system with the class " + systemType + " has not been registered.");
        }

        int systemTypeInt = systemTypes.get(systemType);
        systems.get(systemTypeInt).update(dataMappings.get(systemTypeInt).entities());
    }

    public void runSystems() {
        for (ECSSystem system : systems.values()) {
            system.update(system.data().entities());
        }
    }

    private <T extends ECSSystem> T instantiateSystem(Class<T> systemClass, List<?> systemArgs) {
        registerSystemType(systemClass);

        Integer systemType = systemTypes.get(systemClass.getTypeName());
        T system;

        try {
            if (systemArgs == null || systemArgs.size() == 0) {
                // create system using the default constructor
                system = systemClass.getDeclaredConstructor().newInstance();
            } else {
                // create system using user-defined constructor
                Class<?>[] params = new Class<?>[systemArgs.size()];
                for (int i = 0; i < params.length; i++) {
                    params[i] = systemArgs.get(i).getClass();
                }

                system = systemClass.getDeclaredConstructor(params).newInstance(systemArgs);
            }

            system.world = this.world;
            systems.put(systemType, system);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            // TODO: proper handling of exceptions instead of throwing e
            throw new IllegalStateException("Something went awry while instantiating a system.", e);
        }

        return system;
    }

    private <T extends ECSSystem> void registerSystemType(Class<T> systemClass) {
        String systemType = systemClass.getTypeName();
        if (isRegistered(systemClass)) {
            throw new IllegalStateException("A system with the class " + systemType + " has already been registered.");
        }

        systemTypes.put(systemType, systemTypeQueue.pop());
        if (systemTypeQueue.size() == 0) {
            systemTypeQueue.push(nextSystemType++);
        }
    }

    private <T extends ECSSystem> T createSystemSignature(T system) {
        Set<Class<? extends Component>> components = system.getComponentsList();
        BitSet systemSignature = new BitSet(components.size());

        for (var component : components) {
            // ensure the component class is registered before adding the signature
            if (!world.componentManager().isComponentRegistered(component)) {
                world.componentManager().registerComponent(component);
            }

            systemSignature.set(world.componentManager().getComponentType(component), true);
        }

        int systemTypeInt = systemTypes.get(system.getClass().getTypeName());
        dataMappings.put(systemTypeInt, new SystemMetadata(systemSignature));

        return system;
    }

    private ECSSystem[] createSystemSignatures(ECSSystem... systems) {
        for (ECSSystem system : systems) {
            Set<Class<? extends Component>> components = system.getComponentsList();
            BitSet systemSignature = new BitSet(components.size());

            for (var component : components) {
                // ensure the component class is registered before adding the signature
                if (!world.componentManager().isComponentRegistered(component)) {
                    world.componentManager().registerComponent(component);
                }

                systemSignature.set(world.componentManager().getComponentType(component), true);
            }

            int systemTypeInt = systemTypes.get(system.getClass().getTypeName());
            dataMappings.put(systemTypeInt, new SystemMetadata(systemSignature));
        }

        return systems;
    }

    private void destroyEntitiesInMetadata(int... entities) {
        if (entities.length > 1000) {
            // convert to set for use of Set#removeAll
            Set<Integer> entitiesSet = Arrays.stream(entities)
                    .parallel().boxed()
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            if (dataMappings.size() > 1000) {
                dataMappings.values().parallelStream()
                        .forEach(systemMetadata -> systemMetadata.entitiesDestroyed(entitiesSet));
            } else {
                for (SystemMetadata metadata : dataMappings.values()) {
                    metadata.entitiesDestroyed(entitiesSet);
                }
            }
        } else {
            for (int entity : entities) {
                if (dataMappings.size() > 1000) {
                    dataMappings.values().parallelStream()
                            .forEach(systemMetadata -> systemMetadata.entityDestroyed(entity));
                } else {
                    for (SystemMetadata metadata : dataMappings.values()) {
                        metadata.entityDestroyed(entity);
                    }
                }
            }
        }
    }

    public int getSystemCount() {
        return systems.size();
    }
}
