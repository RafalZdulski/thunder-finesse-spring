package org.dtos.playerVehicleStatsTables;

import lombok.*;

import java.io.Serializable;

@Data
public abstract class PlayerVehicleStats implements Serializable {
    @Setter @Getter
    int battles;
    @Setter @Getter
    int spawns;
    @Setter @Getter
    int deaths;
    @Setter @Getter
    int wins;
    @Setter @Getter
    int defeats;
    @Setter @Getter
    int air_kills;
    @Setter @Getter
    int ground_kills;
}
