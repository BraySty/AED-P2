/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JPAController;

import Clases.Cliente;
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
public class ClienteJpaController {
    
    private EntityManagerFactory emf = null;

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    /**
     * Metodo para ingresar un Cliente.
     * @param cliente El Cliente a ingresar.
     * @return Regresa true si se creo, false si no.
     */
    public boolean create(Cliente cliente) {
        EntityManager em = null;
        boolean operacion = false;
        if (findCliente(cliente.getId()) != null) {
                System.out.println("Cliente " + cliente + " ya existe.");
        } else {
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.persist(cliente);
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
     * Metodo para actualizar un Cliente.
     * @param cliente El Clente a actualizar.
     * @return Regresa true si se actualizo, false si no.
     */
    public boolean update(Cliente cliente) {
        EntityManager em = null;
        boolean operacion = false;
        if (findCliente(cliente.getId()) == null) {
                System.out.println("Cliente " + cliente + " no existe.");
        } else {
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.merge(cliente);
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
     * Metodo para eliminar un Cliente.
     * @param id int con el id que se elimina.
     * @return Regresa true si se elimino, false si no.
     */
    public boolean delete(int id) {
        EntityManager em = null;
        boolean operacion = false;
        Cliente clienteTemp = findCliente(id);
        if (clienteTemp == null) {
                System.out.println("Cliente " + id + " no existe.");
        } else {
            Cliente cliente;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
                em.remove(cliente);
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
     * Busca el cliente por su id.
     * @param id Int con el id.
     * @return Regresa el cliente si existe, null si no existe.
     */
    public Cliente findCliente(int id) {
        EntityManager em = getEntityManager();
        Cliente cliente = null;
        try {
            cliente = em.find(Cliente.class, id);
        } catch(NoResultException nre) {
            System.out.println("El cliente con id " + id + " no existe.");
        } finally {
            em.close();
        }
        return cliente;
    }
    
    /**
     * Busca el Cliente por su nombre de usuario y contraseña
     * @param usuario String con el nombre de usuario.
     * @param password String con la contraseña.
     * @return Regresa el cliente si existe, null si no existe.
     */
    public Cliente findCliente(String usuario, String password) {
        EntityManager em = getEntityManager();
        Cliente cliente = null;
        try {
            TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c WHERE c.nombre = :name and c.contraseña = :password", Cliente.class);
            query.setParameter("name", usuario);
            query.setParameter("password", password);
            cliente = query.getSingleResult();
        } catch(NoResultException nre) {
            System.err.println("El cliente con nombre" + usuario + " no existe.");
        } finally {
            em.close();
        }
        return cliente;
    }
    
    /**
     * Metodo que devuelve una lista de canciones.
     * @return Regresa un List<Cliente> con la lista de canciones.
     */
    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    /**
     * Metodo que devuelve una lista de clientes empezando y terminando
     * por los valores introducidos.
     * @param maxResults El limite de la lista.
     * @param firstResult El inicio de la lista.
     * @return Regresa un List<Cliente> con la lista de clientes.
     */
    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    /**
     * 
     * @param all Booleano para elegir si recorger todos o por valores especificos.
     * @param maxResults int con el maximo de resultados.
     * @param firstResult int con el inicio del resultado.
     * @return Regresa un List con los clientes.
     */
    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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
     * Regresa la cuenta de todos los usuarios.
     * @return Regresa un int.
     */
    public int getClienteCount() {
        EntityManager em = getEntityManager();
        int count = -1;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            count = ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
        return count;
    }
    
}
