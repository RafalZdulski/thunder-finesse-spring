package org.database.postgre;

import org.database.GameStatsAccessPoint;
import org.database.dtos.VehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import javax.persistence.*;
import java.util.List;

public class PostgreGameStatsAccessPoint implements GameStatsAccessPoint {
    private EntityManagerFactory emf;

    public PostgreGameStatsAccessPoint(EntityManagerFactory emf){
        this.emf = emf;
    }

    public PostgreGameStatsAccessPoint() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void saveVehicleStat(VehicleStats vehicle) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vehicle);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateVehicleStat(VehicleStats vehicle) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(vehicle);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void upsertVehicleStat(VehicleStats vehicle) {
        try{
            saveVehicleStat(vehicle);
        }catch (RollbackException | IllegalStateException e){
            updateVehicleStat(vehicle);
        }
    }

    @Override
    public List<VehicleStats> getVehicleStat(String vehicleId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT c from VehicleStats c WHERE c.vehicle.vehicle_id = :vehicle_id");
        q.setParameter("vehicle_id", vehicleId);
        List<VehicleStats> ret = q.getResultList();
        em.close();
        return ret;
    }

    @Override
    public List<VehicleStats> getVehiclesStats(Modes mode, VehicleType type) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT c from VehicleStats c WHERE c.Mode = :mode AND c.vehicle.type = :type");
        q.setParameter("mode", mode.toString().toLowerCase());
        q.setParameter("type", type.toString());
        List<VehicleStats> ret = q.getResultList();
        em.close();
        return ret;
    }

}
