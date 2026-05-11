
package modelo;


public class Cliente extends Usuario{
    
     private Carrito carrito;

    public Cliente(String id, String nombre, String email, String contrasenia) {
        super(id, nombre, email, contrasenia);
        this.carrito = new Carrito(this);
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public String getRol() {
        return "Cliente";
    }
    
}
