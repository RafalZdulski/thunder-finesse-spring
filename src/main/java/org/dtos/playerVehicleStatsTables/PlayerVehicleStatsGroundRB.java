package org.dtos.playerVehicleStatsTables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dtos.VehicleInfo;

import javax.persistence.*;

@Entity
@Table(name = "PLAYER_VEHICLES_STATS_GROUND_RB")
@NoArgsConstructor
@AllArgsConstructor
public class PlayerVehicleStatsGroundRB extends PlayerVehicleStats {
    @Id
    String player_login;
    @Id
    @ManyToOne
    //@JoinTable(name = "VEHICLE_DETAILS")
    VehicleInfo vehicle;
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
