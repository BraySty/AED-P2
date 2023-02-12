/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import Clases.Cancion;
import Clases.Cliente;
import Clases.ClienteCancion;
import JPAController.CancionJpaController;
import JPAController.ClienteCancionJpaController;
import JPAController.ClienteJpaController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import org.openide.util.Exceptions;

/**
 *
 * @author Ryuka
 */
public class Data {

    public Data() {
    }
    
    public void datosDePrueba(EntityManagerFactory emf) {
        CancionJpaController cancionJpaC = new CancionJpaController(emf);
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = "";
        Date fecha = new Date();
        
        Cancion[] canciones = new Cancion[]{
            new Cancion("Flowers"),
            new Cancion("Shakira: Bzrp music sessions Vol. 53"),
            new Cancion("La bachata"),
            new Cancion("Playa del inglés"),
            new Cancion("Calm down"),
            new Cancion("Llylm"),
            new Cancion("Cairo"),
            new Cancion("Miss you"),
            new Cancion("Snap"),
            new Cancion("Unholy"),
            new Cancion("Made you look"),
            new Cancion("I'm good (Blue)"),
            new Cancion("Plan fatal"),
            new Cancion("Sunroof"),
            new Cancion("Corazones rotos"),
            new Cancion("Forget me"),
            new Cancion("Qué bonita"),
            new Cancion("Suena en las bodas"),
            new Cancion("Celestial"),
            new Cancion("Quiero decirte"),
            new Cancion("Amigos"),
            new Cancion("Quasi"),
            new Cancion("Junio"),
            new Cancion("Bebiendo sola"),
            new Cancion("Quevedo: Bzrp music sessions Vol. 52"),
            new Cancion("Anti-hero"),
            new Cancion("Quieres"),
            new Cancion("I ain't worried"),
            new Cancion("Un clásico"),
            new Cancion("Mariposas"),
            new Cancion("Beso robado"),
            new Cancion("Agua salada"),
            new Cancion("Traductor"),
            new Cancion("As it was"),
            new Cancion("Envolver"),
            new Cancion("Los 12"),
            new Cancion("Monotonía"),
            new Cancion("Hay que vivir el momentoç"),
            new Cancion("Dime que no duele"),
            new Cancion("Despechá"),
        };
        
        for (Cancion cancion : canciones) {
            cancionJpaC.create(cancion);
        }
        
        Cliente[] clientes = new Cliente[]{
            new Cliente("Jon", "1234"),
            new Cliente("Alex", "1234"),
            new Cliente("David", "1234"),
            new Cliente("Marco", "1234"),
            new Cliente("Leon", "1234"),
            new Cliente("Kenedi", "1234"),
            new Cliente("Maximiliano", "1234"),
            new Cliente("Jeff", "1234"),
            new Cliente("Orwells", "1234"),
            new Cliente("Sandra", "1234"),
            new Cliente("Paco", "1234"),
            new Cliente("Willfredo", "1234"),
            new Cliente("Admin", "1234"),
            new Cliente("Samuel", "1234"),
            new Cliente("Leonardo", "1234"),
            new Cliente("Fabio", "1234"),
            new Cliente("Castro", "1234"),
            new Cliente("Ramirez", "1234"),
            new Cliente("Iris", "1234"),
            new Cliente("Ronald", "1234"),
            new Cliente("Cornelius", "1234"),
            
        };
        
        for (Cliente cliente : clientes) {
            clienteJpaC.create(cliente);
        }
        
        date = "2022-08-12";
        try {
            fecha = formatter.parse(date);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        ClienteCancion[] cc = new ClienteCancion[] {
            new ClienteCancion(canciones[3], clientes[1], fecha),
            new ClienteCancion(canciones[13], clientes[1], fecha),
            new ClienteCancion(canciones[31], clientes[1], fecha),
            new ClienteCancion(canciones[32], clientes[6], fecha),
            new ClienteCancion(canciones[3], clientes[4], fecha),
            new ClienteCancion(canciones[3], clientes[8], fecha),
            new ClienteCancion(canciones[31], clientes[6], fecha),
            new ClienteCancion(canciones[32], clientes[13], fecha),
            new ClienteCancion(canciones[35], clientes[14], fecha),
            new ClienteCancion(canciones[3], clientes[15], fecha),
            new ClienteCancion(canciones[15], clientes[12], fecha),
            new ClienteCancion(canciones[14], clientes[11], fecha),
            new ClienteCancion(canciones[9], clientes[1], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[1], fecha),
            
            new ClienteCancion(canciones[3], clientes[3], fecha),
            new ClienteCancion(canciones[13], clientes[2], fecha),
            new ClienteCancion(canciones[31], clientes[2], fecha),
            new ClienteCancion(canciones[32], clientes[3], fecha),
            new ClienteCancion(canciones[3], clientes[5], fecha),
            new ClienteCancion(canciones[3], clientes[6], fecha),
            new ClienteCancion(canciones[31], clientes[13], fecha),
            new ClienteCancion(canciones[32], clientes[13], fecha),
            new ClienteCancion(canciones[35], clientes[13], fecha),
            new ClienteCancion(canciones[3], clientes[16], fecha),
            new ClienteCancion(canciones[15], clientes[14], fecha),
            new ClienteCancion(canciones[14], clientes[7], fecha),
            new ClienteCancion(canciones[9], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
        };
        
        for (ClienteCancion clienteCancion : cc) {
            ccJpaC.create(clienteCancion);
        }
        
        date = "2022-08-17";
        try {
            fecha = formatter.parse(date);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        ClienteCancion[] cc2 = new ClienteCancion[] {
            new ClienteCancion(canciones[3], clientes[1], fecha),
            new ClienteCancion(canciones[13], clientes[1], fecha),
            new ClienteCancion(canciones[21], clientes[1], fecha),
            new ClienteCancion(canciones[22], clientes[6], fecha),
            new ClienteCancion(canciones[3], clientes[4], fecha),
            new ClienteCancion(canciones[3], clientes[8], fecha),
            new ClienteCancion(canciones[21], clientes[6], fecha),
            new ClienteCancion(canciones[22], clientes[13], fecha),
            new ClienteCancion(canciones[25], clientes[14], fecha),
            new ClienteCancion(canciones[3], clientes[15], fecha),
            new ClienteCancion(canciones[15], clientes[12], fecha),
            new ClienteCancion(canciones[14], clientes[11], fecha),
            new ClienteCancion(canciones[9], clientes[1], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[1], fecha),
            
            new ClienteCancion(canciones[3], clientes[3], fecha),
            new ClienteCancion(canciones[13], clientes[2], fecha),
            new ClienteCancion(canciones[21], clientes[2], fecha),
            new ClienteCancion(canciones[22], clientes[3], fecha),
            new ClienteCancion(canciones[3], clientes[5], fecha),
            new ClienteCancion(canciones[3], clientes[6], fecha),
            new ClienteCancion(canciones[21], clientes[13], fecha),
            new ClienteCancion(canciones[22], clientes[13], fecha),
            new ClienteCancion(canciones[25], clientes[13], fecha),
            new ClienteCancion(canciones[3], clientes[16], fecha),
            new ClienteCancion(canciones[15], clientes[14], fecha),
            new ClienteCancion(canciones[14], clientes[7], fecha),
            new ClienteCancion(canciones[9], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
        };
        
        for (ClienteCancion clienteCancion : cc2) {
            ccJpaC.create(clienteCancion);
        }
        
        date = "2022-09-12";
        try {
            fecha = formatter.parse(date);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        ClienteCancion[] cc3 = new ClienteCancion[] {
            new ClienteCancion(canciones[3], clientes[1], fecha),
            new ClienteCancion(canciones[13], clientes[1], fecha),
            new ClienteCancion(canciones[31], clientes[1], fecha),
            new ClienteCancion(canciones[32], clientes[6], fecha),
            new ClienteCancion(canciones[3], clientes[4], fecha),
            new ClienteCancion(canciones[3], clientes[8], fecha),
            new ClienteCancion(canciones[31], clientes[6], fecha),
            new ClienteCancion(canciones[32], clientes[13], fecha),
            new ClienteCancion(canciones[35], clientes[14], fecha),
            new ClienteCancion(canciones[3], clientes[15], fecha),
            new ClienteCancion(canciones[15], clientes[12], fecha),
            new ClienteCancion(canciones[14], clientes[11], fecha),
            new ClienteCancion(canciones[9], clientes[1], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[1], fecha),
            
            new ClienteCancion(canciones[3], clientes[3], fecha),
            new ClienteCancion(canciones[13], clientes[2], fecha),
            new ClienteCancion(canciones[31], clientes[2], fecha),
            new ClienteCancion(canciones[32], clientes[3], fecha),
            new ClienteCancion(canciones[3], clientes[5], fecha),
            new ClienteCancion(canciones[3], clientes[6], fecha),
            new ClienteCancion(canciones[31], clientes[13], fecha),
            new ClienteCancion(canciones[32], clientes[13], fecha),
            new ClienteCancion(canciones[35], clientes[13], fecha),
            new ClienteCancion(canciones[3], clientes[16], fecha),
            new ClienteCancion(canciones[15], clientes[14], fecha),
            new ClienteCancion(canciones[14], clientes[7], fecha),
            new ClienteCancion(canciones[9], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
        };
        
        for (ClienteCancion clienteCancion : cc3) {
            ccJpaC.create(clienteCancion);
        }
        
        date = "2022-08-25";
        try {
            fecha = formatter.parse(date);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        ClienteCancion[] cc4 = new ClienteCancion[] {
            new ClienteCancion(canciones[3], clientes[1], fecha),
            new ClienteCancion(canciones[13], clientes[1], fecha),
            new ClienteCancion(canciones[21], clientes[1], fecha),
            new ClienteCancion(canciones[22], clientes[6], fecha),
            new ClienteCancion(canciones[3], clientes[4], fecha),
            new ClienteCancion(canciones[3], clientes[8], fecha),
            new ClienteCancion(canciones[21], clientes[6], fecha),
            new ClienteCancion(canciones[22], clientes[13], fecha),
            new ClienteCancion(canciones[25], clientes[14], fecha),
            new ClienteCancion(canciones[3], clientes[15], fecha),
            new ClienteCancion(canciones[15], clientes[12], fecha),
            new ClienteCancion(canciones[14], clientes[11], fecha),
            new ClienteCancion(canciones[9], clientes[1], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[1], fecha),
            
            new ClienteCancion(canciones[3], clientes[3], fecha),
            new ClienteCancion(canciones[13], clientes[2], fecha),
            new ClienteCancion(canciones[21], clientes[2], fecha),
            new ClienteCancion(canciones[22], clientes[3], fecha),
            new ClienteCancion(canciones[3], clientes[5], fecha),
            new ClienteCancion(canciones[3], clientes[6], fecha),
            new ClienteCancion(canciones[21], clientes[13], fecha),
            new ClienteCancion(canciones[22], clientes[13], fecha),
            new ClienteCancion(canciones[25], clientes[13], fecha),
            new ClienteCancion(canciones[3], clientes[16], fecha),
            new ClienteCancion(canciones[15], clientes[14], fecha),
            new ClienteCancion(canciones[14], clientes[7], fecha),
            new ClienteCancion(canciones[9], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
            new ClienteCancion(canciones[7], clientes[7], fecha),
        };
        
        for (ClienteCancion clienteCancion : cc4) {
            ccJpaC.create(clienteCancion);
        }
    }
    
}
