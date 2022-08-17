package org.thunderfinesse.services;

import org.database.PlayerStatsAccessPoint;
import org.database.postgre.PostgrePlayerStatsAccessPoint;
import org.database.dtos.Player;
import org.database.dtos.PlayerModes;
import org.database.dtos.playerVehicleStats.PlayerVehicleStats;
import org.dtos.PlayerStatsResponse;
import org.dtos.VehicleModeStats;
import org.enums.Modes;
import org.enums.VehicleType;
import org.thunderskill.*;

import java.util.Comparator;
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
    public PlayerStatsResponse getPlayerModesStats(String login) {
        Player player = playerStatsAccessPoint.getPlayer(login);
        if (player == null)
            thunderSkill.update(login);
        List<PlayerModes> playerModes = playerStatsAccessPoint.getPlayerModes(login);
        if (playerModes == null || playerModes.isEmpty())
            return null;

        //sorting: air_ab, ground_ab, air_rb, ground_rb, air_sb, ground_sb
        playerModes.sort(Comparator.comparing(PlayerModes::getMode).thenComparing(PlayerModes::getVehicle_type));
        PlayerStatsResponse response = new PlayerStatsResponse(login);
        response.setAir_ab(toVehicleModeStats(playerModes.get(0)));
        response.setGround_ab(toVehicleModeStats(playerModes.get(1)));
        response.setAir_rb(toVehicleModeStats(playerModes.get(2)));
        response.setGround_rb(toVehicleModeStats(playerModes.get(3)));
        response.setAir_sb(toVehicleModeStats(playerModes.get(4)));
        response.setGround_sb(toVehicleModeStats(playerModes.get(5)));

        return response;
    }

    @Override
    public boolean update(String login){
        thunderSkill.update(login);
        Player player = playerStatsAccessPoint.getPlayer(login);
        return player == null;
    }

    private VehicleModeStats toVehicleModeStats(PlayerModes playerModes){
        return new VehicleModeStats(
                playerModes.getBattles(),
                playerModes.getSpawns(),
                playerModes.getDeaths(),
                playerModes.getWins(),
                playerModes.getDefeats(),
                playerModes.getAir_kills(),
                playerModes.getGround_kills()
        );
    }
}
