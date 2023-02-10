/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Launcher;

import Clases.Cliente;
import Clases.ClienteCancion;
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
    private String dbUser = "root";
    private String dbPassword = "";
    private Cliente cliente;
    private final String[] titulo = new String[]{"Cancion", "Cantante", "Repeticiones", "Fecha"};
    
    /**
     * Creates new form Launcher
     */
    public Launcher() {
        initComponents();
        emf = Persistence.createEntityManagerFactory("persistencia");
        inicializarTabla();
    }

    public EntityManagerFactory setPropiedades() {
        EntityManagerFactory emf = null;
        try {
            Properties props = new Properties();
            props.setProperty("javax.persistence.jdbc.driver", "org.mariadb.jdbc.Driver");
            props.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
            props.setProperty("javax.persistence.jdbc.url", "jdbc:mariadb://localhost:3306/karaoke?createDatabaseIfNotExist=true");
            props.setProperty("javax.persistence.jdbc.user", dbUser);
            props.setProperty("javax.persistence.jdbc.javax.persistence.jdbc.password", dbPassword);
            emf = Persistence.createEntityManagerFactory("persistencia", props);
        } catch (ServiceException se) {
            JOptionPane.showMessageDialog(this, "Revise el usuario y la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return emf;
    }
    
    /**
     * Inicializa las tablas al empezar la aplicacion.
     */
    private void inicializarTabla() {
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
        cargarDatos();
    }
    
    /**
     * Carga los datos en la tabla.
     */
    private void cargarDatos() {
        ClienteCancionJpaController ccJpaC = new ClienteCancionJpaController(emf);
        List<ClienteCancion> asd = ccJpaC.findClienteCancionEntities();
        List<Object[]> sssss = ccJpaC.findClienteCancionCountDesc();
        for (Object[] ssss : sssss) {
            addData(ssss);
        }
    }
    
    /**
     * Se encarga de escribir datos a la tabla general.
     * @param rowData 
     */
    public void addData(Object[] rowData) {
        //Pasa la informacion a la pantalla principal.
        DefaultTableModel dtm = (DefaultTableModel)jTableHistorial.getModel();
        dtm.addRow(rowData);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPanel = new javax.swing.JTabbedPane();
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
        jTabbedClientes = new javax.swing.JTabbedPane();
        jTabbedCanciones = new javax.swing.JTabbedPane();
        jPanelGestorConexion = new javax.swing.JPanel();
        jLabelMariaDBUusuario = new javax.swing.JLabel();
        jLabelMariaDBDPassword = new javax.swing.JLabel();
        jTextFieldMariaDBPassword = new javax.swing.JTextField();
        jTextFieldMariaDBUsuario = new javax.swing.JTextField();
        jButtonAceptar = new javax.swing.JButton();
        jButtonDefault = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPanelStateChanged(evt);
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
                            .addComponent(jTextFieldClientePassword, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
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
                .addContainerGap(103, Short.MAX_VALUE)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClienteUusuario)
                    .addComponent(jTextFieldClienteUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClientePassword)
                    .addComponent(jTextFieldClientePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(101, 101, 101)
                .addGroup(jPanelInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLogIn)
                    .addComponent(jButtonCrearUsuario))
                .addContainerGap())
        );

        jTabbedPanel.addTab("Inicio Sesion", jPanelInicioSesion);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );
        jPanelHistorialLayout.setVerticalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPanel.addTab("Historial", jPanelHistorial);

        jTabbedAdministracion.addTab("Usuarios", jTabbedClientes);
        jTabbedAdministracion.addTab("Canciones", jTabbedCanciones);

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
                            .addComponent(jTextFieldMariaDBPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addGroup(jPanelGestorConexionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAceptar)
                    .addComponent(jButtonDefault))
                .addContainerGap())
        );

        jTabbedAdministracion.addTab("Ajustes de conexion", jPanelGestorConexion);

        jTabbedPanel.addTab("Administracion", jTabbedAdministracion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanel)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanelStateChanged
        int indexTab = jTabbedPanel.getSelectedIndex();
        int conexionTab = 2;
        if (indexTab == conexionTab) {
            jTextFieldMariaDBUsuario.setText(dbUser);
            jTextFieldMariaDBPassword.setText(dbPassword);
        }
    }//GEN-LAST:event_jTabbedPanelStateChanged

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
    private javax.swing.JButton jButtonCrearUsuario;
    private javax.swing.JButton jButtonDefault;
    private javax.swing.JButton jButtonLogIn;
    private javax.swing.JLabel jLabelClientePassword;
    private javax.swing.JLabel jLabelClienteUusuario;
    private javax.swing.JLabel jLabelMariaDBDPassword;
    private javax.swing.JLabel jLabelMariaDBUusuario;
    private javax.swing.JPanel jPanelGestorConexion;
    private javax.swing.JPanel jPanelHistorial;
    private javax.swing.JPanel jPanelInicioSesion;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedAdministracion;
    private javax.swing.JTabbedPane jTabbedCanciones;
    private javax.swing.JTabbedPane jTabbedClientes;
    private javax.swing.JTabbedPane jTabbedPanel;
    private javax.swing.JTable jTableHistorial;
    private javax.swing.JTextField jTextFieldClientePassword;
    private javax.swing.JTextField jTextFieldClienteUsuario;
    private javax.swing.JTextField jTextFieldMariaDBPassword;
    private javax.swing.JTextField jTextFieldMariaDBUsuario;
    // End of variables declaration//GEN-END:variables
}
