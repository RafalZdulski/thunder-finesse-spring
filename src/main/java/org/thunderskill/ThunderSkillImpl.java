package org.thunderskill;

import org.database.PlayerStatsAccessPoint;
import org.database.postgre.WikiAccessPointPostgre;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class ThunderSkillImpl implements ThunderSkill {
    public static void main(String... args) {
        PlayerStatsAccessPoint psap = new WikiAccessPointPostgre();
        ThunderskillPlayerScrapper tsps = new ThunderskillPlayerScraperJsoupImpl();
        ThunderSkill thunderSkill = new ThunderSkillImpl(psap, tsps);
        try {
            thunderSkill.update("resgksadilesdhbalsufne");
        } catch (NoSuchPlayerException e) {
            System.err.println(e.getMessage());
        }
    }

    PlayerStatsAccessPoint playerStatsAccessPoint;
    ThunderskillPlayerScrapper thunderskillPlayerScrapper;

    public ThunderSkillImpl(PlayerStatsAccessPoint playerStatsAccessPoint, ThunderskillPlayerScrapper thunderskillPlayerScrapper) {
        this.playerStatsAccessPoint = playerStatsAccessPoint;
        this.thunderskillPlayerScrapper = thunderskillPlayerScrapper;
    }

    @Override
    public void update(String login) throws NoSuchPlayerException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (var mode : Modes.values())
            for (var type : new VehicleType[]{VehicleType.GroundVehicle, VehicleType.Aircraft}){
                try {
                    List<PlayerVehicleStats> list = thunderskillPlayerScrapper.getPlayerStats(login, mode, type);
                    for (var vehicle : list)
                        playerStatsAccessPoint.upsertPlayerVehicleStats(vehicle);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    countDownLatch.countDown();
                }
            }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
