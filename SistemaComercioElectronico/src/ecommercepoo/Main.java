
package ecommercepoo;

import modelo.*;
import persistencia.GestionDatos;
import java.util.ArrayList;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        
        // ========== 1. CREAR DATOS DE EJEMPLO ==========
        System.out.println("=== CREANDO DATOS DE EJEMPLO ===");
        
        // Crear productos
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new ProductoElectronico("P001", "Laptop HP", 3500900,25,7));
        productos.add(new ProductoElectronico("P002", "Mouse USB", 150000,45,6));
        productos.add(new ProductoAlimenticio("P003", "Manzana Roja", 3500,20,"31/05/2026"));
        productos.add(new ProductoAlimenticio("P004", "Pan Integral", 4500,30,"15/05/2026"));
        
        // Crear usuarios
        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Administrador("A001", "Juan Lopez","juan@mail.com","admin123"));
        usuarios.add(new Cliente("C001", "Eric Perez", "eric@mail.com", "1234"));
       //suarios.add(new Cliente("C002", "María García", "maria@mail.com", "maria456"));
        
        // ========== 2. GUARDAR EN ARCHIVOS CSV ==========
        System.out.println("\n=== GUARDANDO DATOS EN CSV ===");
        try {
            GestionDatos.guardarProductos(productos);
            System.out.println("Productos guardados correctamente en datos/productos.csv");
            
            GestionDatos.guardarUsuarios(usuarios);
            System.out.println("Usuarios guardados correctamente en datos/usuarios.csv");
            
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
        
        // ========== 3. CARGAR DATOS DESDE LOS CSV ==========
        System.out.println("\n=== CARGANDO DATOS DESDE CSV ===");
        try {
            ArrayList<Producto> productosCargados = GestionDatos.cargarProductos();
            System.out.println("Productos cargados: " + productosCargados.size());
            
            ArrayList<Usuario> usuariosCargados = GestionDatos.cargarUsuarios();
            System.out.println("Usuarios cargados: " + usuariosCargados.size());
            
            // ========== 4. MOSTRAR DATOS CARGADOS ==========
            System.out.println("\n=== LISTA DE PRODUCTOS CARGADOS ===");
            for (Producto p : productosCargados) {
                System.out.println("  - " + p.getId() + " | " + p.getNombre() + 
                                   " | $" + p.getPrecio() + " | Stock: " + p.getStock() +
                                   " | Tipo: " + p.getTipo());
            }
            
            System.out.println("\n=== LISTA DE USUARIOS CARGADOS ===");
            for (Usuario u : usuariosCargados) {
                System.out.println("  - " + u.getId() + " | " + u.getNombre() + 
                                   " | " + u.getEmail() + " | Rol: " + u.getRol());
            }
            
        } catch (IOException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
        
        // ========== 5. VERIFICAR QUE LA CARPETA DATOS SE CREÓ ==========
        System.out.println("\n=== VERIFICACIÓN ===");
        System.out.println("¿Existe archivo de productos? " + GestionDatos.existeArchivoProductos());
        System.out.println("¿Existe archivo de usuarios? " + GestionDatos.existeArchivoUsuarios());
        System.out.println("\nLos archivos CSV se han creado en la carpeta 'datos/' de tu proyecto.");
        System.out.println("Puedes abrirlos con cualquier editor de texto para ver su contenido.");
    }
}