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
    @Getter
    String name;
    @Getter
    String type;
    @Getter
    String status;
    @Getter
    String klass;
    @Getter
    String nation;
    @Getter
    String rank;
    @Getter
    String arcade_br;
    @Getter
    String realistic_br;
    @Getter
    String simulation_br;
    @Getter
    String picture;

    public VehicleInfo(String vehicle_id){
        this.vehicle_id = vehicle_id;
    }

}
