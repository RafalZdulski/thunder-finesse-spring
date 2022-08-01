package org.dtos.playerVehicleStatsTables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dtos.Player;
import org.dtos.VehicleInfo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYER_VEHICLES_STATS_AIR_RB")
@NoArgsConstructor
@AllArgsConstructor
public class PlayerVehicleStatsAirRB extends PlayerVehicleStats {
    @Id
    @ManyToOne
    @Setter @Getter
    Player player;
    @Id
    @ManyToOne
    @Setter @Getter
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
