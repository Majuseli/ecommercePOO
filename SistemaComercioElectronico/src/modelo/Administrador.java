
package modelo;


public class Administrador extends Usuario{
    
    public Administrador(String id, String nombre, String email, String contrasenia) {
        super(id, nombre, email, contrasenia);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }
    
}
