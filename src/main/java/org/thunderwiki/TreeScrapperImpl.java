package org.thunderwiki;

import org.dtos.VehicleInfo;
import org.enums.Nation;
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
import java.util.concurrent.CountDownLatch;


public class TreeScrapperImpl implements TreeScrapper {
    private static final String baseUrl = "https://wiki.warthunder.com/";
    private static final String baseTreeUrl = "https://wiki.warthunder.com/Category:{category}";

    VehicleScrapper vehicleScrapper;

    public TreeScrapperImpl(VehicleScrapper vehicleScrapper) {
        this.vehicleScrapper = vehicleScrapper;
    }

    @Override
    public List<VehicleInfo> getVehiclesInfo(Nation nation, VehicleType type) {
        List<VehicleInfo> vehicles = new ArrayList<>();
        CountDownLatch latch;
        String treeUrl = "";
        List<String> vehiclesUrls;

        try {
            treeUrl = baseTreeUrl.replace("{category}",getTreePagePath(nation, type));
            vehiclesUrls = fetchURLs(treeUrl);
        } catch (IOException e) {
            System.err.println("no such tree: " + treeUrl);
            return vehicles;
        }

        latch = new CountDownLatch(vehiclesUrls.size());
        for (var url : vehiclesUrls){
            new Thread(() -> {
                try {
                    VehicleInfo v = vehicleScrapper.parseVehicle(type, baseUrl + url);
                    vehicles.add(v);
                    System.out.println(v.getVehicle_id() + " parsed");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    @Override
    public List<VehicleInfo> getVehiclesInfo(Nation nation) {
        List<VehicleInfo> vehicles = new ArrayList<>();
        for (var type : VehicleType.values())
            vehicles.addAll(getVehiclesInfo(nation, type));
        return vehicles;
    }

    @Override
    public List<VehicleInfo> getVehiclesInfo(VehicleType type) {
        List<VehicleInfo> vehicles = new ArrayList<>();
        for (var nation : Nation.values())
            vehicles.addAll(getVehiclesInfo(nation, type));
        return vehicles;
    }

    @Override
    public List<VehicleInfo> getAllVehiclesInfo() {
        List<VehicleInfo> vehicles = new ArrayList<>();
        for (var nation : Nation.values())
            for (var type : VehicleType.values())
                vehicles.addAll(getVehiclesInfo(nation, type));
        return vehicles;
    }

    //  PRIVATE

    /**
     * @param nation
     * @param type
     * @return part of the tech tree URL labeled as {category} e.g. USA_ground_vehicles
     */
    private String getTreePagePath(Nation nation, VehicleType type){
        if (type == VehicleType.BluewaterVessel || type == VehicleType.CoastalVessel){
            return type.getWikiPath() + "_" + nation;
        }else{
            return nation + "_" + type.getWikiPath();
        }
    }

    /**
     * @param treeUrl tech tree URL e.g. https://wiki.warthunder.com/Category:USA_ground_vehicles
     * @return list of URLs to vehicle pages
     * @throws IOException when could not get InputStream from treeURL
     */
    private List<String> fetchURLs(String treeUrl) throws IOException {
        List<String> urls = new ArrayList<>();
        InputStream inputStream = new URL(treeUrl).openConnection().getInputStream();
        Document doc = Jsoup.parse(inputStream, "UTF-8",treeUrl);
        inputStream.close();
        Elements treeItems = doc.getElementsByClass("tree-item");
        for (var treeItem : treeItems){
            Element href = treeItem.getElementsByAttribute("href").get(0);
            String suffix = href.attr("href");
            urls.add(suffix.substring(1).replace(' ','_'));
        }
        return urls;
    }
}
