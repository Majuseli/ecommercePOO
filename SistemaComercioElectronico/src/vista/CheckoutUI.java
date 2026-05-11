
package vista;


import modelo.*;
import persistencia.GestionDatos;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckoutUI extends JFrame {
    private Cliente cliente;
    private CarritoUI carritoUI;
    private JComboBox<String> cbMetodoPago;
    private JPanel panelDetallesPago;
    private JTextField txtReferencia, txtNumeroTarjeta, txtBanco;
    
    public CheckoutUI(Cliente cliente, CarritoUI carritoUI) {
        this.cliente = cliente;
        this.carritoUI = carritoUI;
        setTitle("Finalizar Compra");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con resumen
        JPanel panelResumen = new JPanel();
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen del Pedido"));
        panelResumen.setLayout(new GridLayout(3, 1));
        panelResumen.add(new JLabel("Cliente: " + cliente.getNombre()));
        panelResumen.add(new JLabel("Total a pagar: $" + cliente.getCarrito().calcularTotal()));
        panelResumen.add(new JLabel("Productos: " + cliente.getCarrito().getItems().size()));
        add(panelResumen, BorderLayout.NORTH);
        
        // Panel central - Método de pago
        JPanel panelPago = new JPanel(new BorderLayout(10, 10));
        panelPago.setBorder(BorderFactory.createTitledBorder("Método de Pago"));
        
        JPanel panelSelector = new JPanel();
        panelSelector.add(new JLabel("Seleccione método:"));
        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta de Crédito", "Transferencia Bancaria"});
        cbMetodoPago.addActionListener(e -> actualizarPanelPago());
        panelSelector.add(cbMetodoPago);
        panelPago.add(panelSelector, BorderLayout.NORTH);
        
        panelDetallesPago = new JPanel(new GridLayout(3, 2, 5, 5));
        panelDetallesPago.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPago.add(panelDetallesPago, BorderLayout.CENTER);
        
        add(panelPago, BorderLayout.CENTER);
        
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel();
        JButton btnConfirmar = new JButton("Confirmar Pedido");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnConfirmar.addActionListener(e -> confirmarPedido());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
        actualizarPanelPago();
    }
    
    private void actualizarPanelPago() {
        panelDetallesPago.removeAll();
        String metodo = (String) cbMetodoPago.getSelectedItem();
        
        switch (metodo) {
            case "Efectivo":
                panelDetallesPago.add(new JLabel("Referencia de pago:"));
                txtReferencia = new JTextField();
                panelDetallesPago.add(txtReferencia);
                break;
            case "Tarjeta de Crédito":
                panelDetallesPago.add(new JLabel("Número de tarjeta:"));
                txtNumeroTarjeta = new JTextField();
                panelDetallesPago.add(txtNumeroTarjeta);
                panelDetallesPago.add(new JLabel("Fecha expiración:"));
                panelDetallesPago.add(new JTextField(10));
                panelDetallesPago.add(new JLabel("CVV:"));
                panelDetallesPago.add(new JTextField(5));
                break;
            case "Transferencia Bancaria":
                panelDetallesPago.add(new JLabel("Banco:"));
                txtBanco = new JTextField();
                panelDetallesPago.add(txtBanco);
                panelDetallesPago.add(new JLabel("Número de cuenta:"));
                panelDetallesPago.add(new JTextField());
                break;
        }
        
        panelDetallesPago.revalidate();
        panelDetallesPago.repaint();
    }
    
    private void confirmarPedido() {
        // Simular procesamiento del pedido
        double total = cliente.getCarrito().calcularTotal();
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Confirmar pedido por $" + total + "?",
            "Confirmar pedido",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Reducir stock de productos
                for (ItemCarrito item : cliente.getCarrito().getItems()) {
                    item.getProducto().reducirStock(item.getCantidad());
                }
                
                // Guardar productos actualizados
                GestionDatos.guardarProductos(cargarProductosActualizados());
                
                // Vaciar carrito
                cliente.getCarrito().vaciar();
                
                JOptionPane.showMessageDialog(this, 
                    "¡Pedido realizado con éxito!\n" +
                    "Total pagado: $" + total + "\n" +
                    "Método: " + cbMetodoPago.getSelectedItem() + "\n" +
                    "¡Gracias por su compra!");
                
                carritoUI.dispose();
                dispose();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al procesar pedido: " + e.getMessage());
            }
        }
    }
    
    private java.util.ArrayList<Producto> cargarProductosActualizados() throws Exception {
        return GestionDatos.cargarProductos();
    }
}