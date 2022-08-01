package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VEHICLE_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInfo {
    @Id
    @Setter @Getter
    String vehicle_id;
    @Setter @Getter
    String name;
    @Setter @Getter
    String type;
    @Setter @Getter
    String status;
    @Setter @Getter
    String klass;
    @Setter @Getter
    String nation;
    @Setter @Getter
    String rank;
    @Setter @Getter
    String arcade_br;
    @Setter @Getter
    String realistic_br;
    @Setter @Getter
    String simulation_br;
    @Setter @Getter
    String picture;

    public VehicleInfo(String vehicle_id){
        this.vehicle_id = vehicle_id;
    }

}
