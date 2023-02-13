/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Launcher;

import Clases.Cancion;
import Clases.Cliente;
import Clases.ClienteCancion;
import Data.Data;
import JPAController.CancionJpaController;
import JPAController.ClienteCancionJpaController;
import JPAController.ClienteJpaController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;
import org.hibernate.service.spi.ServiceException;

/**
 *
 * @author Ryuka
 */
public class Launcher extends javax.swing.JFrame {
    
    private static EntityManagerFactory emf;
    // String que guarda los ajustes de la base de datos.
    private String dataBase = "karaokebraystyven";
    private String dbUser;
    private String dbPassword;
    // Booleano que verifica que la conexion ex exitosa.
    private boolean error = true;
    private boolean testDat = false;
    // String con los nombres de las filas de cada tabla.
    private final String[] ordenRepeticiones = new String[]{"Cancion", "Usuario", "Repeticiones", "Fecha"};
    private final String[] ordenFecha = new String[]{"Cancion","Repeticiones", "Fecha"};
    private final String[] clientesTable = new String[]{"ID", "Usuario", "Contraseña"};
    private final String[] cancionTable = new String[]{"ID", "Cancion"};
    private final String[] historialTable = new String[]{"ID", "Cliente", "Cancion", "Fecha"};
    List<ClienteCancion> cc = null;
    
    /**
     * Creates new form Launcher
     */
    public Launcher() {
        initComponents();
        inicializarEMF();
    }
    
