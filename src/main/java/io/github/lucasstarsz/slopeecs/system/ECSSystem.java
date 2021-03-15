package io.github.lucasstarsz.slopeecs.system;

import io.github.lucasstarsz.slopeecs.World;
import io.github.lucasstarsz.slopeecs.component.Component;

import java.util.Set;

public interface ECSSystem {

    Set<Class<? extends Component>> getComponentsList();

    void update(World world, Set<Integer> entities);
}
