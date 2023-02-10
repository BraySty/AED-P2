/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JPAController;

import Clases.Cancion;
import codigo.exceptions.NonexistentEntityException;
import codigo.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Ryuka
 */
public class CancionJpaController {
    
    private EntityManagerFactory emf = null;

    public CancionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void create(Cancion cancion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cancion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCancion(cancion.getId()) != null) {
                throw new PreexistingEntityException("Cancion " + cancion + " ya existe.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void edit(Cancion cancion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cancion = em.merge(cancion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = cancion.getId();
                if (findCancion(id) == null) {
                    throw new NonexistentEntityException("La cancion " + id + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cancion cancion;
            try {
                cancion = em.getReference(Cancion.class, id);
                cancion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La cancion " + id + " no existe.", enfe);
            }
            em.remove(cancion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Cancion> findCancionEntities() {
        return findCancionEntities(true, -1, -1);
    }

    public List<Cancion> findCancionEntities(int maxResults, int firstResult) {
        return findCancionEntities(false, maxResults, firstResult);
    }

    private List<Cancion> findCancionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cancion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Cancion findCancion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cancion.class, id);
        } finally {
            em.close();
        }
    }
    
    public int getCancionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cancion> rt = cq.from(Cancion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
