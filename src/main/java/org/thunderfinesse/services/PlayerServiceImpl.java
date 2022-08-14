package org.thunderfinesse.services;

import org.database.PlayerStatsAccessPoint;
import org.database.postgre.PostgrePlayerStatsAccessPoint;
import org.database.dtos.Player;
import org.database.dtos.PlayerModes;
import org.database.dtos.playerVehicleStats.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;
import org.thunderskill.*;

import java.util.List;

public class PlayerServiceImpl implements PlayerService{
    private ThunderSkill thunderSkill;
    private PlayerStatsAccessPoint playerStatsAccessPoint;

    public PlayerServiceImpl(){
        PlayerStatsAccessPoint psap = new PostgrePlayerStatsAccessPoint();
        ThunderskillPlayerScrapper tsps = new ThunderskillPlayerScraperJsoupImpl();
        thunderSkill = new ThunderSkillImpl(psap, tsps);
        playerStatsAccessPoint = new PostgrePlayerStatsAccessPoint();
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

    @Override
    public List<PlayerModes> getPlayerModesStats(String login) {
        Player player = playerStatsAccessPoint.getPlayer(login);
        if (player == null) {
            thunderSkill.update(login);
        }
        return playerStatsAccessPoint.getPlayerModes(login);
    }

    @Override
    public boolean update(String login){
        thunderSkill.update(login);
        Player player = playerStatsAccessPoint.getPlayer(login);
        return player == null;
    }
}
