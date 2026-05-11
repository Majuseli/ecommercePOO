
package modelo;

import java.util.ArrayList;

public class Carrito {
    private ArrayList<ItemCarrito> items;
    private Cliente cliente;

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }

    public ArrayList<ItemCarrito> getItems() {
        return items;
    }

    public Cliente getCliente() {
        return cliente;
    }

    // Agregar producto al carrito
    public void agregarItem(Producto producto, int cantidad) throws StockInsuficienteException {
        // Verificar stock antes de agregar
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException("Stock insuficiente de " + producto.getNombre() + 
                                                   ". Disponible: " + producto.getStock());
        }
        
        // Buscar si el producto ya está en el carrito
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                // Actualizar cantidad
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (nuevaCantidad > producto.getStock()) {
                    throw new StockInsuficienteException("No puedes agregar " + cantidad + " más. " +
                                                           "Stock actual: " + producto.getStock() +
                                                           ", ya tienes: " + item.getCantidad());
                }
                item.setCantidad(nuevaCantidad);
                return;
            }
        }
        
        // Si no existe, agregar nuevo item
        items.add(new ItemCarrito(producto, cantidad));
    }

    // Eliminar producto del carrito
    public void eliminarItem(String productoId) {
        items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }

    // Modificar cantidad de un producto
    public void modificarCantidad(String productoId, int nuevaCantidad) throws StockInsuficienteException {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(productoId)) {
                if (nuevaCantidad <= 0) {
                    eliminarItem(productoId);
                } else {
                    if (nuevaCantidad > item.getProducto().getStock()) {
                        throw new StockInsuficienteException("Stock insuficiente. Máximo disponible: " + 
                                                               item.getProducto().getStock());
                    }
                    item.setCantidad(nuevaCantidad);
                }
                return;
            }
        }
    }

    // Calcular total del carrito
    public double calcularTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Vaciar carrito
    public void vaciar() {
        items.clear();
    }
}