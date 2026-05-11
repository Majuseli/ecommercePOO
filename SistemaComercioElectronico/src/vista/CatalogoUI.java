package vista;

import modelo.*;
import persistencia.GestionDatos;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class CatalogoUI extends JFrame {
    private Cliente cliente;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtCantidad;
    private JButton btnAgregarCarrito, btnVerCarrito, btnHistorial, btnCerrarSesion;
    private ArrayList<Producto> productos;
    private LoginUI loginUI;
    
    public CatalogoUI(Cliente cliente, LoginUI loginUI) {
        this.cliente = cliente;
        this.loginUI = loginUI;
        setTitle("Catálogo de Productos - Bienvenido " + cliente.getNombre());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cargarProductos();
        initComponents();
        setVisible(true);
    }
    
    private void cargarProductos() {
        try {
            productos = GestionDatos.cargarProductos();
            if (productos.isEmpty()) {
                // Crear productos de ejemplo si no hay
                productos.add(new ProductoElectronico("P001", "Laptop HP", 750.99, 5, 12));
                productos.add(new ProductoElectronico("P002", "Mouse USB", 15.50, 20, 6));
                productos.add(new ProductoAlimenticio("P003", "Manzana Roja", 0.50, 100, "31/12/2025"));
                productos.add(new ProductoAlimenticio("P004", "Pan Integral", 2.30, 30, "15/06/2025"));
                GestionDatos.guardarProductos(productos);
            }
        } catch (Exception e) {
            productos = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVerCarrito = new JButton("Ver Carrito (" + cliente.getCarrito().getItems().size() + ")");
        btnHistorial = new JButton("Mi Historial");
        btnCerrarSesion = new JButton("Cerrar Sesión");
        
        panelSuperior.add(btnVerCarrito);
        panelSuperior.add(btnHistorial);
        panelSuperior.add(btnCerrarSesion);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Precio", "Stock", "Tipo", "Detalle"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Producto p : productos) {
            String detalle = "";
            if (p instanceof ProductoElectronico) {
                detalle = ((ProductoElectronico) p).getGarantiaMeses() + " meses garantía";
            } else if (p instanceof ProductoAlimenticio) {
                detalle = "Vence: " + ((ProductoAlimenticio) p).getFechaVencimiento();
            }
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), "$" + p.getPrecio(), p.getStock(), p.getTipo(), detalle
            });
        }
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior para agregar al carrito
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField(5);
        txtCantidad.setText("1");
        panelInferior.add(txtCantidad);
        
        btnAgregarCarrito = new JButton("Agregar al Carrito");
        panelInferior.add(btnAgregarCarrito);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarCarrito.addActionListener(e -> agregarAlCarrito());
        btnVerCarrito.addActionListener(e -> new CarritoUI(cliente, this));
        btnHistorial.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Historial de pedidos"));
        btnCerrarSesion.addActionListener(e -> {
            loginUI.setVisible(true);
            dispose();
        });
    }
    
    private void agregarAlCarrito() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero");
            return;
        }
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida (mayor a 0)");
            return;
        }
        
        Producto producto = productos.get(filaSeleccionada);
        
        try {
            cliente.getCarrito().agregarItem(producto, cantidad);
            JOptionPane.showMessageDialog(this, "✓ " + cantidad + " x " + producto.getNombre() + " agregado al carrito");
            actualizarContadorCarrito();
        } catch (StockInsuficienteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    public void actualizarContadorCarrito() {
        btnVerCarrito.setText("Ver Carrito (" + cliente.getCarrito().getItems().size() + ")");
    }
}