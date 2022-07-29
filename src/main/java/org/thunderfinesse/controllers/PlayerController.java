package org.thunderfinesse.controllers;

import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;
import org.springframework.web.bind.annotation.*;
import org.thunderfinesse.services.PlayerService;
import org.thunderfinesse.services.PlayerServiceImpl;
import org.thunderskill.NoSuchPlayerException;

import java.util.List;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerController {
    private PlayerService playerService = new PlayerServiceImpl();

    @GetMapping("/{login}")
    public String getPlayerStats(@PathVariable("login") String login){
        return login;
    }

    @GetMapping("/{login}/vehicles")
    public List<PlayerVehicleStats> getPlayersVehiclesStats(@PathVariable("login") String login, @RequestParam("mode") String mode){
        try {
            return switch (mode.toLowerCase()) {
                case "air_ab" -> playerService.getPlayerVehiclesStats(login, Modes.ARCADE, VehicleType.Aircraft);
                case "air_rb" -> playerService.getPlayerVehiclesStats(login, Modes.REALISTIC, VehicleType.Aircraft);
                case "air_sb" -> playerService.getPlayerVehiclesStats(login, Modes.SIMULATION, VehicleType.Aircraft);
                case "ground_ab" -> playerService.getPlayerVehiclesStats(login, Modes.ARCADE, VehicleType.GroundVehicle);
                case "ground_rb" -> playerService.getPlayerVehiclesStats(login, Modes.REALISTIC, VehicleType.GroundVehicle);
                case "ground_sb" -> playerService.getPlayerVehiclesStats(login, Modes.SIMULATION, VehicleType.GroundVehicle);
                default -> null;
            };
        }catch (NoSuchPlayerException e){
            return null;
        }
    }

    @GetMapping("/{login}/graphs")
    public String getPlayerModesStats(@PathVariable("login") String login, @RequestParam("mode") String mode){
        return mode;
    }

    @PostMapping("/{login}")
    public boolean updatePlayer(@PathVariable("login") String login){
        return false;
    }

}
