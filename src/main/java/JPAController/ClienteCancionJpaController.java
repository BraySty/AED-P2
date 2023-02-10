/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JPAController;

import Clases.ClienteCancion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Ryuka
 */
public class ClienteCancionJpaController {
    
    private EntityManagerFactory emf = null;

    public ClienteCancionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    /**
     * Metodo para ingresar un ClienteCancion.
     * @param cc El ClienteCancion a ingresar.
     * @return Regresa false si ya existe.
     */
    public boolean create(ClienteCancion cc) {
        EntityManager em = null;
        boolean operacion = false;
        if (findClienteCancion(cc.getId()) != null) {
            System.out.println("ClienteCancion " + cc + " ya existe.");
        } else {
            try {
            em = getEntityManager();
                em.getTransaction().begin();
                em.persist(cc);
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
     * Metodo para actualizar un ClienteCancion.
     * @param cc El ClienteCancion a actualizar.
     * @return Regresa true si se actualizo, false si no.
     */
    public boolean update(ClienteCancion cc) {
        EntityManager em = null;
        boolean operacion = false;
        if (findClienteCancion(cc.getId()) != null) {
            System.out.println("ClienteCancion " + cc + " no existe.");
        } else {
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.merge(cc);
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
     * Metodo para actualizar un ClienteCancion.
     * @param id int con el id que se elimina.
     * @return Regresa true si se actualizo, false si no.
     */
    public boolean delete(int id) {
        EntityManager em = null;
        boolean operacion = false;
        ClienteCancion clienteCancion = findClienteCancion(id);
        if (clienteCancion != null) {
            System.out.println("ClienteCancion " + id + " no existe.");
        } else {
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                ClienteCancion cc;
                cc = em.getReference(ClienteCancion.class, id);
                cc.getId();
                em.remove(cc);
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
     * Busca lun ClienteCancion por su id.
     * @param id Int con el id.
     * @return Regresa el ClienteCancion si existe, null si no existe.
     */
    public ClienteCancion findClienteCancion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteCancion.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * Metodo que devuelve una lista de ClienteCancion.
     * @return Regresa un List<ClienteCancion> con la lista de canciones.
     */
    public List<ClienteCancion> findClienteCancionEntities() {
        return findClienteCancionEntities(true, -1, -1);
    }

    /**
     * Metodo que devuelve una lista de ClienteCancion empezando y terminando
     * por los valores introducidos.
     * @param maxResults El limite de la lista.
     * @param firstResult El inicio de la lista.
     * @return Regresa un List<ClienteCancion> con la lista de canciones.
     */
    public List<ClienteCancion> findClienteCancionEntities(int maxResults, int firstResult) {
        return findClienteCancionEntities(false, maxResults, firstResult);
    }

    /**
     * 
     * @param all Booleano para elegir si recorger todos o por valores especificos.
     * @param maxResults int con el maximo de resultados.
     * @param firstResult int con el inicio del resultado.
     * @return Regresa un List con los ClienteCancion.
     */
    private List<ClienteCancion> findClienteCancionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteCancion.class));
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
    
    public List<Object[]> findClienteCancionCountAsc() {
        EntityManager em = getEntityManager();
        String jpql;
        Query query;
        jpql = "SELECT cc.cancion, cc.cliente, count(cc.cancion), cc.fecha FROM ClienteCancion cc GROUP BY cc.cliente ORDER BY count(cc.cancion)"; 
        try {
            query = em.createQuery(jpql);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Object[]> findClienteCancionCountDesc() {
        EntityManager em = getEntityManager();
        String jpql;
        Query query;
        jpql = "SELECT cc.cancion, cc.cliente, count(cc.cancion), cc.fecha FROM ClienteCancion cc GROUP BY cc.cliente ORDER BY count(cc.cancion) DESC"; 
        try {
            query = em.createQuery(jpql);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Regresa la cuenta de todas llos ClienteCancion.
     * @return Regresa un int.
     */
    public int getClienteCancionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteCancion> rt = cq.from(ClienteCancion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
