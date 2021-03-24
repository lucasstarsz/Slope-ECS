package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;

import java.util.BitSet;
import java.util.Set;

public abstract class ECSSystem {
    World world;

    public SystemMetadata data() {
        return world.systemManager().getSystemMetadata(this.getClass());
    }

    public Set<Integer> entities() {
        return data().entities();
    }

    public World world() {
        return world;
    }

    public BitSet signature() {
        return data().signature();
    }

    public abstract Set<Class<? extends Component>> getComponentsList();

    public abstract void update(Set<Integer> entities);
}
