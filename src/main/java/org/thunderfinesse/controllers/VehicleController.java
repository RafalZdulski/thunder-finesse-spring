package org.thunderfinesse.controllers;

import org.database.postgre.PostgreGameStatsAccessPoint;
import org.database.postgre.PostgrePlayerStatsAccessPoint;
import org.dtos.VehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;
import org.springframework.web.bind.annotation.*;
import org.thunderfinesse.services.VehicleService;
import org.thunderfinesse.services.VehicleServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {
    private VehicleService vehicleService = new VehicleServiceImpl(new PostgreGameStatsAccessPoint());

    @GetMapping("/{vehicleId}")
    public List<VehicleStats> getVehicleStats(@PathVariable("vehicleId") String vehicleId){
        return vehicleService.getVehicleStats(vehicleId);
    }

    @GetMapping
    public List<VehicleStats> getVehiclesStats(@RequestParam("mode") String mode){
        return switch (mode.toLowerCase()) {
            case "air_ab" -> vehicleService.getVehiclesStats(Modes.ARCADE, VehicleType.Aircraft);
            case "air_rb" -> vehicleService.getVehiclesStats(Modes.REALISTIC, VehicleType.Aircraft);
            case "air_sb" -> vehicleService.getVehiclesStats(Modes.SIMULATION, VehicleType.Aircraft);
            case "ground_ab" -> vehicleService.getVehiclesStats(Modes.ARCADE, VehicleType.GroundVehicle);
            case "ground_rb" -> vehicleService.getVehiclesStats(Modes.REALISTIC, VehicleType.GroundVehicle);
            case "ground_sb" -> vehicleService.getVehiclesStats(Modes.SIMULATION, VehicleType.GroundVehicle);
            default -> null;
        };
    }


}