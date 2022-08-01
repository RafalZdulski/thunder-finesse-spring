package org.thunderfinesse.controllers;

import org.database.postgre.PostgreAccessPoint;
import org.dtos.VehicleStats;
import org.springframework.web.bind.annotation.*;
import org.thunderfinesse.services.VehicleService;
import org.thunderfinesse.services.VehicleServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {
    private VehicleService vehicleService = new VehicleServiceImpl(new PostgreAccessPoint());

    @GetMapping("/{vehicle_id}")
    public List<VehicleStats> getVehicle(@PathVariable("vehicle_id") String vehicle_id){
        return vehicleService.getVehicleStats(vehicle_id);
    }


}