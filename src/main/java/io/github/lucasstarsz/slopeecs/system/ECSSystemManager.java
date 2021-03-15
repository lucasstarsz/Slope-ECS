package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class ECSSystemManager {

    private final World world;
    private final Map<String, BitSet> systemSignatures = new HashMap<>();
    private final Map<String, ECSSystem> systems = new HashMap<>();
    private final Map<String, Set<Integer>> systemEntities = new HashMap<>();

    public ECSSystemManager(World world) {
        this.world = world;
    }

    public ECSSystem[] addSystems(Map<Class<? extends ECSSystem>, List<?>> systemsMap) {
        ECSSystem[] generatedSystems = new ECSSystem[systemsMap.size()];
        int i = 0;

        for (var systemPair : systemsMap.entrySet()) {
            Class<? extends ECSSystem> systemClass = systemPair.getKey();
            List<?> systemArgs = systemPair.getValue();
            String systemType = systemClass.getTypeName();

            if (systems.get(systemType) != null) {
                throw new IllegalStateException("System with class " + systemType + " is already registered.");
            }

            try {
                if (systemArgs == null || systemArgs.size() == 0) {
                    // create system using the default constructor
                    ECSSystem system = systemClass.getDeclaredConstructor().newInstance();

                    systems.put(systemType, system);
                    generatedSystems[i] = system;
                } else {
                    // create system using user-defined constructor
                    Class<?>[] params = new Class<?>[systemArgs.size()];
                    for (int j = 0; j < params.length; j++) {
                        params[j] = systemArgs.get(j).getClass();
                    }

                    ECSSystem system = systemClass.getDeclaredConstructor(params).newInstance(systemArgs);
                    systems.put(systemType, system);
                    generatedSystems[i] = system;
                }

                systemEntities.put(systemType, new LinkedHashSet<>());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        registerSystems(generatedSystems);

        return generatedSystems;
    }

    public <T extends ECSSystem> T addSystem(Class<T> systemClass) {
        return addSystem(systemClass, null);
    }

    public <T extends ECSSystem> T addSystem(Class<T> systemClass, List<?> systemArgs) {
        String systemType = systemClass.getTypeName();

        if (systems.get(systemType) != null) {
            throw new IllegalStateException("System with class " + systemType + " is already registered.");
        }

        T system = null;

        try {
            if (systemArgs == null || systemArgs.size() == 0) {
                // create system using the default constructor
                system = systemClass.getDeclaredConstructor().newInstance();
                systems.put(systemType, system);
            } else {
                // create system using user-defined constructor
                Class<?>[] params = new Class<?>[systemArgs.size()];
                for (int i = 0; i < params.length; i++) {
                    params[i] = systemArgs.get(i).getClass();
                }

                system = systemClass.getDeclaredConstructor(params).newInstance(systemArgs);
                systems.put(systemType, system);
            }

            systemEntities.put(systemType, new LinkedHashSet<>());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        registerSystems(system);

        return system;
    }

    public void registerSystems(ECSSystem... systems) {
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
            systemSignatures.put(system.getClass().getTypeName(), systemSignature);
        }
    }



















    public <T extends ECSSystem> void setSignature(Class<T> signatureClass, BitSet signature) {
        String typeName = signatureClass.getTypeName();

        if (systems.get(typeName) == null) {
            throw new IllegalStateException("System with class " + typeName + " was used before it was registered.");
        }

        // Set the signature for this system
        systemSignatures.put(typeName, signature);
    }

    public void entityDestroyed(int entity) {
        for (Map.Entry<String, Set<Integer>> entry : systemEntities.entrySet()) {
            entry.getValue().remove(entity);
        }
    }

    public void entitySignatureChanged(int entity, BitSet entitySignature) {
        for (Map.Entry<String, Set<Integer>> entry : systemEntities.entrySet()) {
            BitSet systemSignature = systemSignatures.get(entry.getKey());

            /* The BitSet class in java does not produce a new BitSet when doing bitwise operations -- the operations
             * are applied to the BitSet that the method is called on.
             * As such, we need to create a clone to avoid messing up the original BitSet. */
            BitSet entitySignatureClone = (BitSet) entitySignature.clone();
            entitySignatureClone.and(systemSignature);

            if (entitySignatureClone.equals(systemSignature)) {
                // Entity signature contains system signature - insert into set
                entry.getValue().add(entity);
            } else {
                // Entity signature does not contain system signature - erase from set
                entry.getValue().remove(entity);
            }
        }
    }

    public void runSystems(World world) {
        for (Map.Entry<String, ECSSystem> system : systems.entrySet()) {
            system.getValue().update(world, systemEntities.get(system.getKey()));
        }
    }

    public <T extends ECSSystem> void runSystem(World world, Class<T> systemClass) {
        systems.get(systemClass.getTypeName()).update(world, systemEntities.get(systemClass.getTypeName()));
    }


    public <T extends ECSSystem> BitSet getSystemSignature(Class<T> systemClass) {
        return systemSignatures.get(systemClass.getTypeName());
    }

    @SuppressWarnings("unchecked")
    public <T extends ECSSystem> T getSystem(Class<T> systemClass) {
        T system = (T) systems.get(systemClass.getTypeName());

        if (system == null) {
            throw new IllegalStateException("Could not find a system with the class " + systemClass.getTypeName());
        }

        return system;
    }

    public <T extends ECSSystem> Set<Integer> getEntities(Class<T> systemClass) {
        return systemEntities.get(systemClass.getTypeName());
    }

    public int getSystemCount() {
        return systems.values().size();
    }
}
