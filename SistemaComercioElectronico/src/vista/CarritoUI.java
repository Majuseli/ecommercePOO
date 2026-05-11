
package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class CarritoUI extends JFrame {
    private Cliente cliente;
    private CatalogoUI catalogoUI;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    
    public CarritoUI(Cliente cliente, CatalogoUI catalogoUI) {
        this.cliente = cliente;
        this.catalogoUI = catalogoUI;
        setTitle("Mi Carrito de Compras");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        actualizarTabla();
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Tabla del carrito
        String[] columnas = {"Producto", "Precio Unitario", "Cantidad", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCarrito = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaCarrito);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnModificar = new JButton("Modificar Cantidad");
        JButton btnSeguirComprando = new JButton("Seguir Comprando");
        JButton btnCheckout = new JButton("Proceder al Pago");
        
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnModificar.addActionListener(e -> modificarCantidad());
        btnSeguirComprando.addActionListener(e -> dispose());
        btnCheckout.addActionListener(e -> {
            if (cliente.getCarrito().getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío");
            } else {
                new CheckoutUI(cliente, this);
            }
        });
        
        panelBotones.add(btnEliminar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnSeguirComprando);
        panelBotones.add(btnCheckout);
        
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        panelInferior.add(lblTotal, BorderLayout.SOUTH);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        ArrayList<ItemCarrito> items = cliente.getCarrito().getItems();
        
        for (ItemCarrito item : items) {
            modeloTabla.addRow(new Object[]{
                item.getProducto().getNombre(),
                "$" + item.getProducto().getPrecio(),
                item.getCantidad(),
                "$" + item.getSubtotal()
            });
        }
        
        lblTotal.setText("Total: $" + cliente.getCarrito().calcularTotal());
    }
    
    private void eliminarProducto() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
            return;
        }
        
        ItemCarrito item = cliente.getCarrito().getItems().get(fila);
        cliente.getCarrito().eliminarItem(item.getProducto().getId());
        actualizarTabla();
        catalogoUI.actualizarContadorCarrito();
    }
    
    private void modificarCantidad() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para modificar");
            return;
        }
        
        ItemCarrito item = cliente.getCarrito().getItems().get(fila);
        String input = JOptionPane.showInputDialog(this, 
            "Nueva cantidad para " + item.getProducto().getNombre() + ":", 
            item.getCantidad());
        
        if (input != null) {
            try {
                int nuevaCantidad = Integer.parseInt(input);
                if (nuevaCantidad <= 0) {
                    cliente.getCarrito().eliminarItem(item.getProducto().getId());
                } else {
                    cliente.getCarrito().modificarCantidad(item.getProducto().getId(), nuevaCantidad);
                }
                actualizarTabla();
                catalogoUI.actualizarContadorCarrito();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida");
            } catch (StockInsuficienteException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
}