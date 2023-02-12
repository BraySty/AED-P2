/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
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
@Table (name = "clientes")
public class Cliente implements Serializable {
    
    private static final long serialVersionUID = 1;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private int id;

    @Column(name = "cliente_nombre", unique=true, length = 255)
    private String nombre;

    @Column(name = "cliente_contraseña", length = 255)
    private String contraseña;

    // Relación 1:N
    @OneToMany(mappedBy="cliente", cascade = CascadeType.ALL)
    private List<ClienteCancion> cancionCliente;

    public Cliente() {
    }

    public Cliente(int id, String nombre, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        cancionCliente = new LinkedList<>();
    }
    
    public Cliente(String nombre, String contraseña) {
        this.nombre = nombre;
        this.contraseña = contraseña;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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
        sb.append(nombre);
        return sb.toString();
    }
    
    
    
}
