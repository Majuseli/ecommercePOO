
package persistencia;

import modelo.*;
import java.io.*;
import java.util.ArrayList;

public class GestionDatos {
    
    // Rutas de los archivos CSV (guardados en la carpeta "datos" del proyecto)
    private static final String RUTA_USUARIOS = "datos/usuarios.csv";
    private static final String RUTA_PRODUCTOS = "datos/productos.csv";
    private static final String RUTA_PEDIDOS = "datos/pedidos.csv";
    
    // ========== MÉTODOS PARA USUARIOS ==========
    
    public static void guardarUsuarios(ArrayList<Usuario> usuarios) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_USUARIOS))) {
            // Escribir cabecera
            writer.println("tipo,id,nombre,email,contrasenia");
            
            for (Usuario u : usuarios) {
                if (u instanceof Cliente) {
                    writer.println("Cliente," + u.getId() + "," + u.getNombre() + "," + 
                                   u.getEmail() + "," + u.getContrasenia());
                } else if (u instanceof Administrador) {
                    writer.println("Administrador," + u.getId() + "," + u.getNombre() + "," + 
                                   u.getEmail() + "," + u.getContrasenia());
                }
            }
        }
    }
    
    public static ArrayList<Usuario> cargarUsuarios() throws IOException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(RUTA_USUARIOS);
        
        if (!archivo.exists()) {
            return usuarios; // Si no existe, retornar lista vacía
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean esCabecera = true;
            
            while ((linea = reader.readLine()) != null) {
                if (esCabecera) {
                    esCabecera = false;
                    continue; // Saltar cabecera
                }
                
                String[] datos = linea.split(",");
                if (datos.length >= 5) {
                    String tipo = datos[0];
                    String id = datos[1];
                    String nombre = datos[2];
                    String email = datos[3];
                    String contrasenia = datos[4];
                    
                    if (tipo.equals("Cliente")) {
                        usuarios.add(new Cliente(id, nombre, email, contrasenia));
                    } else if (tipo.equals("Administrador")) {
                        usuarios.add(new Administrador(id, nombre, email, contrasenia));
                    }
                }
            }
        }
        return usuarios;
    }
    
    // ========== MÉTODOS PARA PRODUCTOS ==========
    
    public static void guardarProductos(ArrayList<Producto> productos) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_PRODUCTOS))) {
            // Escribir cabecera
            writer.println("tipo,id,nombre,precio,stock,atributo_extra");
            
            for (Producto p : productos) {
                if (p instanceof ProductoElectronico) {
                    ProductoElectronico pe = (ProductoElectronico) p;
                    writer.println("Electronico," + pe.getId() + "," + pe.getNombre() + "," + 
                                   pe.getPrecio() + "," + pe.getStock() + "," + pe.getGarantiaMeses());
                } else if (p instanceof ProductoAlimenticio) {
                    ProductoAlimenticio pa = (ProductoAlimenticio) p;
                    writer.println("Alimenticio," + pa.getId() + "," + pa.getNombre() + "," + 
                                   pa.getPrecio() + "," + pa.getStock() + "," + pa.getFechaVencimiento());
                }
            }
        }
    }
    
    public static ArrayList<Producto> cargarProductos() throws IOException {
        ArrayList<Producto> productos = new ArrayList<>();
        File archivo = new File(RUTA_PRODUCTOS);
        
        if (!archivo.exists()) {
            return productos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean esCabecera = true;
            
            while ((linea = reader.readLine()) != null) {
                if (esCabecera) {
                    esCabecera = false;
                    continue;
                }
                
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    String tipo = datos[0];
                    String id = datos[1];
                    String nombre = datos[2];
                    double precio = Double.parseDouble(datos[3]);
                    int stock = Integer.parseInt(datos[4]);
                    String atributoExtra = datos[5];
                    
                    if (tipo.equals("Electronico")) {
                        int garantia = Integer.parseInt(atributoExtra);
                        productos.add(new ProductoElectronico(id, nombre, precio, stock, garantia));
                    } else if (tipo.equals("Alimenticio")) {
                        productos.add(new ProductoAlimenticio(id, nombre, precio, stock, atributoExtra));
                    }
                }
            }
        }
        return productos;
    }
    
    // ========== MÉTODOS PARA PEDIDOS ==========
    // (Implementaremos más adelante cuando tengamos la clase Pedido completa)
    
    public static boolean existeArchivoProductos() {
        File archivo = new File(RUTA_PRODUCTOS);
        return archivo.exists();
    }
    
    public static boolean existeArchivoUsuarios() {
        File archivo = new File(RUTA_USUARIOS);
        return archivo.exists();
    }
}