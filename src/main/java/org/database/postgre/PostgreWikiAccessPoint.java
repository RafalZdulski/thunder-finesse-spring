package org.database.postgre;

import org.database.WikiAccessPoint;
import org.dtos.VehicleInfo;
import org.enums.VehicleType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class PostgreWikiAccessPoint implements WikiAccessPoint {
    private EntityManagerFactory emf;

    public PostgreWikiAccessPoint(EntityManagerFactory emf){
        this.emf = emf;
    }

    public PostgreWikiAccessPoint() {
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
    public List<String> getVehiclesIds(VehicleType type) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT vehicle_id FROM VehicleInfo WHERE type = :type");
        q.setParameter("type", type.toString());
        List<String> ret = q.getResultList();
        em.close();
        return ret;
    }
}