    /**
     * Carga la configuracion desde los datos guardados.
     * En caso de no haber configuracion o tener un error,
     * pregunta por los datos y los escribe.
     */
    private void loadOptions() {
        File archivo = new File("SaveOptions.dat");
        boolean existe = archivo.exists();
        if(existe && error) {
            try {
                BufferedReader brDatos = new BufferedReader(new FileReader(archivo));
                dbUser =brDatos.readLine();
                dbPassword =brDatos.readLine();
                if (dbUser == null) {
                    dbUser = "";
                }
                if (dbPassword == null) {
                    dbPassword = "";
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el archivo de opciones.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ocurrio un error al leer las opciones", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            dbUser = JOptionPane.showInputDialog(this, "Configuracion inicial.\nIntroduce el usuario de la base de datos.");
            if (dbUser == null) {
                dbUser = "";
            }
            dbPassword = JOptionPane.showInputDialog(this, "Configuracion inicial.\nIntroduce la contraseña de la base de datos.");
            if (dbPassword == null) {
                dbPassword = "";
            }
            saveOptions();
            int respuesta = JOptionPane.showConfirmDialog(this, "Configuracion inicial.\n¿Añadir datos de prueba?.", "Datos de prueba", JOptionPane.OK_OPTION);
            if (respuesta == 0) {
                testDat = true;
            }
        }
    }
    
    /**
     * Guarda los datos de inicio de sesion en la base de datos a fichero.
     */
    private void saveOptions() {
        File archivo = new File("SaveOptions.dat");
        try {
            try (BufferedWriter bwdatos = new BufferedWriter(new FileWriter(archivo))) {
                bwdatos.write(dbUser);
                bwdatos.write("\n");
                bwdatos.write(dbPassword);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error al guardar las opciones", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Inicializa el EMF al empezar la aplicacion.
     */
    private void inicializarEMF() {
        // Carga los ajustes de conexion guardados.
        loadOptions();
        // Crea el EMF con los datos de conexion.
        emf = setEMF();
        if (emf != null) {
            if (testDat) {
                Data testData = new Data();
                testData.datosDePrueba(emf);
                inicializarComponentes();
                testDat = false;
            }
            inicializarComponentes();
        } else {
            // En caso de dar error la conexion, vuelve a repetir toda la operacion.
            inicializarEMF();
        }
    }
    
    /**
     * Se encarga de llamar todos los metodos inicializadores.
     */
    private void inicializarComponentes() {
        // Inicializa el combobox
        inicializarBoxModelCancion();
        // Inicializa las tablas con los datos.
        inicializarTablaPrincipalOrdenRepeticiones();
        inicializarTablaHistorial();
        inicializarTablaClientes();
        inicializarTablaCanciones();
        SimpleDateFormat model = new SimpleDateFormat("yyyy-MM-dd");
        jSpinnerFecha.setEditor(new JSpinner.DateEditor(jSpinnerFecha, model.toPattern()));
        jSpinnerHistorialFecha.setEditor(new JSpinner.DateEditor(jSpinnerHistorialFecha, model.toPattern()));
    }
    
    /**
     * Inicializa el combobox de inicio de sesion .
     */
    private void inicializarBoxModelCancion() {
        DefaultComboBoxModel dcb = new DefaultComboBoxModel();
        CancionJpaController cancionJpaC = new CancionJpaController(emf);
        List<Cancion> cancion = cancionJpaC.findCancionEntities();
        dcb.addAll(cancion);
        jComboBox1.setModel(dcb);
    }
    
    /**
     * Realiza una consulta e imprime los datos en la tabla.
     * La consulta son todas las canciones con los clientes que las cantaron,
     * cuantas veces y el dia que lo hicieron.
     */
    private void inicializarTablaPrincipalOrdenRepeticiones() {
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
        dtm.setColumnIdentifiers(ordenRepeticiones);
        jTableHistorial.setModel(dtm);
        List<Object[]> clienteCancion = ccJpaC.findClienteCancionCountDesc();
        for (Object[] cc : clienteCancion) {
            //Pasa la informacion a la pantalla principal.
            DefaultTableModel dtm2 = (DefaultTableModel)jTableHistorial.getModel();
            dtm2.addRow(cc);
        }
    }
    
    /**
     * Realiza una consulta e imprime los datos en la tabla.
     * La consulta son las canciones repetidas en un dia especifico
     * con el numero de veces que se repiten.
     */
    private void inicializarTablaPrincipalOrdenFecha() {
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
        dtm.setColumnIdentifiers(ordenFecha);
        jTableHistorial.setModel(dtm);
        Date fecha = (Date) jSpinnerFecha.getValue();
        System.out.println(fecha);
        List<Object[]> clienteCancion = ccJpaC.findClienteCancionWhereDate(fecha);
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
        cc = ccJpaC.findClienteCancionEntities();
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
     * Metodo que es invocado cuando un jRadioButton es seleccionado.
     * Verifica cual jRadioButton fue seleccionado y activa o desactiva
     * el jSpinnerFecha.
     */
    private void grupoEventoRes() {
        if (jRadioButton1.isSelected()){
            inicializarTablaPrincipalOrdenRepeticiones();
            jSpinnerFecha.setEnabled(false);
        }
        if (jRadioButton2.isSelected()){
            inicializarTablaPrincipalOrdenFecha();
            jSpinnerFecha.setEnabled(true);
        }
    }
    
    /**
     * Restablece el EMF con los nuevos datos de usuario y contraseña.
     * @return 
     */
    public EntityManagerFactory setEMF() {
        EntityManagerFactory emfLocal = null;
        try {
            Properties props = new Properties();
            props.setProperty("javax.persistence.jdbc.url", "jdbc:mariadb://localhost:3306/" + dataBase + "?createDatabaseIfNotExist=true");
            props.setProperty("javax.persistence.jdbc.user", dbUser);
            props.setProperty("javax.persistence.jdbc.password", dbPassword);
            emfLocal = Persistence.createEntityManagerFactory("persistencia", props);
            error = true;
        } catch (ServiceException se) {
            error = false;
            JOptionPane.showMessageDialog(this, "Revise el usuario y la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return emfLocal;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupTipoOrden = new javax.swing.ButtonGroup();
        jTabbedPanelPrincipal = new javax.swing.JTabbedPane();
        jPanelInicioSesion = new javax.swing.JPanel();
        jTextFieldClienteUsuario = new javax.swing.JTextField();
        jLabelClienteUusuario = new javax.swing.JLabel();
        jLabelClientePassword = new javax.swing.JLabel();
        jTextFieldClientePassword = new javax.swing.JTextField();
        jButtonLogIn = new javax.swing.JButton();
        jButtonCrearUsuario = new javax.swing.JButton();
        jLabelCancion = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanelHistorial = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHistorial = new javax.swing.JTable();
        jSpinnerFecha = new javax.swing.JSpinner();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTabbedAdministracion = new javax.swing.JTabbedPane();
        jPanelDBHistorial = new javax.swing.JPanel();
        jButtonHistorialAgregar = new javax.swing.JButton();
        jButtonHistorialActualizar = new javax.swing.JButton();
        jButtonHistorialBorrar = new javax.swing.JButton();
        jLabelHistorialID = new javax.swing.JLabel();
        jLabelHistorialCliente = new javax.swing.JLabel();
        jLabelHistorialCancion = new javax.swing.JLabel();
        jLabelHistorialFecha = new javax.swing.JLabel();
        jSpinnerHistorialFecha = new javax.swing.JSpinner();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableDBHistorial = new javax.swing.JTable();
        jTextFieldHistorialCliente = new javax.swing.JTextField();
        jTextFieldHistorialCancion = new javax.swing.JTextField();
        jSpinnerHistorialID = new javax.swing.JSpinner();
        jPanelDBClientes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDBClientes = new javax.swing.JTable();
        jLabelClienteID = new javax.swing.JLabel();
        jLabelClienteNombre = new javax.swing.JLabel();
        jLabelClienteContraseña = new javax.swing.JLabel();
        jTextFieldClienteNombre = new javax.swing.JTextField();
        jTextFieldClienteContraseña = new javax.swing.JTextField();
        jButtonClienteBorrar = new javax.swing.JButton();
        jButtonClienteActualizar = new javax.swing.JButton();
        jButtonClienteAgregar = new javax.swing.JButton();
        jSpinnerClienteID = new javax.swing.JSpinner();
        jPanelDBCanciones = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableDBCanciones = new javax.swing.JTable();
        jLabelCancionesID = new javax.swing.JLabel();
        jLabelCancionNombre = new javax.swing.JLabel();
        jTextFieldCancionNombre = new javax.swing.JTextField();
        jButtonCancionAgregar = new javax.swing.JButton();
        jButtonCancionActualizar = new javax.swing.JButton();
        jButtonCancionesBorrar = new javax.swing.JButton();
        jSpinnerCancionID = new javax.swing.JSpinner();
        jPanelGestorConexion = new javax.swing.JPanel();
        jLabelMariaDBUusuario = new javax.swing.JLabel();
        jLabelMariaDBDPassword = new javax.swing.JLabel();
        jTextFieldMariaDBPassword = new javax.swing.JTextField();
        jTextFieldMariaDBUsuario = new javax.swing.JTextField();
        jButtonAceptar = new javax.swing.JButton();
        jButtonDefault = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPanelPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPanelPrincipalStateChanged(evt);
            }
        });

        jTextFieldClienteUsuario.setToolTipText("Nombre de usuario.");

        jLabelClienteUusuario.setText("Usuario");

        jLabelClientePassword.setText("Contraseña");

        jTextFieldClientePassword.setToolTipText("Contraseña de usuario.");

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

        jLabelCancion.setText("Cancion");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanelInicioSesionLayout = new javax.swing.GroupLayout(jPanelInicioSesion);
        jPanelInicioSesion.setLayout(jPanelInicioSesionLayout);
        jPanelInicioSesionLayout.setHorizontalGroup(
            jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInicioSesionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInicioSesionLayout.createSequentialGroup()
                        .addComponent(jButtonCrearUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonLogIn))
                    .addGroup(jPanelInicioSesionLayout.createSequentialGroup()
                        .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClientePassword)
                            .addComponent(jLabelClienteUusuario)
                            .addComponent(jLabelCancion))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldClientePassword, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                            .addComponent(jTextFieldClienteUsuario)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(jLabelCancion)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClientePassword)
                    .addComponent(jTextFieldClientePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLogIn)
                    .addComponent(jButtonCrearUsuario))
                .addContainerGap())
        );

        jTabbedPanelPrincipal.addTab("Seleccionar Cancion", jPanelInicioSesion);

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

        jSpinnerFecha.setModel(new javax.swing.SpinnerDateModel());
        jSpinnerFecha.setEnabled(false);
        jSpinnerFecha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerFechaStateChanged(evt);
            }
        });

        buttonGroupTipoOrden.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Repeticiones");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroupTipoOrden.add(jRadioButton2);
        jRadioButton2.setText("Canciones mas cantadas en:");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelHistorialLayout = new javax.swing.GroupLayout(jPanelHistorial);
        jPanelHistorial.setLayout(jPanelHistorialLayout);
        jPanelHistorialLayout.setHorizontalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSpinnerFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelHistorialLayout.setVerticalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHistorialLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jRadioButton1)
                .addGap(110, 110, 110)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPanelPrincipal.addTab("Historial", jPanelHistorial);

        jTabbedAdministracion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedAdministracionStateChanged(evt);
            }
        });

        jButtonHistorialAgregar.setText("Agregar");
        jButtonHistorialAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHistorialAgregarActionPerformed(evt);
            }
        });

        jButtonHistorialActualizar.setText("Actualizar");
        jButtonHistorialActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHistorialActualizarActionPerformed(evt);
            }
        });

        jButtonHistorialBorrar.setText("Borrar");
        jButtonHistorialBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHistorialBorrarActionPerformed(evt);
            }
        });

        jLabelHistorialID.setText("ID");

        jLabelHistorialCliente.setText("Cliente");

        jLabelHistorialCancion.setText("Cancion");

        jLabelHistorialFecha.setText("Fecha");

        jSpinnerHistorialFecha.setModel(new javax.swing.SpinnerDateModel());

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
        jTableDBHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDBHistorialMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableDBHistorial);

        jSpinnerHistorialID.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        javax.swing.GroupLayout jPanelDBHistorialLayout = new javax.swing.GroupLayout(jPanelDBHistorial);
        jPanelDBHistorial.setLayout(jPanelDBHistorialLayout);
        jPanelDBHistorialLayout.setHorizontalGroup(
            jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBHistorialLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBHistorialLayout.createSequentialGroup()
                        .addGroup(jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonHistorialAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonHistorialBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonHistorialActualizar)))
                        .addGap(65, 65, 65))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBHistorialLayout.createSequentialGroup()
                        .addGroup(jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelHistorialID)
                            .addComponent(jLabelHistorialCliente)
                            .addComponent(jLabelHistorialCancion)
                            .addComponent(jLabelHistorialFecha)
                            .addComponent(jSpinnerHistorialFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBHistorialLayout.createSequentialGroup()
                        .addGroup(jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSpinnerHistorialID, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldHistorialCancion, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldHistorialCliente))
                        .addContainerGap())))
        );
        jPanelDBHistorialLayout.setVerticalGroup(
            jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDBHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(jPanelDBHistorialLayout.createSequentialGroup()
                        .addComponent(jLabelHistorialID)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerHistorialID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHistorialCliente)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldHistorialCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHistorialCancion)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldHistorialCancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHistorialFecha)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerHistorialFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonHistorialAgregar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonHistorialActualizar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonHistorialBorrar)))
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

        jSpinnerClienteID.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        javax.swing.GroupLayout jPanelDBClientesLayout = new javax.swing.GroupLayout(jPanelDBClientes);
        jPanelDBClientes.setLayout(jPanelDBClientesLayout);
        jPanelDBClientesLayout.setHorizontalGroup(
            jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                        .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonClienteAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButtonClienteBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonClienteActualizar))))
                            .addGroup(jPanelDBClientesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelClienteID, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelClienteContraseña)
                                    .addComponent(jLabelClienteNombre))))
                        .addGap(70, 70, 70))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBClientesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldClienteNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldClienteContraseña, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinnerClienteID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanelDBClientesLayout.setVerticalGroup(
            jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDBClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelDBClientesLayout.createSequentialGroup()
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonClienteAgregar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClienteActualizar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClienteBorrar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
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
        jTableDBCanciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDBCancionesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableDBCanciones);

        jLabelCancionesID.setText("ID");

        jLabelCancionNombre.setText("Nombre");

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

        jSpinnerCancionID.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        javax.swing.GroupLayout jPanelDBCancionesLayout = new javax.swing.GroupLayout(jPanelDBCanciones);
        jPanelDBCanciones.setLayout(jPanelDBCancionesLayout);
        jPanelDBCancionesLayout.setHorizontalGroup(
            jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonCancionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonCancionesBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonCancionActualizar))))
                    .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCancionNombre)
                            .addComponent(jTextFieldCancionNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCancionesID, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBCancionesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerCancionID, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelDBCancionesLayout.setVerticalGroup(
            jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDBCancionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDBCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelDBCancionesLayout.createSequentialGroup()
                        .addComponent(jLabelCancionesID)
                        .addGap(19, 19, 19)
                        .addComponent(jSpinnerCancionID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelCancionNombre)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCancionNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancionAgregar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancionActualizar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancionesBorrar))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
                .addContainerGap())
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

        jButton1.setText("Cargar datos de prueba");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                            .addComponent(jTextFieldMariaDBPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                            .addComponent(jTextFieldMariaDBUsuario)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGestorConexionLayout.createSequentialGroup()
                        .addComponent(jButtonDefault)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAceptar))
                    .addGroup(jPanelGestorConexionLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
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

    /**
     * Evento que carga los datos de la conexion de la base de datos en pantalla
     * cuando es seleccionada la pestaña Ajustes de conexion.
     * @param evt 
     */
    private void jTabbedPanelPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanelPrincipalStateChanged
        int indexTab = jTabbedPanelPrincipal.getSelectedIndex();
        int conexionTab = 3;
        System.out.println(indexTab);
        if (indexTab == conexionTab) {
            inicializarTablaClientes();
            jTextFieldMariaDBUsuario.setText(dbUser);
            jTextFieldMariaDBPassword.setText(dbPassword);
        }
    }//GEN-LAST:event_jTabbedPanelPrincipalStateChanged

    /**
     * Boton que se encarga de insertar el cliente y la cancion seleccionada en 
     * el historial (La tabla ClienteCancion).
     * @param evt 
     */
    private void jButtonLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogInActionPerformed
        String usuario = jTextFieldClienteUsuario.getText();
        String password = jTextFieldClientePassword.getText();
        if (jComboBox1.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No se ha seleccionado ninguna cancion.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
            Cliente cliente = clienteJpaC.findCliente(usuario, password);
            Date fecha = new Date();
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
                ccJpaC.create(new ClienteCancion((Cancion)jComboBox1.getSelectedItem(), cliente, fecha));
                inicializarTablaPrincipalOrdenRepeticiones();
            }
        }
    }//GEN-LAST:event_jButtonLogInActionPerformed

    /**
     * Boton que se encarga de crear un nuevo usuario.
     * Este boton esta pensado para ser usado por clientes.
     * @param evt 
     */
    private void jButtonCrearUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearUsuarioActionPerformed
        String usuario = jTextFieldClienteUsuario.getText();
        String password = jTextFieldClientePassword.getText();
        if (!usuario.equals("") && !password.equals("")) {
            ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
            Cliente cliente = clienteJpaC.findCliente(usuario, password);
            if (cliente != null) {
                JOptionPane.showMessageDialog(this, "Usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                clienteJpaC.create(new Cliente(usuario, password));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Uno de los campos esta vacio", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCrearUsuarioActionPerformed

    /**
     * Restablece los ajustes de conexion a ajustes predeterminados.
     * @param evt 
     */
    private void jButtonDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDefaultActionPerformed
        dbUser = "root";
        dbPassword = "1234";
        jTextFieldMariaDBUsuario.setText(dbUser);
        jTextFieldMariaDBPassword.setText(dbPassword);
        saveOptions();
        emf = setEMF();
    }//GEN-LAST:event_jButtonDefaultActionPerformed

    /**
     * Boton para guardar los nuevos ajustes de conexion.
     * @param evt 
     */
    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        dbUser = jTextFieldMariaDBUsuario.getText();
        dbPassword = jTextFieldMariaDBPassword.getText();
        saveOptions();
        emf = setEMF();
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    
    private void jTabbedAdministracionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedAdministracionStateChanged

    }//GEN-LAST:event_jTabbedAdministracionStateChanged

    /**
     * Evento que recoge que columna se selecciona y carga los datos en las casillas para su posterios uso.
     * @param evt 
     */
    private void jTableDBClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDBClientesMouseClicked
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

    /**
     * INSERT Cancion.
     * @param evt 
     */
    private void jButtonCancionAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancionAgregarActionPerformed
        CancionJpaController cancionJPAC = new CancionJpaController(emf);
        String nombre = jTextFieldCancionNombre.getText();
        Cancion cancion = cancionJPAC.findCancion(nombre);
        if (cancion == null) {
            cancionJPAC.create(new Cancion(nombre));
            inicializarTablaCanciones();
        } else {
            JOptionPane.showMessageDialog(this, "Esta cancion ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            jSpinnerCancionID.setValue(cancion.getId());
        }
    }//GEN-LAST:event_jButtonCancionAgregarActionPerformed

    /**
     * DELETE Cancion.
     * @param evt 
     */
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

    /**
     * UPDATE Cancion.
     * @param evt 
     */
    private void jButtonCancionActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancionActualizarActionPerformed
        CancionJpaController cancionJPAC = new CancionJpaController(emf);
        int id = (int) jSpinnerCancionID.getValue();
        String nombre = jTextFieldCancionNombre.getText();
        if (cancionJPAC.findCancion(id) == null) {
            JOptionPane.showMessageDialog(this, "Esta cliente no existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            cancionJPAC.update(new Cancion(id, nombre));
            inicializarTablaCanciones();
        }
    }//GEN-LAST:event_jButtonCancionActualizarActionPerformed

    /**
     * DELETE Cliente.
     * @param evt 
     */
    private void jButtonClienteBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClienteBorrarActionPerformed
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        int id = (int) jSpinnerClienteID.getValue();
        if (clienteJpaC.findCliente(id) == null) {
            JOptionPane.showMessageDialog(this, "Este cliente no existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            clienteJpaC.delete(id);
            inicializarTablaClientes();
        }
    }//GEN-LAST:event_jButtonClienteBorrarActionPerformed

    /**
     * UPDATE Cliente.
     * @param evt 
     */
    private void jButtonClienteActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClienteActualizarActionPerformed
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        int id =(int) jSpinnerClienteID.getValue();
        String nombre = jTextFieldClienteNombre.getText();
        String contraseña = jTextFieldClienteContraseña.getText();
        if (clienteJpaC.findCliente(id) == null) {
            JOptionPane.showMessageDialog(this, "Este cliente no existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            clienteJpaC.update(new Cliente(id, nombre, contraseña));
            inicializarTablaClientes();
        }
    }//GEN-LAST:event_jButtonClienteActualizarActionPerformed

    /**
     * INSERT Cliente.
     * @param evt 
     */
    private void jButtonClienteAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClienteAgregarActionPerformed
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        String nombre = jTextFieldClienteNombre.getText();
        String contraseña = jTextFieldClienteContraseña.getText();
        Cliente cliente = clienteJpaC.findCliente(nombre);
        if (cliente != null) {
            JOptionPane.showMessageDialog(this, "Este cliente ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            jSpinnerClienteID.setValue(cliente.getId());
            jTextFieldClienteContraseña.setText(cliente.getContraseña());
        } else {
            clienteJpaC.create(new Cliente(nombre, contraseña));
            inicializarTablaClientes();
        }
    }//GEN-LAST:event_jButtonClienteAgregarActionPerformed

    /**
     * Evento que llama al metodo cada vez que cambia su estado.
     * @param evt 
     */
    private void jSpinnerFechaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerFechaStateChanged
        inicializarTablaPrincipalOrdenFecha();
    }//GEN-LAST:event_jSpinnerFechaStateChanged

    /**
     * Evento que llama al metodo cada vez que cambia su estado.
     * @param evt 
     */
    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        grupoEventoRes();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    /**
     * Evento que llama al metodo cada vez que cambia su estado.
     * @param evt 
     */
    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        grupoEventoRes();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    /**
     * Evento que recoge que columna se selecciona y carga los datos en las casillas para su posterios uso.
     * @param evt 
     */
    private void jTableDBCancionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDBCancionesMouseClicked
        int row = jTableDBCanciones.getSelectedRow();
        if(evt.getClickCount() == 2) {
            int ID = (int) jTableDBCanciones.getValueAt(row, 0);
            String nombre = (String) jTableDBCanciones.getValueAt(row, 1);
            jSpinnerCancionID.setValue(ID);
            jTextFieldCancionNombre.setText(nombre);
        }
    }//GEN-LAST:event_jTableDBCancionesMouseClicked

    /**
     * Cierra el EntityManagerFactory.
     * @param evt 
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        emf.close();
    }//GEN-LAST:event_formWindowClosing

    /**
     * 
     * @param evt 
     */
    private void jButtonHistorialAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHistorialAgregarActionPerformed
        CancionJpaController cancionJpaC = new CancionJpaController(emf);
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        int id = (int) jSpinnerHistorialID.getValue();
        Cliente cliente = clienteJpaC.findCliente(jTextFieldHistorialCliente.getText());
        Cancion cancion = cancionJpaC.findCancion(jTextFieldHistorialCancion.getText());
        Date fecha = (Date) jSpinnerHistorialFecha.getValue();
        if (ccJpaC.findClienteCancion(id) != null) {
            JOptionPane.showMessageDialog(this, "Este registro ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Este cliente no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (cancion == null) {
                JOptionPane.showMessageDialog(this, "Esta cancion no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ccJpaC.create(new ClienteCancion(cancion, cliente, fecha));
                inicializarTablaHistorial();
            }
        }
    }//GEN-LAST:event_jButtonHistorialAgregarActionPerformed

    private void jButtonHistorialActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHistorialActualizarActionPerformed
        CancionJpaController cancionJpaC = new CancionJpaController(emf);
        ClienteJpaController clienteJpaC = new ClienteJpaController(emf);
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        int id = (int) jSpinnerHistorialID.getValue();
        Cliente cliente = clienteJpaC.findCliente(jTextFieldHistorialCliente.getText());
        Cancion cancion = cancionJpaC.findCancion(jTextFieldHistorialCancion.getText());
        Date fecha = (Date) jSpinnerHistorialFecha.getValue();
        if (ccJpaC.findClienteCancion(id) == null) {
            JOptionPane.showMessageDialog(this, "Este registro no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Este cliente no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (cancion == null) {
                JOptionPane.showMessageDialog(this, "Esta cancion no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ccJpaC.update(new ClienteCancion(id, cancion, cliente, fecha));
                inicializarTablaHistorial();
            }
        }
    }//GEN-LAST:event_jButtonHistorialActualizarActionPerformed

    private void jButtonHistorialBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHistorialBorrarActionPerformed
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        int id = (int) jSpinnerHistorialID.getValue();
        if (ccJpaC.findClienteCancion(id) == null) {
            JOptionPane.showMessageDialog(this, "Este registro no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            ccJpaC.delete(id);
            inicializarTablaHistorial();
        }
    }//GEN-LAST:event_jButtonHistorialBorrarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Data testData = new Data();
        testData.datosDePrueba(emf);
        inicializarComponentes();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTableDBHistorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDBHistorialMouseClicked
        int row = jTableDBHistorial.getSelectedRow();
        if(evt.getClickCount() == 2) {
            int ID = (int) jTableDBHistorial.getValueAt(row, 0);
            Cliente cliente = cc.get(row).getCliente();
            Cancion cancion = cc.get(row).getCancion();
            Date fecha = (Date) jTableDBHistorial.getValueAt(row, 3);
            jSpinnerHistorialID.setValue(ID);
            jTextFieldHistorialCliente.setText(cliente.getNombre());
            jTextFieldHistorialCancion.setText(cancion.getNombre());
            jSpinnerHistorialFecha.setValue(fecha);
        }
    }//GEN-LAST:event_jTableDBHistorialMouseClicked

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
    private javax.swing.ButtonGroup buttonGroupTipoOrden;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancionActualizar;
    private javax.swing.JButton jButtonCancionAgregar;
    private javax.swing.JButton jButtonCancionesBorrar;
    private javax.swing.JButton jButtonClienteActualizar;
    private javax.swing.JButton jButtonClienteAgregar;
    private javax.swing.JButton jButtonClienteBorrar;
    private javax.swing.JButton jButtonCrearUsuario;
    private javax.swing.JButton jButtonDefault;
    private javax.swing.JButton jButtonHistorialActualizar;
    private javax.swing.JButton jButtonHistorialAgregar;
    private javax.swing.JButton jButtonHistorialBorrar;
    private javax.swing.JButton jButtonLogIn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabelCancion;
    private javax.swing.JLabel jLabelCancionNombre;
    private javax.swing.JLabel jLabelCancionesID;
    private javax.swing.JLabel jLabelClienteContraseña;
    private javax.swing.JLabel jLabelClienteID;
    private javax.swing.JLabel jLabelClienteNombre;
    private javax.swing.JLabel jLabelClientePassword;
    private javax.swing.JLabel jLabelClienteUusuario;
    private javax.swing.JLabel jLabelHistorialCancion;
    private javax.swing.JLabel jLabelHistorialCliente;
    private javax.swing.JLabel jLabelHistorialFecha;
    private javax.swing.JLabel jLabelHistorialID;
    private javax.swing.JLabel jLabelMariaDBDPassword;
    private javax.swing.JLabel jLabelMariaDBUusuario;
    private javax.swing.JPanel jPanelDBCanciones;
    private javax.swing.JPanel jPanelDBClientes;
    private javax.swing.JPanel jPanelDBHistorial;
    private javax.swing.JPanel jPanelGestorConexion;
    private javax.swing.JPanel jPanelHistorial;
    private javax.swing.JPanel jPanelInicioSesion;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinnerCancionID;
    private javax.swing.JSpinner jSpinnerClienteID;
    private javax.swing.JSpinner jSpinnerFecha;
    private javax.swing.JSpinner jSpinnerHistorialFecha;
    private javax.swing.JSpinner jSpinnerHistorialID;
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
    private javax.swing.JTextField jTextFieldHistorialCancion;
    private javax.swing.JTextField jTextFieldHistorialCliente;
    private javax.swing.JTextField jTextFieldMariaDBPassword;
    private javax.swing.JTextField jTextFieldMariaDBUsuario;
    // End of variables declaration//GEN-END:variables
}
