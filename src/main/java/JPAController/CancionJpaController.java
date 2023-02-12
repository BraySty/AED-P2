/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JPAController;

import Clases.Cancion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    
    /**
     * Metodo para ingresar una Cancion.
     * @param cancion la Cancion a ingresar.
     * @return Regresa false si ya existe.
     */
    public boolean create(Cancion cancion) {
        EntityManager em = null;
        boolean operacion = false;
        if (findCancion(cancion.getId()) != null) {
            System.out.println("Cancion " + cancion + " ya existe.");
        } else {
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.persist(cancion);
                em.getTransaction().commit();
                operacion = true;
            } catch (Exception ex) {
                throw ex;
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }
        return operacion;
    }
    
    /**
     * Metodo para actualizar una Cancion.
     * @param cancion La Cancion a actualizar.
     * @return Regresa true si se actualizo, false si no.
     */
    public boolean update(Cancion cancion){
        EntityManager em = null;
        boolean operacion = false;
        if (findCancion(cancion.getId()) == null) {
                System.out.println("Cancion " + cancion + " no existe.");
        } else {
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.merge(cancion);
                em.getTransaction().commit();
                operacion = true;
            } catch (Exception ex) {
                throw ex;
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }
        return operacion;
    }
    
    /**
     * Metodo para eliminar una Cancion.
     * @param id int con el id que se elimina.
     * @return Regresa true si se elimino, false si no.
     */
    public boolean delete(int id) {
        EntityManager em = null;
        boolean operacion = false;
        Cancion cancionTemp = findCancion(id);
        if (cancionTemp == null) {
                System.out.println("Cancion " + id + " no existe.");
        } else {
            Cancion cancion;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                cancion = em.getReference(Cancion.class, id);
                cancion.getId();
                em.remove(cancion);
                em.getTransaction().commit();
                operacion = true;
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }
        return operacion;
    }
    
    /**
     * Busca la cancion por su id.
     * @param id Int con el id.
     * @return Regresa la cancion si existe, null si no existe.
     */
    public Cancion findCancion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cancion.class, id);
        } finally {
            em.close();
        }
    }
    
    public Cancion findCancion(String nombre) {
        EntityManager em = getEntityManager();
        Cancion cancion = null;
        try {
            TypedQuery<Cancion> query = em.createQuery("SELECT c FROM Cancion c WHERE c.nombre = :name", Cancion.class);
            query.setParameter("name", nombre);
            cancion = query.getSingleResult();
        } catch(NoResultException nre) {
            System.err.println("La cancion con nombre" + nombre + " no existe.");
        } finally {
            em.close();
        }
        return cancion;
    }
    
    /**
     * Metodo que devuelve una lista de canciones.
     * @return Regresa un List<Cancion> con la lista de canciones.
     */
    public List<Cancion> findCancionEntities() {
        return findCancionEntities(true, -1, -1);
    }

    /**
     * Metodo que devuelve una lista de canciones empezando y terminando
     * por los valores introducidos.
     * @param maxResults El limite de la lista.
     * @param firstResult El inicio de la lista.
     * @return Regresa un List<Cancion> con la lista de canciones.
     */
    public List<Cancion> findCancionEntities(int maxResults, int firstResult) {
        return findCancionEntities(false, maxResults, firstResult);
    }

    /**
     * 
     * @param all Booleano para elegir si recorger todos o por valores especificos.
     * @param maxResults int con el maximo de resultados.
     * @param firstResult int con el inicio del resultado.
     * @return Regresa un List con las Cancion.
     */
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
    
    /**
     * Regresa la cuenta de todas las canciones.
     * @return Regresa un int.
     */
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
