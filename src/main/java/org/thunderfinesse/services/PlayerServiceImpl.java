package org.thunderfinesse.services;

import org.database.PlayerAlreadyInDatabaseException;
import org.database.PlayerStatsAccessPoint;
import org.database.postgre.WikiAccessPointPostgre;
import org.dtos.Player;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;
import org.thunderskill.*;

import java.util.List;

public class PlayerServiceImpl implements PlayerService{
    public static void main(String... args){
        var v = new PlayerServiceImpl();
        List<PlayerVehicleStats> x;
        try {
            x = v.getPlayerVehiclesStats("Luigi012", Modes.REALISTIC, VehicleType.GroundVehicle);
        } catch (NoSuchPlayerException e) {
            throw new RuntimeException(e);
        }
        System.err.println("");
    }

    private ThunderSkill thunderSkill;
    private PlayerStatsAccessPoint playerStatsAccessPoint;

    public PlayerServiceImpl(){
        PlayerStatsAccessPoint psap = new WikiAccessPointPostgre();
        ThunderskillPlayerScrapper tsps = new ThunderskillPlayerScraperJsoupImpl();
        thunderSkill = new ThunderSkillImpl(psap, tsps);
        playerStatsAccessPoint = new WikiAccessPointPostgre();
    }

    @Override
    public List<PlayerVehicleStats> getPlayerVehiclesStats(String login, Modes mode, VehicleType type) throws NoSuchPlayerException {
        Player player = playerStatsAccessPoint.getPlayer(login);
        if (player == null) {
            thunderSkill.update(login);
        }
        player = new Player(login);
        return playerStatsAccessPoint.getPlayerStats(player, mode, type);
    }
}
