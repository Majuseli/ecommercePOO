
package modelo;

public abstract class Usuario {
    
    private String id;
    private String nombre;
    private String email;
    private String contrasenia;

    public Usuario(String id, String nombre, String email, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getContrasenia() { return contrasenia; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    // Método abstracto (polimorfismo)
    public abstract String getRol();
    
}
