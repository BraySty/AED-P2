/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Launcher;

import Clases.Cancion;
import Clases.Cliente;
import Clases.ClienteCancion;
import JPAController.CancionJpaController;
import JPAController.ClienteCancionJpaController;
import JPAController.ClienteJpaController;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.service.spi.ServiceException;

/**
 *
 * @author Ryuka
 */
public class Launcher extends javax.swing.JFrame {
    
    private static EntityManagerFactory emf;
    private String dataBase = "karaoke";
    private String dbUser = "root";
    private String dbPassword = "";
    private Cliente cliente;
    private final String[] titulo = new String[]{"Cancion", "Usuario", "Repeticiones", "Fecha"};
    private final String[] clientesTable = new String[]{"ID", "Usuario", "Contraseña"};
    private final String[] cancionTable = new String[]{"ID", "Cancion"};
    private final String[] historialTable = new String[]{"ID", "Cancion", "Cancion", "Fecha"};
    
    /**
     * Creates new form Launcher
     */
    public Launcher() {
        initComponents();
        inicializarEMF();
        
    }
    
    /**
     * Inicializa el EMF al empezar la aplicacion.
     */
    public void inicializarEMF() {
        try {
            emf = Persistence.createEntityManagerFactory("persistencia");
        } catch (ServiceException se) {
            JOptionPane.showMessageDialog(this, "Revise el usuario y la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException se) {
            JOptionPane.showMessageDialog(this, "Revise el usuario y la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        }
        inicializarTablaPrincipal();
        inicializarTablaHistorial();
        inicializarTablaClientes();
        inicializarTablaCanciones();
    }
    
    /**
     * Inicializa las tablas al empezar la aplicacion.
     */
    private void inicializarTablaPrincipal() {
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        // Crea la nueva tabla.
        DefaultTableModel dtm = new DefaultTableModel() {
            // Permite seleccionar que celdas son editables.
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == -1;
            }
        };
        // Añade las columnas a la tabla.
        dtm.setColumnIdentifiers(titulo);
        jTableHistorial.setModel(dtm);
        List<Object[]> clienteCancion = ccJpaC.findClienteCancionCountDesc();
        for (Object[] cc : clienteCancion) {
            //Pasa la informacion a la pantalla principal.
            DefaultTableModel dtm2 = (DefaultTableModel)jTableHistorial.getModel();
            dtm2.addRow(cc);
        }
    }
    
    /**
     * Inicializa la tabla historial.
     */
    private void inicializarTablaHistorial() {
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        // Crea la nueva tabla.
        DefaultTableModel dtm = new DefaultTableModel() {
            // Permite seleccionar que celdas son editables.
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == -1;
            }
        };
        // Añade las columnas a la tabla.
        dtm.setColumnIdentifiers(historialTable);
        jTableDBHistorial.setModel(dtm);
        List<ClienteCancion> cc = ccJpaC.findClienteCancionEntities();
        for (ClienteCancion c2Object : cc) {
            Object[] objeto = new Object[] {
                c2Object.getId(), 
                c2Object.getCliente(),
                c2Object.getCancion(),
                c2Object.getFecha()
            };
            //Pasa la informacion a la pantalla principal.
            DefaultTableModel dtm2 = (DefaultTableModel)jTableDBHistorial.getModel();
            dtm2.addRow(objeto);
        }
    }
    
    /**
     * Inicializa la tabla clientes.
     */
    private void inicializarTablaClientes() {
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        // Crea la nueva tabla.
        DefaultTableModel dtm = new DefaultTableModel() {
            // Permite seleccionar que celdas son editables.
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == -1;
            }
        };
        // Añade las columnas a la tabla.
        dtm.setColumnIdentifiers(clientesTable);
        jTableDBClientes.setModel(dtm);
        List<Cliente> cliente = clienteJpaC.findClienteEntities();
        for (Cliente cliente2Object : cliente) {
            
            Object[] objeto = new Object[] {
                cliente2Object.getId(), 
                cliente2Object.getNombre(), 
                cliente2Object.getContraseña()
            };
            //Pasa la informacion a la pantalla principal.
            DefaultTableModel dtm2 = (DefaultTableModel)jTableDBClientes.getModel();
            dtm2.addRow(objeto);
        }
    }
    
