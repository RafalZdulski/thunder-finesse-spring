package org.thunderskill;

import org.dtos.Player;
import org.dtos.VehicleInfo;
import org.dtos.playerVehicleStatsTables.*;
import org.enums.Modes;
import org.enums.VehicleType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThunderskillPlayerScraperJsoupImpl implements ThunderskillPlayerScrapper {
    private static final String baseUrl = "https://thunderskill.com/en/stat/{login}/vehicles/{mode}";

    //TODO need some optimization
    @Override
    public List<PlayerVehicleStats> getPlayerStats(String login, Modes mode, VehicleType type) throws IOException, NoSuchPlayerException {
        String url = baseUrl.replace("{login}", login).replace("{mode}", mode.getLetter());
        Elements table = fetchTable(url, type);
        List<PlayerVehicleStats> playerVehicleStats= new ArrayList<>();
        for (var row : table){
            try {
                playerVehicleStats.add(parseVehicle(login, mode, type, row));
            }catch (IllegalStateException e){
                //System.err.println(e.getMessage());
            }
        }
        return playerVehicleStats;
    }

    private Elements fetchTable(String url, VehicleType type) throws IOException {
        InputStream inputStream = new URL(url).openConnection().getInputStream();
        Document doc = Jsoup.parse(inputStream, "UTF-8", url);
        inputStream.close();
        return switch (type){
            case Aircraft -> doc.getElementsByTag("tbody").first().children();
            case GroundVehicle -> doc.getElementsByTag("tbody").last().children();
            default -> throw new IllegalStateException("vehicle type: " + type + " not supported");
        };
    }

    private PlayerVehicleStats parseVehicle(String login, Modes mode, VehicleType type, Element row) throws IllegalStateException, NoSuchPlayerException {
        String vehicleId = row.getElementsByAttribute("href").attr("href");
        vehicleId = vehicleId.substring(vehicleId.lastIndexOf("/")+1);
        Elements details = null;
        try {
            details = row.getElementsByClass("params").first().children();
        }catch(NullPointerException e){
            //if player doesn't exist .children() will throw NullPointerException
            //TODO find better way to determine if player exists
            throw new NoSuchPlayerException("player " + login + " not found");
        }
        Element battlesElement = (Element) details.stream().filter(c -> c.toString().contains("Battles")).toArray()[0];
        int battles = Integer.parseInt(battlesElement.childNode(1).childNode(0).childNode(0).toString());

        if(battles == 0){
            throw new IllegalStateException("zero battles in " + vehicleId);
        }

        Element respawnsElement = (Element) details.stream().filter(c -> c.toString().contains("Respawns")).toArray()[0];
        Element victoriesElement = (Element) details.stream().filter(c -> c.toString().contains("Victories")).toArray()[0];
        Element defeatsElement = (Element) details.stream().filter(c -> c.toString().contains("Defeats")).toArray()[0];
        Element deathsElement = (Element) details.stream().filter(c -> c.toString().contains("Deaths")).toArray()[0];
        Element airKillsElement = (Element) details.stream().filter(c -> c.toString().contains("Overall air frags")).toArray()[0];
        Element groundKillsElement = (Element) details.stream().filter(c -> c.toString().contains("Overall ground frags")).toArray()[0];

        int respawns = Integer.parseInt(respawnsElement.childNode(1).childNode(0).childNode(0).toString());
        int victories = Integer.parseInt(victoriesElement.childNode(1).childNode(0).childNode(0).toString());
        int defeats = Integer.parseInt(defeatsElement.childNode(1).childNode(0).childNode(0).toString());
        int deaths = Integer.parseInt(deathsElement.childNode(1).childNode(0).childNode(0).toString());
        int airKills = Integer.parseInt(airKillsElement.childNode(1).childNode(0).childNode(0).toString());
        int groundKills = Integer.parseInt(groundKillsElement.childNode(1).childNode(0).childNode(0).toString());

        Player player = new Player(login);
        if (type == VehicleType.Aircraft && mode == Modes.ARCADE)
            return new PlayerVehicleStatsAirAB(login, new VehicleInfo(vehicleId), battles, respawns, deaths, victories, defeats, airKills, groundKills);
        if (type == VehicleType.Aircraft && mode == Modes.REALISTIC)
            return new PlayerVehicleStatsAirRB(login, new VehicleInfo(vehicleId), battles, respawns, deaths, victories, defeats, airKills, groundKills);
        if (type == VehicleType.Aircraft && mode == Modes.SIMULATION)
            return new PlayerVehicleStatsAirSB(login, new VehicleInfo(vehicleId), battles, respawns, deaths, victories, defeats, airKills, groundKills);
        if (type == VehicleType.GroundVehicle && mode == Modes.ARCADE)
            return new PlayerVehicleStatsGroundAB(login, new VehicleInfo(vehicleId), battles, respawns, deaths, victories, defeats, airKills, groundKills);
        if (type == VehicleType.GroundVehicle && mode == Modes.REALISTIC)
            return new PlayerVehicleStatsGroundRB(login, new VehicleInfo(vehicleId), battles, respawns, deaths, victories, defeats, airKills, groundKills);
        if (type == VehicleType.GroundVehicle && mode == Modes.SIMULATION)
            return new PlayerVehicleStatsGroundSB(login, new VehicleInfo(vehicleId), battles, respawns, deaths, victories, defeats, airKills, groundKills);
        else
            throw new IllegalStateException("mode: " + type + "-" + mode + " not supported");
    }
}
