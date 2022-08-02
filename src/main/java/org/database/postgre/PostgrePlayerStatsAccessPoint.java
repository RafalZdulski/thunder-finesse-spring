package org.database.postgre;

import org.database.GameStatsAccessPoint;
import org.database.PlayerAlreadyInDatabaseException;
import org.database.PlayerStatsAccessPoint;
import org.database.WikiAccessPoint;
import org.dtos.Player;
import org.dtos.PlayerModes;
import org.dtos.VehicleStats;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.dtos.VehicleInfo;
import org.enums.Modes;
import org.enums.VehicleType;

import javax.persistence.*;
import java.util.List;

public class PostgrePlayerStatsAccessPoint implements PlayerStatsAccessPoint {

    private EntityManagerFactory emf;

    public PostgrePlayerStatsAccessPoint(EntityManagerFactory emf){
        this.emf = emf;
    }

    public PostgrePlayerStatsAccessPoint() {
        emf = Persistence.createEntityManagerFactory("default");
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

    @Override
    public void savePlayer(Player player) throws PlayerAlreadyInDatabaseException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(player);
            em.getTransaction().commit();
        }catch (IllegalStateException | RollbackException e){
            throw new PlayerAlreadyInDatabaseException("player " + player.getLogin() + " already in database", e.getCause());
        }finally {
            em.close();
        }
    }

    @Override
    public Player getPlayer(String login) {
        EntityManager em = emf.createEntityManager();
        Player player = em.find(Player.class, login);
        em.close();
        return player;
    }

    @Override
    public List<PlayerVehicleStats> getPlayerStats(Player player, Modes mode, VehicleType type) {
        EntityManager em = emf.createEntityManager();
        Query q = getPlayerStatsQuery(em, mode, type);
        q.setParameter("login", player.getLogin());
        List<PlayerVehicleStats> ret = q.getResultList();
        em.close();
        return ret;
    }

    @Override
    public void savePlayerMode(PlayerModes playerMode) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(playerMode);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updatePlayerMode(PlayerModes playerMode) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(playerMode);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void upsertPlayerMode(PlayerModes playerMode) {
        try{
            savePlayerMode(playerMode);
        }catch (RollbackException | IllegalStateException e){
            updatePlayerMode(playerMode);
        }
    }

    @Override
    public List<PlayerModes> getPlayerModes(String login) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT c FROM PlayerModes c WHERE c.player.login = :login");
        q.setParameter("login", login);
        List<PlayerModes> ret = q.getResultList();
        em.close();
        return ret;
    }

    @Override
    public List<PlayerVehicleStats> getVehicleStatList(String vehicleId, Modes mode, VehicleType type) {
        EntityManager em = emf.createEntityManager();
        Query q = this.getVehicleStatsQuery(em, mode, type);
        q.setParameter("vehicle_id", vehicleId);
        List<PlayerVehicleStats> ret = q.getResultList();
        em.close();
        return ret;
    }

    //TODO there must be easy way to do this queries
    private Query getPlayerStatsQuery(EntityManager em, Modes mode, VehicleType type) {
        if (mode == Modes.ARCADE && type == VehicleType.Aircraft)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsAirAB c WHERE c.player.login = :login");
        if (mode == Modes.ARCADE && type == VehicleType.GroundVehicle)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsGroundAB c WHERE c.player.login = :login");
        if (mode == Modes.REALISTIC && type == VehicleType.Aircraft)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsAirRB c WHERE c.player.login = :login");
        if (mode == Modes.REALISTIC && type == VehicleType.GroundVehicle)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsGroundRB c WHERE c.player.login = :login");
        if (mode == Modes.SIMULATION && type == VehicleType.Aircraft)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsAirSB c WHERE c.player.login = :login");
        if (mode == Modes.SIMULATION && type == VehicleType.GroundVehicle)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsGroundSB c WHERE c.player.login = :login");
        throw new IllegalStateException("mode or type not supported");
    }

    private Query getVehicleStatsQuery(EntityManager em, Modes mode, VehicleType type) {
        if (mode == Modes.ARCADE && type == VehicleType.Aircraft)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsAirAB c WHERE c.vehicle.vehicle_id = :vehicle_id");
        if (mode == Modes.ARCADE && type == VehicleType.GroundVehicle)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsGroundAB c WHERE c.vehicle.vehicle_id = :vehicle_id");
        if (mode == Modes.REALISTIC && type == VehicleType.Aircraft)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsAirRB c WHERE c.vehicle.vehicle_id = :vehicle_id");
        if (mode == Modes.REALISTIC && type == VehicleType.GroundVehicle)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsGroundRB c WHERE c.vehicle.vehicle_id = :vehicle_id");
        if (mode == Modes.SIMULATION && type == VehicleType.Aircraft)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsAirSB c WHERE c.vehicle.vehicle_id = :vehicle_id");
        if (mode == Modes.SIMULATION && type == VehicleType.GroundVehicle)
            return em.createQuery("SELECT c FROM PlayerVehicleStatsGroundSB c WHERE c.vehicle.vehicle_id = :vehicle_id");
        throw new IllegalStateException("mode or type not supported");
    }
}
