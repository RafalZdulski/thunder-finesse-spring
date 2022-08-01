package org.thunderskill;

import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;

public interface ThunderSkill{

    void update(String login);

    List<PlayerVehicleStats> getWithUpdate(String login, Modes mode, VehicleType type) throws NoSuchPlayerException;
}
