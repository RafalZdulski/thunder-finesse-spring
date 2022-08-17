package org.dtos;

import lombok.Getter;
import lombok.Setter;

public class PlayerStatsResponse {
    @Setter @Getter
    private String login;
    @Setter @Getter
    private VehicleModeStats air_ab;
    @Setter @Getter
    private VehicleModeStats ground_ab;
    @Setter @Getter
    private VehicleModeStats air_rb;
    @Setter @Getter
    private VehicleModeStats ground_rb;
    @Setter @Getter
    private VehicleModeStats air_sb;
    @Setter @Getter
    private VehicleModeStats ground_sb;

    public PlayerStatsResponse(String login) {
        this.login = login;
    }
}
