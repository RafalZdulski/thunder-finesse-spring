package org.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enums.Modes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "VEHICLE_STATS")
public class VehicleStats implements Serializable {
    @Id
    @ManyToOne
    @Setter @Getter
    VehicleInfo vehicle;
    @Id
    @Setter @Getter
    String Mode;
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


    public VehicleStats(VehicleInfo vehicle, Modes mode, int battles, int spawns, int deaths, int wins, int defeats, int air_kills, int ground_kills) {
        this.vehicle = vehicle;
        Mode = mode.toString().toLowerCase();
        this.battles = battles;
        this.spawns = spawns;
        this.deaths = deaths;
        this.wins = wins;
        this.defeats = defeats;
        this.air_kills = air_kills;
        this.ground_kills = ground_kills;
    }
}
