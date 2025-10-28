package com.example.demo.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Islands {

    private final Set<Island> islands;

    private Islands(Collection<Island> islands) {
        this.islands = new HashSet<>(islands);
    }

    public static Islands of(Collection<Island> islands) {
        return new Islands(islands);
    }

    public boolean isEmpty() {
        return islands.isEmpty();
    }

    public Island getIslandWithFewerAvaiableWorkstations() {
        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final int positions = slots;
            var possibleIsland = islands.stream()
                .filter(i -> i.getWorkstations().stream()
                            .map(Workstation::getUser)
                            .filter(Objects::nonNull)
                            .count() == positions)
                .findFirst();
            if (possibleIsland.isPresent()) {
                return possibleIsland.get();
            }
        }
        return islands.iterator().next();
    }

    public Island assignUserToIslandWithFewerWorkstations(User user) {
        return this.getIslandWithFewerAvaiableWorkstations()
            .assignUserToTheFirstWorkstationAvailable(user);
    }
    
}