    /**
     * Inicializa la tabla canciones.
     */
    private void inicializarTablaCanciones() {
        CancionJpaController cancionJpaC = new CancionJpaController(emf);
        // Crea la nueva tabla.
        DefaultTableModel dtm = new DefaultTableModel() {
            // Permite seleccionar que celdas son editables.
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == -1;
            }
        };
        // Añade las columnas a la tabla.
        dtm.setColumnIdentifiers(cancionTable);
        jTableDBCanciones.setModel(dtm);
        List<Cancion> cancion = cancionJpaC.findCancionEntities();
        for (Cancion cancion2Object : cancion) {
            Object[] objeto = new Object[] {
                cancion2Object.getId(), 
                cancion2Object.getNombre()
            };
            //Pasa la informacion a la pantalla principal.
            DefaultTableModel dtm2 = (DefaultTableModel)jTableDBCanciones.getModel();
            dtm2.addRow(objeto);
        }
    }
    
    /**
     * Restablece el EMF con los nuevos datos de usuario y contraseña.
     * @return 
     */
    public EntityManagerFactory setPropiedades() {
        EntityManagerFactory emf = null;
        try {
            Properties props = new Properties();
            props.setProperty("javax.persistence.jdbc.url", "jdbc:mariadb://localhost:3306/" + dataBase + "?createDatabaseIfNotExist=true");
            props.setProperty("javax.persistence.jdbc.user", dbUser);
            props.setProperty("javax.persistence.jdbc.javax.persistence.jdbc.password", dbPassword);
            emf = Persistence.createEntityManagerFactory("persistencia", props);
        } catch (ServiceException se) {
            JOptionPane.showMessageDialog(this, "Revise el usuario y la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return emf;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPanelPrincipal = new javax.swing.JTabbedPane();
        jPanelInicioSesion = new javax.swing.JPanel();
        jTextFieldClienteUsuario = new javax.swing.JTextField();
        jLabelClienteUusuario = new javax.swing.JLabel();
        jLabelClientePassword = new javax.swing.JLabel();
        jTextFieldClientePassword = new javax.swing.JTextField();
        jButtonLogIn = new javax.swing.JButton();
        jButtonCrearUsuario = new javax.swing.JButton();
        jPanelHistorial = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHistorial = new javax.swing.JTable();
        jTabbedAdministracion = new javax.swing.JTabbedPane();
        jPanelDBHistorial = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableDBHistorial = new javax.swing.JTable();
        jPanelDBClientes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDBClientes = new javax.swing.JTable();
        jLabelClienteID = new javax.swing.JLabel();
        jLabelClienteNombre = new javax.swing.JLabel();
        jLabelClienteContraseña = new javax.swing.JLabel();
        jTextFieldClienteNombre = new javax.swing.JTextField();
        jSpinnerClienteID = new javax.swing.JSpinner();
        jTextFieldClienteContraseña = new javax.swing.JTextField();
        jButtonClienteBorrar = new javax.swing.JButton();
        jButtonClienteActualizar = new javax.swing.JButton();
        jButtonClienteAgregar = new javax.swing.JButton();
        jPanelDBCanciones = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableDBCanciones = new javax.swing.JTable();
        jLabelCancionesID = new javax.swing.JLabel();
        jLabelCancionNombre = new javax.swing.JLabel();
        jTextFieldCancionNombre = new javax.swing.JTextField();
        jSpinnerCancionID = new javax.swing.JSpinner();
        jButtonCancionAgregar = new javax.swing.JButton();
        jButtonCancionActualizar = new javax.swing.JButton();
        jButtonCancionesBorrar = new javax.swing.JButton();
        jPanelGestorConexion = new javax.swing.JPanel();
        jLabelMariaDBUusuario = new javax.swing.JLabel();
        jLabelMariaDBDPassword = new javax.swing.JLabel();
        jTextFieldMariaDBPassword = new javax.swing.JTextField();
        jTextFieldMariaDBUsuario = new javax.swing.JTextField();
        jButtonAceptar = new javax.swing.JButton();
        jButtonDefault = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPanelPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPanelPrincipalStateChanged(evt);
            }
        });

        jLabelClienteUusuario.setText("Usuario");

        jLabelClientePassword.setText("Contraseña");

        jButtonLogIn.setText("Aceptar");
        jButtonLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogInActionPerformed(evt);
            }
        });

        jButtonCrearUsuario.setText("Crear usuario");
        jButtonCrearUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelInicioSesionLayout = new javax.swing.GroupLayout(jPanelInicioSesion);
        jPanelInicioSesion.setLayout(jPanelInicioSesionLayout);
        jPanelInicioSesionLayout.setHorizontalGroup(
            jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInicioSesionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInicioSesionLayout.createSequentialGroup()
                        .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClientePassword)
                            .addComponent(jLabelClienteUusuario))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldClientePassword, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                            .addComponent(jTextFieldClienteUsuario)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInicioSesionLayout.createSequentialGroup()
                        .addComponent(jButtonCrearUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonLogIn)))
                .addContainerGap())
        );
        jPanelInicioSesionLayout.setVerticalGroup(
            jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInicioSesionLayout.createSequentialGroup()
                .addGap(162, 162, 162)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClienteUusuario)
                    .addComponent(jTextFieldClienteUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClientePassword)
                    .addComponent(jTextFieldClientePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLogIn)
                    .addComponent(jButtonCrearUsuario))
                .addContainerGap())
        );

        jTabbedPanelPrincipal.addTab("Inicio Sesion", jPanelInicioSesion);

        jTableHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableHistorial);

        javax.swing.GroupLayout jPanelHistorialLayout = new javax.swing.GroupLayout(jPanelHistorial);
        jPanelHistorial.setLayout(jPanelHistorialLayout);
        jPanelHistorialLayout.setHorizontalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelHistorialLayout.setVerticalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPanelPrincipal.addTab("Historial", jPanelHistorial);

        jTabbedAdministracion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedAdministracionStateChanged(evt);
            }
        });

        jTableDBHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTableDBHistorial);

        javax.swing.GroupLayout jPanelDBHistorialLayout = new javax.swing.GroupLayout(jPanelDBHistorial);
        jPanelDBHistorial.setLayout(jPanelDBHistorialLayout);
        jPanelDBHistorialLayout.setHorizontalGroup(
            jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addGap(212, 212, 212))
        );
        jPanelDBHistorialLayout.setVerticalGroup(
            jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        jTabbedAdministracion.addTab("Historial", jPanelDBHistorial);

        jTableDBClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableDBClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDBClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDBClientes);

        jLabelClienteID.setText("ID");

        jLabelClienteNombre.setText("Usuario");

        jLabelClienteContraseña.setText("Contraseña");

        jSpinnerClienteID.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinnerClienteID.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerClienteIDStateChanged(evt);
            }
        });

        jButtonClienteBorrar.setText("Borrar");
        jButtonClienteBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClienteBorrarActionPerformed(evt);
            }
        });

        jButtonClienteActualizar.setText("Actualizar");
        jButtonClienteActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClienteActualizarActionPerformed(evt);
            }
        });

        jButtonClienteAgregar.setText("Agregar");
        jButtonClienteAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClienteAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDBClientesLayout = new javax.swing.GroupLayout(jPanelDBClientes);
        jPanelDBClientes.setLayout(jPanelDBClientesLayout);
        jPanelDBClientesLayout.setHorizontalGroup(
            jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerClienteID)
                            .addComponent(jTextFieldClienteNombre)
                            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                                .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelClienteID, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelClienteContraseña)
                                    .addComponent(jLabelClienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextFieldClienteContraseña))
                        .addContainerGap())
                    .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonClienteAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonClienteBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonClienteActualizar)))
                        .addContainerGap(67, Short.MAX_VALUE))))
        );
        jPanelDBClientesLayout.setVerticalGroup(
            jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabelClienteID)
                .addGap(18, 18, 18)
                .addComponent(jSpinnerClienteID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelClienteNombre)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldClienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelClienteContraseña)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldClienteContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonClienteAgregar)
                .addGap(18, 18, 18)
                .addComponent(jButtonClienteActualizar)
                .addGap(18, 18, 18)
                .addComponent(jButtonClienteBorrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedAdministracion.addTab("Clientes", jPanelDBClientes);

        jTableDBCanciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTableDBCanciones);

        jLabelCancionesID.setText("ID");

        jLabelCancionNombre.setText("Nombre");

        jSpinnerCancionID.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinnerCancionID.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerCancionIDStateChanged(evt);
            }
        });

        jButtonCancionAgregar.setText("Agregar");
        jButtonCancionAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancionAgregarActionPerformed(evt);
            }
        });

        jButtonCancionActualizar.setText("Actualizar");
        jButtonCancionActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancionActualizarActionPerformed(evt);
            }
        });

        jButtonCancionesBorrar.setText("Borrar");
        jButtonCancionesBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancionesBorrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDBCancionesLayout = new javax.swing.GroupLayout(jPanelDBCanciones);
        jPanelDBCanciones.setLayout(jPanelDBCancionesLayout);
        jPanelDBCancionesLayout.setHorizontalGroup(
            jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerCancionID, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                                .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCancionesID, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelCancionNombre))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextFieldCancionNombre))
                        .addContainerGap())
                    .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonCancionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonCancionesBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonCancionActualizar)))
                        .addContainerGap(68, Short.MAX_VALUE))))
        );
        jPanelDBCancionesLayout.setVerticalGroup(
            jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabelCancionesID)
                .addGap(18, 18, 18)
                .addComponent(jSpinnerCancionID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelCancionNombre)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldCancionNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(jButtonCancionAgregar)
                .addGap(18, 18, 18)
                .addComponent(jButtonCancionActualizar)
                .addGap(18, 18, 18)
                .addComponent(jButtonCancionesBorrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedAdministracion.addTab("Canciones", jPanelDBCanciones);

        jTabbedPanelPrincipal.addTab("Administracion", jTabbedAdministracion);

        jLabelMariaDBUusuario.setText("Usuario");

        jLabelMariaDBDPassword.setText("Contraseña");

        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.setToolTipText("Cambia los datos de conexion a los introducidos");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jButtonDefault.setText("Por defecto");
        jButtonDefault.setToolTipText("Establece la conexion a la conexion por defecto de la aplicacion");
        jButtonDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDefaultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelGestorConexionLayout = new javax.swing.GroupLayout(jPanelGestorConexion);
        jPanelGestorConexion.setLayout(jPanelGestorConexionLayout);
        jPanelGestorConexionLayout.setHorizontalGroup(
            jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestorConexionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestorConexionLayout.createSequentialGroup()
                        .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelMariaDBDPassword)
                            .addComponent(jLabelMariaDBUusuario))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldMariaDBPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                            .addComponent(jTextFieldMariaDBUsuario)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGestorConexionLayout.createSequentialGroup()
                        .addComponent(jButtonDefault)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAceptar)))
                .addContainerGap())
        );
        jPanelGestorConexionLayout.setVerticalGroup(
            jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestorConexionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMariaDBUusuario)
                    .addComponent(jTextFieldMariaDBUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMariaDBDPassword)
                    .addComponent(jTextFieldMariaDBPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 398, Short.MAX_VALUE)
                .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAceptar)
                    .addComponent(jButtonDefault))
                .addContainerGap())
        );

        jTabbedPanelPrincipal.addTab("Ajustes de conexion", jPanelGestorConexion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanelPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanelPrincipal)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPanelPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanelPrincipalStateChanged
        int indexTab = jTabbedPanelPrincipal.getSelectedIndex();
        int conexionTab = 2;
        System.out.println(indexTab);
        if (indexTab == conexionTab) {
            inicializarTablaClientes();
            jTextFieldMariaDBUsuario.setText(dbUser);
            jTextFieldMariaDBPassword.setText(dbPassword);
        }
    }//GEN-LAST:event_jTabbedPanelPrincipalStateChanged

    private void jButtonLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogInActionPerformed
        String usuario = jTextFieldClienteUsuario.getText();
        String password = jTextFieldClientePassword.getText();
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        Cliente cliente = clienteJpaC.findCliente(usuario, password);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.cliente = cliente;
        }
    }//GEN-LAST:event_jButtonLogInActionPerformed

    private void jButtonCrearUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearUsuarioActionPerformed
        String usuario = jTextFieldClienteUsuario.getText();
        String password = jTextFieldClientePassword.getText();
        if (!usuario.equals("") && !password.equals("")) {
            ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
            clienteJpaC.create(new Cliente(usuario, password));
        } else {
            JOptionPane.showMessageDialog(this, "Uno de los campos esta vacio", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCrearUsuarioActionPerformed

    private void jButtonDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDefaultActionPerformed
        dbUser = "root";
        dbPassword = "";
        jTextFieldMariaDBUsuario.setText(dbUser);
        jTextFieldMariaDBPassword.setText(dbPassword);
        emf = setPropiedades();
    }//GEN-LAST:event_jButtonDefaultActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        dbUser = jTextFieldMariaDBUsuario.getText();
        dbPassword = jTextFieldMariaDBPassword.getText();
        emf = setPropiedades();
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jTabbedAdministracionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedAdministracionStateChanged

    }//GEN-LAST:event_jTabbedAdministracionStateChanged

    private void jTableDBClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDBClientesMouseClicked
        int col = jTableDBClientes.getSelectedColumn();
        int row = jTableDBClientes.getSelectedRow();
        if(evt.getClickCount() == 2) {
            int ID = (int) jTableDBClientes.getValueAt(row, 0);
            String nombre = (String) jTableDBClientes.getValueAt(row, 1);
            String contraseña = (String) jTableDBClientes.getValueAt(row, 2);
            jSpinnerClienteID.setValue(ID);
            jTextFieldClienteNombre.setText(nombre);
            jTextFieldClienteContraseña.setText(contraseña);
        }
    }//GEN-LAST:event_jTableDBClientesMouseClicked

    private void jSpinnerClienteIDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerClienteIDStateChanged
        int id = (int)jSpinnerClienteID.getValue();
        if (id < 1) {
            jSpinnerClienteID.setValue(1);
        }
    }//GEN-LAST:event_jSpinnerClienteIDStateChanged

    private void jSpinnerCancionIDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerCancionIDStateChanged
        int id = (int)jSpinnerCancionID.getValue();
        if (id < 1) {
            jSpinnerCancionID.setValue(1);
        }
    }//GEN-LAST:event_jSpinnerCancionIDStateChanged

    private void jButtonCancionAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancionAgregarActionPerformed
        CancionJpaController cancionJPAC = new CancionJpaController(emf);
        int id = (int) jSpinnerCancionID.getValue();
        String nombre = jTextFieldCancionNombre.getText();
        if (cancionJPAC.findCancion(id) == null) {
            cancionJPAC.create(new Cancion(id, nombre));
            inicializarTablaCanciones();
        } else {
            JOptionPane.showMessageDialog(this, "Esta cancion ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCancionAgregarActionPerformed

    private void jButtonCancionesBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancionesBorrarActionPerformed
        CancionJpaController cancionJPAC = new CancionJpaController(emf);
        int id = (int) jSpinnerCancionID.getValue();
        if (cancionJPAC.findCancion(id) == null) {
            JOptionPane.showMessageDialog(this, "Esta cancion no existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            cancionJPAC.delete(id);
            inicializarTablaCanciones();
        }
    }//GEN-LAST:event_jButtonCancionesBorrarActionPerformed

    private void jButtonCancionActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancionActualizarActionPerformed
        CancionJpaController cancionJPAC = new CancionJpaController(emf);
        int id = (int) jSpinnerCancionID.getValue();
        String nombre = jTextFieldCancionNombre.getText();
        if (cancionJPAC.findCancion(id) == null) {
            JOptionPane.showMessageDialog(this, "Esta cancion no existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            cancionJPAC.update(new Cancion(id, nombre));
            inicializarTablaCanciones();
        }
    }//GEN-LAST:event_jButtonCancionActualizarActionPerformed

    private void jButtonClienteBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClienteBorrarActionPerformed
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
    }//GEN-LAST:event_jButtonClienteBorrarActionPerformed

    private void jButtonClienteActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClienteActualizarActionPerformed
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
    }//GEN-LAST:event_jButtonClienteActualizarActionPerformed

    private void jButtonClienteAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClienteAgregarActionPerformed
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        int id = (int) jSpinnerCancionID.getValue();
        String nombre = jTextFieldCancionNombre.getText();
        if (clienteJpaC.findCliente(id) == null) {
            JOptionPane.showMessageDialog(this, "Esta cancion no existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            clienteJpaC.update(new Cancion(id, nombre));
            inicializarTablaCanciones();
        }
    }//GEN-LAST:event_jButtonClienteAgregarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Launcher().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancionActualizar;
    private javax.swing.JButton jButtonCancionAgregar;
    private javax.swing.JButton jButtonCancionesBorrar;
    private javax.swing.JButton jButtonClienteActualizar;
    private javax.swing.JButton jButtonClienteAgregar;
    private javax.swing.JButton jButtonClienteBorrar;
    private javax.swing.JButton jButtonCrearUsuario;
    private javax.swing.JButton jButtonDefault;
    private javax.swing.JButton jButtonLogIn;
    private javax.swing.JLabel jLabelCancionNombre;
    private javax.swing.JLabel jLabelCancionesID;
    private javax.swing.JLabel jLabelClienteContraseña;
    private javax.swing.JLabel jLabelClienteID;
    private javax.swing.JLabel jLabelClienteNombre;
    private javax.swing.JLabel jLabelClientePassword;
    private javax.swing.JLabel jLabelClienteUusuario;
    private javax.swing.JLabel jLabelMariaDBDPassword;
    private javax.swing.JLabel jLabelMariaDBUusuario;
    private javax.swing.JPanel jPanelDBCanciones;
    private javax.swing.JPanel jPanelDBClientes;
    private javax.swing.JPanel jPanelDBHistorial;
    private javax.swing.JPanel jPanelGestorConexion;
    private javax.swing.JPanel jPanelHistorial;
    private javax.swing.JPanel jPanelInicioSesion;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinnerCancionID;
    private javax.swing.JSpinner jSpinnerClienteID;
    private javax.swing.JTabbedPane jTabbedAdministracion;
    private javax.swing.JTabbedPane jTabbedPanelPrincipal;
    private javax.swing.JTable jTableDBCanciones;
    private javax.swing.JTable jTableDBClientes;
    private javax.swing.JTable jTableDBHistorial;
    private javax.swing.JTable jTableHistorial;
    private javax.swing.JTextField jTextFieldCancionNombre;
    private javax.swing.JTextField jTextFieldClienteContraseña;
    private javax.swing.JTextField jTextFieldClienteNombre;
    private javax.swing.JTextField jTextFieldClientePassword;
    private javax.swing.JTextField jTextFieldClienteUsuario;
    private javax.swing.JTextField jTextFieldMariaDBPassword;
    private javax.swing.JTextField jTextFieldMariaDBUsuario;
    // End of variables declaration//GEN-END:variables
}
