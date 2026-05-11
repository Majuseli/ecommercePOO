
package modelo;


public class ProductoAlimenticio  extends Producto{
    private String fechaVencimiento;  // formato "DD/MM/AAAA"

    public ProductoAlimenticio(String id, String nombre, double precio, int stock, String fechaVencimiento) {
        super(id, nombre, precio, stock);
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String getTipo() {
        return "Alimenticio";
    }
    
}
