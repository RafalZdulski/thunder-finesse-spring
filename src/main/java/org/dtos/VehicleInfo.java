package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VEHICLE_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInfo {
    @Id
    @Getter
    String vehicle_id;
    String name;
    String type;
    String status;
    String klass;
    String nation;
    String rank;
    String arcade_br;
    String realistic_br;
    String simulation_br;
    String picture;

    public VehicleInfo(String vehicle_id){
        this.vehicle_id = vehicle_id;
    }

}
