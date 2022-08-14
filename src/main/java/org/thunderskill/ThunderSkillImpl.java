package org.thunderskill;

import org.database.PlayerAlreadyInDatabaseException;
import org.database.PlayerStatsAccessPoint;
import org.database.dtos.Player;
import org.database.dtos.PlayerModes;
import org.database.dtos.playerVehicleStats.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThunderSkillImpl implements ThunderSkill {
    PlayerStatsAccessPoint playerStatsAccessPoint;

    ThunderskillPlayerScrapper thunderskillPlayerScrapper;

    public ThunderSkillImpl(PlayerStatsAccessPoint playerStatsAccessPoint, ThunderskillPlayerScrapper thunderskillPlayerScrapper) {
        this.playerStatsAccessPoint = playerStatsAccessPoint;
        this.thunderskillPlayerScrapper = thunderskillPlayerScrapper;
    }

    @Override
    public void update(String login) {
        //player_details
        Player player = new Player(login);
        try{
            playerStatsAccessPoint.savePlayer(player);
        } catch (PlayerAlreadyInDatabaseException e){
            System.err.println(e.getMessage());
        }

        AtomicBoolean playerNotFound = new AtomicBoolean(false);

        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (var mode : Modes.values())
            for (var type : new VehicleType[]{VehicleType.GroundVehicle, VehicleType.Aircraft})
                new Thread(()->{
                    try {
                        List<PlayerVehicleStats> list = thunderskillPlayerScrapper.getPlayerStats(login, mode, type);
                        //player_modes
                        int battles = 0;
                        int spawns = 0;
                        int deaths = 0;
                        int wins = 0;
                        int defeats = 0;
                        int air_kills = 0;
                        int ground_kills = 0;
                        for (var vehicle : list) {
                            //player_vehicles_stats
                            playerStatsAccessPoint.upsertPlayerVehicleStats(vehicle);
                            //player_modes
                            battles += vehicle.getBattles();
                            spawns += vehicle.getSpawns();
                            deaths += vehicle.getDeaths();
                            wins += vehicle.getWins();
                            defeats += vehicle.getDefeats();
                            air_kills += vehicle.getAir_kills();
                            ground_kills += vehicle.getGround_kills();
                        }
                        PlayerModes playerMode = new PlayerModes(player, mode, type, battles, spawns, deaths, wins, defeats, air_kills, ground_kills);
                        playerStatsAccessPoint.upsertPlayerMode(playerMode);
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchPlayerException e) {
                        playerNotFound.set(true);
                        System.err.println(e.getMessage());
                    } finally {
                        countDownLatch.countDown();
                    }
                }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (playerNotFound.get())
            playerStatsAccessPoint.deletePlayer(login);

    }

    @Override
    public List<PlayerVehicleStats> getWithUpdate(String login, Modes mode, VehicleType type) throws NoSuchPlayerException {
        List<PlayerVehicleStats> retList = new ArrayList<>();
        try {
            retList.addAll(thunderskillPlayerScrapper.getPlayerStats(login, mode, type));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var vehicle : retList)
            playerStatsAccessPoint.upsertPlayerVehicleStats(vehicle);

        return retList;
    }

}
