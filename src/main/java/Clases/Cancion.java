/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Ryuka
 */
@Entity
@Table (name = "canciones")
public class Cancion implements Serializable {
    
    private static final long serialVersionUID = 1;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="canciones_id")
    private int id;

    @Column(name="canciones_nombre", length = 255)
    private String nombre;

    // Relación 1:N
    @OneToMany(mappedBy="cancion")
    private List<ClienteCancion> cancionCliente;

    public Cancion() {
    }

    public Cancion(String nombre) {
        this.nombre = nombre;
        cancionCliente = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ClienteCancion> getCancionCliente() {
        return cancionCliente;
    }

    public void setCancionCliente(List<ClienteCancion> cancionCliente) {
        this.cancionCliente = cancionCliente;
    }
    
    public void addCancionCliente(ClienteCancion cc) {
        this.cancionCliente.add(cc);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cancion{");
        sb.append("id = ").append(id);
        sb.append(", nombre = ").append(nombre);
        sb.append('}');
        return sb.toString();
    }
    
}
