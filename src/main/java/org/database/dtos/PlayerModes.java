package org.database.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enums.Modes;
import org.enums.VehicleType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "PLAYER_MODES_DETAILS")
public class PlayerModes implements Serializable {
    @Id
    @ManyToOne
    @Setter @Getter
    Player player;
    @Id
    @Setter @Getter
    String mode;
    @Id
    @Setter @Getter
    String vehicle_type;
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

    public PlayerModes(Player player, Modes mode, VehicleType vehicle_type, int battles, int spawns, int deaths, int wins, int defeats, int air_kills, int ground_kills) {
        this.player = player;
        this.mode = mode.toString().toLowerCase();
        this.vehicle_type = vehicle_type.toString();
        this.battles = battles;
        this.spawns = spawns;
        this.deaths = deaths;
        this.wins = wins;
        this.defeats = defeats;
        this.air_kills = air_kills;
        this.ground_kills = ground_kills;
    }
}
