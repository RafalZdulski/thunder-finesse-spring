package org.thunderfinesse.controllers;

import org.database.dtos.VehicleInfo;
import org.database.postgre.PostgreGameStatsAccessPoint;
import org.database.dtos.VehicleStats;
import org.dtos.VehicleStatsResponse;
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
    public VehicleStatsResponse getVehicleStats(@PathVariable("vehicleId") String vehicleId){
        return vehicleService.getVehicleStats(vehicleId);
    }

    @GetMapping("/stats")
    public List<VehicleStatsResponse> getVehiclesStats(@RequestParam("type") String type){
        return switch (type.toLowerCase()) {
            case "air" -> vehicleService.getVehiclesStats(VehicleType.Aircraft);
            case "ground" -> vehicleService.getVehiclesStats(VehicleType.GroundVehicle);
            default -> null;
        };
    }




}