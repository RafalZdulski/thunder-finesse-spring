package org.thunderwiki;

import org.database.WikiAccessPoint;
import org.database.postgre.PostgrePlayerStatsAccessPoint;
import org.database.postgre.PostgreWikiAccessPoint;
import org.dtos.VehicleInfo;
import org.enums.Nation;
import org.enums.VehicleType;

import javax.persistence.RollbackException;
import java.util.Collection;
import java.util.List;

public class ThunderWikiImpl implements ThunderWiki{
    public static void main(String... args){
        WikiAccessPoint wap = new PostgreWikiAccessPoint();
        TreeScrapper ts = new TreeScrapperImpl(new VehicleScrapperJsoupImpl());
        ThunderWiki thunderWiki = new ThunderWikiImpl(wap,ts);
        thunderWiki.updateAll();
    }

    private WikiAccessPoint wikiAccessPoint;
    private TreeScrapper treeScrapper;

    public ThunderWikiImpl(WikiAccessPoint wikiAccessPoint, TreeScrapper treeScrapper){
        this.wikiAccessPoint = wikiAccessPoint;
        this.treeScrapper = treeScrapper;
    }

    @Override
    public void update(Nation nation, VehicleType type) {
        List<VehicleInfo> vehicles = treeScrapper.getVehiclesInfo(nation, type);
        upsert(vehicles);
    }

    @Override
    public void update(Nation nation) {
        List<VehicleInfo> vehicles = treeScrapper.getVehiclesInfo(nation);
        upsert(vehicles);
    }

    @Override
    public void update(VehicleType type) {
        List<VehicleInfo> vehicles = treeScrapper.getVehiclesInfo(type);
        upsert(vehicles);
    }

    @Override
    public void updateAll() {
        List<VehicleInfo> vehicles = treeScrapper.getAllVehiclesInfo();
        upsert(vehicles);
    }

    private void upsert(Collection<VehicleInfo> vehicles){
        for (var vehicle : vehicles)
            try {
                wikiAccessPoint.saveVehicle(vehicle);
            }catch (RollbackException | IllegalStateException e){
                wikiAccessPoint.updateVehicle(vehicle);
            }
    }
}
