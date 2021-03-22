package io.github.lucasstarsz.slopeecs.system;

import java.util.*;

public class SystemMetadata {
    private final BitSet signature;
    private final Set<Integer> entities;

    public SystemMetadata() {
        signature = new BitSet();
        entities = new LinkedHashSet<>();
    }

    public SystemMetadata(BitSet generatedSignature) {
        signature = generatedSignature;
        entities = new LinkedHashSet<>();
    }

    public void entityChanged(int entity, BitSet entitySignature) {
        /* The BitSet class in java does not produce a new BitSet when doing bitwise operations -- the operations
         * are applied to the BitSet that the method is called on.
         * As such, we need to create a clone to avoid modifying the original BitSet. */
        BitSet entitySignatureClone = (BitSet) entitySignature.clone();
        entitySignatureClone.and(signature);

        if (entitySignatureClone.equals(signature)) {
            entities.add(entity);
        } else {
            entities.remove(entity);
        }
    }

    public void entitiesChanged(Map<Integer, BitSet> entitiesChanged) {
        for (var entity : entitiesChanged.entrySet()) {
            /* The BitSet class in java does not produce a new BitSet when doing bitwise operations -- the operations
             * are applied to the BitSet that the method is called on.
             * As such, we need to create a clone to avoid modifying the original BitSet. */
            BitSet entitySignatureClone = (BitSet) entity.getValue().clone();
            entitySignatureClone.and(signature);

            if (entitySignatureClone.equals(signature)) {
                entities.add(entity.getKey());
            } else {
                entities.remove(entity.getKey());
            }
        }
    }

    public void entityDestroyed(int destroyedEntity) {
        entities.remove(destroyedEntity);
    }

    public void entitiesDestroyed(Set<Integer> destroyedEntities) {
        entities.removeAll(destroyedEntities);
    }

    public Set<Integer> entities() {
        return entities;
    }

    public BitSet signature() {
        return signature;
    }
}
