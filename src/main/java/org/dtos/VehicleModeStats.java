package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class VehicleModeStats{
    @Setter
    @Getter
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
