package org.thunderfinesse.services;

import org.database.GameStatsAccessPoint;
import org.database.PlayerStatsAccessPoint;
import org.database.WikiAccessPoint;
import org.database.postgre.PostgreAccessPoint;
import org.dtos.VehicleInfo;
import org.dtos.VehicleStats;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class VehicleServiceImpl implements VehicleService {

    public static void main(String... args){
        PostgreAccessPoint pap = new PostgreAccessPoint();
        VehicleService vehicleService = new VehicleServiceImpl(pap,pap,pap);
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
    public List<VehicleStats> getVehicleStats(String vehicleId) {
        return gameStatsAccessPoint.getVehicleStat(vehicleId);
    }

    @Override
    public List<VehicleStats> getVehiclesStats(Modes mode, VehicleType type) {
        return gameStatsAccessPoint.getVehiclesStats(mode, type);
    }
}
