/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Ryuka
 */
@Entity
@Table(name = "cliente_has_canciones")
public class ClienteCancion implements Serializable {
    
    private static final long serialVersionUID = 1;
    
    @ManyToOne
    @JoinColumn(name = "canciones_id")
    private Cancion cancion;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="cc_id")
    private int id;

    @Column(name="cc_fecha")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    
    public ClienteCancion() {
    }

    public ClienteCancion(Cancion cancion, Cliente cliente, Date fecha) {
        this.cancion = cancion;
        this.cliente = cliente;
        this.fecha = fecha;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "ClienteCancion{" + "cancion=" + cancion + ", cliente=" + cliente + ", id=" + id + ", fecha=" + fecha + '}';
    }
    
}
