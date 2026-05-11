
package vista;

import modelo.*;
import persistencia.GestionDatos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginUI extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtContrasenia;
    private JButton btnLogin, btnRegistro;
    private ArrayList<Usuario> usuarios;
    
    public LoginUI() {
        setTitle("Sistema de E-commerce - Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar usuarios desde CSV
        cargarUsuarios();
        
        initComponents();
    }
    
    private void cargarUsuarios() {
        try {
            usuarios = GestionDatos.cargarUsuarios();
            if (usuarios.isEmpty()) {
                // Si no hay usuarios, crear un admin por defecto
                usuarios.add(new Administrador("A001", "Admin", "admin@ecommerce.com", "admin123"));
                GestionDatos.guardarUsuarios(usuarios);
            }
        } catch (Exception e) {
            usuarios = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("E-commerce POO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        
        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        add(txtEmail, gbc);
        
        // Contraseña
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);
        
        txtContrasenia = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtContrasenia, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistro = new JButton("Registrarse");
        
        btnLogin.addActionListener(e -> login());
        btnRegistro.addActionListener(e -> {
            new RegistroUI(this);
            dispose();
        });
        
        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);
    }
    
    private void login() {
        String email = txtEmail.getText().trim();
        String contrasenia = new String(txtContrasenia.getPassword());
        
        if (email.isEmpty() || contrasenia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos");
            return;
        }
        
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getContrasenia().equals(contrasenia)) {
                // Login exitoso
                if (u instanceof Administrador) {
                    new AdminUI((Administrador) u);
                } else if (u instanceof Cliente) {
                    new CatalogoUI((Cliente) u, this);
                }
                dispose();
                return;
            }
        }
        
        JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}