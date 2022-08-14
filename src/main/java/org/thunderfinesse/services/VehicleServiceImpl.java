package org.thunderfinesse.services;

import org.database.GameStatsAccessPoint;
import org.database.PlayerStatsAccessPoint;
import org.database.WikiAccessPoint;
import org.database.postgre.PostgreGameStatsAccessPoint;
import org.database.postgre.PostgrePlayerStatsAccessPoint;
import org.database.postgre.PostgreWikiAccessPoint;
import org.database.dtos.VehicleInfo;
import org.database.dtos.VehicleStats;
import org.database.dtos.playerVehicleStats.PlayerVehicleStats;
import org.dtos.VehicleModeStats;
import org.dtos.VehicleStatsResponse;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class VehicleServiceImpl implements VehicleService {

    public static void main(String... args){
        PostgreGameStatsAccessPoint gsap = new PostgreGameStatsAccessPoint();
        PostgreWikiAccessPoint wap = new PostgreWikiAccessPoint();
        PostgrePlayerStatsAccessPoint pap = new PostgrePlayerStatsAccessPoint();
        VehicleService vehicleService = new VehicleServiceImpl(gsap,wap,pap);
        vehicleService.update();
    }

    GameStatsAccessPoint gameStatsAccessPoint;
    WikiAccessPoint wikiAccessPoint;
    PlayerStatsAccessPoint playerStatsAccessPoint;

    public VehicleServiceImpl(GameStatsAccessPoint gameStatsAccessPoint, WikiAccessPoint wikiAccessPoint, PlayerStatsAccessPoint playerStatsAccessPoint) {
        this.gameStatsAccessPoint = gameStatsAccessPoint;
        this.wikiAccessPoint = wikiAccessPoint;
        this.playerStatsAccessPoint = playerStatsAccessPoint;
    }

    public VehicleServiceImpl(GameStatsAccessPoint gameStatsAccessPoint) {
        this.gameStatsAccessPoint = gameStatsAccessPoint;
    }

    @Override
    public void update() {
        CountDownLatch latch = new CountDownLatch(6);
        for(var mode : Modes.values())
            for (var type : new VehicleType[]{VehicleType.GroundVehicle, VehicleType.Aircraft})
                new Thread(() -> {
                    for (var vehicleId : wikiAccessPoint.getVehiclesIds(type)){
                        List<PlayerVehicleStats> vehicleStatsList = playerStatsAccessPoint.getVehicleStatList(vehicleId, mode, type);
                        int battles = 0;
                        int spawns = 0;
                        int deaths = 0;
                        int wins = 0;
                        int defeats = 0;
                        int air_kills = 0;
                        int ground_kills = 0;
                        for (var vehicle : vehicleStatsList){
                            battles += vehicle.getBattles();
                            spawns += vehicle.getSpawns();
                            deaths += vehicle.getDeaths();
                            wins += vehicle.getWins();
                            defeats += vehicle.getDefeats();
                            air_kills += vehicle.getAir_kills();
                            ground_kills += vehicle.getGround_kills();
                        }
                        VehicleStats vehicleStats = new VehicleStats(new VehicleInfo(vehicleId), mode, battles, spawns, deaths, wins, defeats, air_kills, ground_kills);
                        gameStatsAccessPoint.upsertVehicleStat(vehicleStats);

                    latch.countDown();
                    }
                }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VehicleStatsResponse getVehicleStats(String vehicleId) {
        List<VehicleStats> vehicleStats = gameStatsAccessPoint.getVehicleStat(vehicleId);
        if (vehicleStats.isEmpty())
            return null;
        return this.toVehicleResponse(vehicleStats);
    }

    @Override
    public List<VehicleStatsResponse> getVehiclesStats(Modes mode, VehicleType type) {
         List<VehicleStats> stats = gameStatsAccessPoint.getVehiclesStats(mode, type);
         List<VehicleStatsResponse> response = new ArrayList<>();
         while (!stats.isEmpty()){
             VehicleStats vReference = stats.get(0);
             List<VehicleStats> sameVehicleStats = stats.stream().filter(v ->
                     v.getVehicle().getVehicle_id().equals(vReference.getVehicle().getVehicle_id())).toList();
             response.add(this.toVehicleResponse(sameVehicleStats));
             stats.removeAll(sameVehicleStats);
         }
         return response;
    }

    private VehicleStatsResponse toVehicleResponse(List<VehicleStats> vehicleStats){
        VehicleStatsResponse response = new VehicleStatsResponse();
        response.setVehicleInfo(vehicleStats.get(0).getVehicle());
        for (var stat : vehicleStats){
            VehicleModeStats modeStats = new VehicleModeStats(
                    stat.getBattles(),stat.getSpawns(),stat.getDeaths(), stat.getWins(),
                    stat.getDefeats(), stat.getAir_kills(), stat.getGround_kills());
            switch (stat.getMode()){
                case "arcade" -> response.setArcade(modeStats);
                case "realistic" -> response.setRealistic(modeStats);
                case "simulation" -> response.setSimulation(modeStats);
            }
        }
        return response;
    }
}
