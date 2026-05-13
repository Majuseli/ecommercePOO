
package vista;

import modelo.*;
import persistencia.GestionDatos;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminUI extends JFrame {
    private Administrador admin;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ArrayList<Producto> productos;
    private JTextField txtId, txtNombre, txtPrecio, txtStock, txtAtributo;
    private JComboBox<String> cbTipo;
    
    public AdminUI(Administrador admin) {
        this.admin = admin;
        setTitle("Panel de Administrador - " + admin.getNombre());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cargarProductos();
        initComponents();
        setVisible(true);
    }
    
    private void cargarProductos() {
        try {
            productos = GestionDatos.cargarProductos();
        } catch (Exception e) {
            productos = new ArrayList<>();
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel de formulario para agregar/editar productos
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Productos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int y = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        panelFormulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cbTipo = new JComboBox<>(new String[]{"Electrónico", "Alimenticio"});
        panelFormulario.add(cbTipo, gbc);
        
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10);
        panelFormulario.add(txtId, gbc);
        
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(10);
        panelFormulario.add(txtNombre, gbc);
        
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panelFormulario.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(10);
        panelFormulario.add(txtPrecio, gbc);
        
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        panelFormulario.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(10);
        panelFormulario.add(txtStock, gbc);
        
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lblAtributo = new JLabel("Garantía (meses) / Vencimiento:");
        panelFormulario.add(lblAtributo, gbc);
        gbc.gridx = 1;
        txtAtributo = new JTextField(10);
        panelFormulario.add(txtAtributo, gbc);
        
        y++;
        JPanel panelBotonesForm = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        
        btnAgregar.addActionListener(e -> agregarProducto());
        btnModificar.addActionListener(e -> modificarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        
        panelBotonesForm.add(btnAgregar);
        panelBotonesForm.add(btnModificar);
        panelBotonesForm.add(btnEliminar);
        
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panelFormulario.add(panelBotonesForm, gbc);
        
        add(panelFormulario, BorderLayout.WEST);
        
        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Precio", "Stock", "Tipo", "Atributo Extra"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        actualizarTabla();
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarProductoSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);
        
        // Botón cerrar sesión
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });
        panelSuperior.add(btnCerrarSesion);
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            String atributo = "";
            if (p instanceof ProductoElectronico) {
                atributo = ((ProductoElectronico) p).getGarantiaMeses() + " meses";
            } else if (p instanceof ProductoAlimenticio) {
                atributo = ((ProductoAlimenticio) p).getFechaVencimiento();
            }
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), "$" + p.getPrecio(), p.getStock(), p.getTipo(), atributo
            });
        }
    }
    
    private void agregarProducto() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String atributo = txtAtributo.getText().trim();

            if (precio <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El precio no puede ser negativo, ni cero");
                return;
            }
            
            if (id.isEmpty() || nombre.isEmpty() || atributo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            
            Producto nuevo;
            if (cbTipo.getSelectedItem().equals("Electrónico")) {
                int garantia = Integer.parseInt(atributo);
                nuevo = new ProductoElectronico(id, nombre, precio, stock, garantia);
            } else {
                nuevo = new ProductoAlimenticio(id, nombre, precio, stock, atributo);
            }
            
            productos.add(nuevo);
            GestionDatos.guardarProductos(productos);
            actualizarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato de números");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void modificarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para modificar");
            return;
        }
        
        try {
            Producto p = productos.get(fila);
            p.setNombre(txtNombre.getText().trim());
            p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            p.setStock(Integer.parseInt(txtStock.getText().trim()));
            
            if (p instanceof ProductoElectronico) {
                ((ProductoElectronico) p).setGarantiaMeses(Integer.parseInt(txtAtributo.getText().trim()));
            } else if (p instanceof ProductoAlimenticio) {
                ((ProductoAlimenticio) p).setFechaVencimiento(txtAtributo.getText().trim());
            }
            
            GestionDatos.guardarProductos(productos);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Producto modificado exitosamente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productos.remove(fila);
            try {
                GestionDatos.guardarProductos(productos);
                actualizarTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Producto eliminado");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void cargarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila != -1) {
            Producto p = productos.get(fila);
            txtId.setText(p.getId());
            txtNombre.setText(p.getNombre());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtStock.setText(String.valueOf(p.getStock()));
            
            if (p instanceof ProductoElectronico) {
                cbTipo.setSelectedItem("Electrónico");
                txtAtributo.setText(String.valueOf(((ProductoElectronico) p).getGarantiaMeses()));
            } else if (p instanceof ProductoAlimenticio) {
                cbTipo.setSelectedItem("Alimenticio");
                txtAtributo.setText(((ProductoAlimenticio) p).getFechaVencimiento());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtAtributo.setText("");
    }
}
