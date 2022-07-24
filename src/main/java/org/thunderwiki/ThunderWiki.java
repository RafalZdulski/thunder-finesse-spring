package org.thunderwiki;

import org.enums.Nation;
import org.enums.VehicleType;

public interface ThunderWiki {

    void update(Nation nation, VehicleType type);

    void update(Nation nation);

    void update(VehicleType type);

    void updateAll();
}
