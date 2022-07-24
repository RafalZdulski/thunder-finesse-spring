package org.thunderwiki;

import org.dtos.VehicleInfo;
import org.enums.*;
import org.enums.vehiclesclasses.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VehicleScrapperJsoupImpl implements VehicleScrapper {

    @Override
    public VehicleInfo parseVehicle(VehicleType vehicleType, String url) throws IOException, IllegalStateException {
        Element specsCard = getSpecsCard(url);
        if (specsCard == null) throw new IllegalStateException("could not fetch specs card for " + url);

        String vehicle_id = parseId(specsCard);
        String name = parseName(specsCard);
        String type = vehicleType.toString();
        String status = parseStatus(specsCard).toString();
        String klass = parseKlass(vehicleType, specsCard).toString();
        String nation = parseNation(specsCard).toString();
        String rank = parseRank(specsCard).toString();
        String[] brs = parseBattleRating(specsCard);
//        String subclass = String.valueOf(parseAircraftSubclasses(specsCard));
        String picture = parsePictureUrl(specsCard);

        return new VehicleInfo(vehicle_id, name, type, status, klass,
                nation, rank, brs[0], brs [1], brs[2], picture);
    }

    //  PRIVATE VEHICLE PARSING FUNCTIONS  //

    /**
     * @param vehiclePageUrl link to vehicle page e.g. https://wiki.warthunder.com/M2A4
     * @return specification card of given vehicle parsed as org.jsoup.nodes.Element
     * @throws IOException when could not get InputStream from given URL
     * specification card is grey card with battle rating next to photo of the vehicle
     */
    private Element getSpecsCard(String vehiclePageUrl) throws IOException {
        InputStream inputStream = new URL(vehiclePageUrl).openConnection().getInputStream();
        Document doc = Jsoup.parse(inputStream, "UTF-8",vehiclePageUrl);
        return doc.getElementsByClass("specs_card_main").first();
    }

    /**
     * @param specsCard specification card of given vehicle parsed as org.jsoup.nodes.Element
     * @return id of a vehicle e.g. us_m2a4
     */
    private String parseId(Element specsCard) {
        return specsCard.attr("data-code");
    }

    /**
     * @param specsCard specification card of given vehicle parsed as org.jsoup.nodes.Element
     * @return name of a vehicle e.g. M2A4
     */
    private String parseName(Element specsCard) {
        String name = specsCard.getElementsByClass("general_info_name").first().text();
        name = removeNationalSymbols(name,specsCard);
        return name;
    }

    /*TODO:
     * probably this function should be written better
     * for some reason only replaceAll() and split() catches national symbol
     * function like matches() do not work with this - I do not know why
     * wish to add (%nation%) to name of vehicle from which national symbols were removed
     */
    /**
     * removes national symbols from land-lease or captured vehicles
     * @param name name of given vehicle
     * @param specsCard specification card of given vehicle parsed as org.jsoup.nodes.Element
     * @return
     */
    private String removeNationalSymbols(String name, Element specsCard){
        return name.replaceAll("[^\\w\\s/. ()-]","");
    }

    private Nation parseNation(Element specsCard) {
        String s = specsCard.getElementsByClass("general_info_nation").last().text();
        return Nation.valueOf(s);
    }

    private Rank parseRank(Element specsCard) {
        String s = specsCard.getElementsByClass("general_info_rank").last().text();
        return Rank.valueOf(s.substring(0,s.indexOf(' ')));
    }

    private Status parseStatus(Element specsCard) {
        List<String> classes = parseClasses(specsCard);
        if(classes.contains("PREMIUM"))
            return Status.Premium;
        else if(classes.contains("SQUADRON"))
            return Status.Squadron;
        else{
            String s2 = specsCard.getElementsByClass("general_info_price").first().getElementsByClass("desc").first().text();
            if(s2.contains("Research"))
                return Status.Tree;
            else
                return Status.Gift;
        }
    }

    private Enum parseKlass(VehicleType type, Element specsCard){
        switch (type){
            case Aircraft: return parseAircraftClass(specsCard);
            case GroundVehicle: return parseGroundVehicleClass(specsCard);
            case Helicopter: return parseHelicopterClass(specsCard);
            case CoastalVessel: return parseCoastalVesselClass(specsCard);
            case BluewaterVessel: return parseBluewaterVesselClass(specsCard);
            default: throw new IllegalStateException("no klass for vehicle: " + parseId(specsCard));
        }
    }

    private String[] parseBattleRating(Element specsCard) {
        Element e = specsCard.getElementsByClass("general_info_br").last().getElementsByTag("tr").last();
        Element ab = e.getElementsByTag("td").get(0);
        Element rb = e.getElementsByTag("td").get(1);
        Element sb = e.getElementsByTag("td").get(2);
        return new String[]{ab.text(), rb.text(), sb.text()};
    }

    private String parsePictureUrl(Element specsCard) {
        try {
            Element e = specsCard.getElementsByClass("image").first().getElementsByTag("img").first();
            String url = e.attr("src");
            return "https://wiki.warthunder.com" + url;
        }catch (NullPointerException e){
            System.err.println("no picture for: " + this.parseId(specsCard));
            return "";
        }
    }

    private GroundVehicleClass parseGroundVehicleClass(Element specsCard) {
        List<String> classes = parseClasses(specsCard);
        for(var c : classes )
            if (contains(GroundVehicleClass.values(),c.replace(" ","_")))
                return GroundVehicleClass.valueOf(c.replace(" ","_"));
        throw new IllegalStateException("no ground vehicle class for vehicle: " + parseId(specsCard));
    }

    private AircraftClass parseAircraftClass(Element specsCard) {
        List<String> classes = parseClasses(specsCard);
        for(var c : classes )
            if (contains(AircraftClass.values(),c.replace(" ","_")))
                return AircraftClass.valueOf(c.replace(" ","_"));
        throw new IllegalStateException("no aircraft class for vehicle: " + parseId(specsCard));
    }

    private AircraftSubclass[] parseAircraftSubclasses(Element specsCard) {
        List<String> classes = parseClasses(specsCard);
        List<AircraftSubclass> list = new ArrayList<>();
        for (var c : classes)
            if (contains(AircraftSubclass.values(),c.replace(" ","_")))
                list.add(AircraftSubclass.valueOf(c.replace(" ","_")));
        return list.toArray(new AircraftSubclass[0]);
    }

    private HelicopterClass parseHelicopterClass(Element specsCard){
        List<String> classes = parseClasses(specsCard);
        for(var c : classes )
            if (contains(HelicopterClass.values(),c.replace(" ","_")))
                return HelicopterClass.valueOf(c.replace(" ","_"));
        throw new IllegalStateException("no helicopter class for vehicle: " + parseId(specsCard));
    }

    private CoastalVesselClass parseCoastalVesselClass(Element specsCard) throws IllegalStateException{
        List<String> classes = parseClasses(specsCard);
        for (var c : classes){
            if (contains(CoastalVesselClass.values(),c.replace(" ", "_").replace("-", "_")))
                return CoastalVesselClass.valueOf(c.replace(" ", "_").replace("-", "_"));
        }
        throw new IllegalStateException("no coastal vessel class for vehicle: " + parseId(specsCard));
    }

    private BluewaterVesselClass parseBluewaterVesselClass(Element specsCard) throws IllegalStateException{
        List<String> classes = parseClasses(specsCard);
        for(var c : classes )
            if (contains(BluewaterVesselClass.values(),c.replace(" ","_")))
                return BluewaterVesselClass.valueOf(c.replace(" ","_"));
        throw new IllegalStateException("no bluewater vessel class for vehicle: " + parseId(specsCard));
    }

    private List<String> parseClasses(Element specsCard) {
        List<String> list = new ArrayList<>();
        Elements elements = specsCard.getElementsByClass("general_info_class").first().getElementsByTag("a");
        for (var element : elements)
            list.add(element.text());
        return list;
    }

    private boolean contains(Object[] values, String s){
        for (var v : values) {
            if (v.toString().equalsIgnoreCase(s))
                return true;
        }
        return false;
    }



}