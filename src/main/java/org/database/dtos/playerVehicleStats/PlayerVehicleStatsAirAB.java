package org.database.dtos.playerVehicleStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.database.dtos.Player;
import org.database.dtos.VehicleInfo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYER_VEHICLES_STATS_AIR_AB")
@NoArgsConstructor
@AllArgsConstructor
public class PlayerVehicleStatsAirAB extends PlayerVehicleStats {
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
