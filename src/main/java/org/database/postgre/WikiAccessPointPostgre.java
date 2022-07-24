package org.database.postgre;

import org.database.PlayerStatsAccessPoint;
import org.database.WikiAccessPoint;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.dtos.VehicleInfo;

import javax.persistence.*;

public class WikiAccessPointPostgre implements WikiAccessPoint, PlayerStatsAccessPoint {

    private EntityManagerFactory emf;

    public WikiAccessPointPostgre(EntityManagerFactory emf){
        this.emf = emf;
    }

    public WikiAccessPointPostgre() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void saveVehicle(VehicleInfo vehicle) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vehicle);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateVehicle(VehicleInfo vehicle) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(vehicle);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void savePlayerVehicleStats(PlayerVehicleStats vehicle) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vehicle);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updatePlayerVehicleStats(PlayerVehicleStats vehicle) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(vehicle);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void upsertPlayerVehicleStats(PlayerVehicleStats vehicle) {
        try{
            savePlayerVehicleStats(vehicle);
        }catch (RollbackException | IllegalStateException e){
            updatePlayerVehicleStats(vehicle);
        }
    }
}
