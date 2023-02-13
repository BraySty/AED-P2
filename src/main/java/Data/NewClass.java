/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import Clases.Cancion;
import Clases.Cliente;
import JPAController.CancionJpaController;
import JPAController.ClienteCancionJpaController;
import JPAController.ClienteJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ryuka
 */
public class NewClass {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
        CancionJpaController cancionJpaC = new CancionJpaController(emf);
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        Cancion canciones = new Cancion("TEST");
        cancionJpaC.create(canciones);
        Cliente clientes = new Cliente("TEST", "TEST");
        clienteJpaC.create(clientes);
    }
}
