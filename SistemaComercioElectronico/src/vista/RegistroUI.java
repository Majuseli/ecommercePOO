
package vista;

import modelo.*;
import persistencia.GestionDatos;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RegistroUI extends JFrame {
    private JTextField txtId, txtNombre, txtEmail;
    private JPasswordField txtContrasenia;
    private JButton btnRegistrar, btnCancelar;
    private LoginUI loginUI;
    
    public RegistroUI(LoginUI loginUI) {
        this.loginUI = loginUI;
        setTitle("Registro de Nuevo Cliente");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        int y = 0;
        
        // Título
        JLabel lblTitulo = new JLabel("Registro de Cliente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        
        // ID
        gbc.gridwidth = 1;
        gbc.gridy = y++;
        gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);
        txtId = new JTextField(15);
        gbc.gridx = 1;
        add(txtId, gbc);
        
        // Nombre
        gbc.gridy = y++;
        gbc.gridx = 0;
        add(new JLabel("Nombre completo:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1;
        add(txtNombre, gbc);
        
        // Email
        gbc.gridy = y++;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        add(txtEmail, gbc);
        
        // Contraseña
        gbc.gridy = y++;
        gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);
        txtContrasenia = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtContrasenia, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        
        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> {
            loginUI.setVisible(true);
            dispose();
        });
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        gbc.gridy = y++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);
    }
    
    private void registrar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String contrasenia = new String(txtContrasenia.getPassword());
        
        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() || contrasenia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos");
            return;
        }
        
        try {
            // Cargar usuarios existentes
            ArrayList<Usuario> usuarios = GestionDatos.cargarUsuarios();
            
            // Verificar si el ID o email ya existen
            for (Usuario u : usuarios) {
                if (u.getId().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese ID");
                    return;
                }
                if (u.getEmail().equals(email)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese email");
                    return;
                }
            }
            
            // Crear nuevo cliente
            Cliente nuevoCliente = new Cliente(id, nombre, email, contrasenia);
            usuarios.add(nuevoCliente);
            
            // Guardar en CSV
            GestionDatos.guardarUsuarios(usuarios);
            
            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ahora puede iniciar sesión.");
            loginUI.setVisible(true);
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage());
        }
    }
}