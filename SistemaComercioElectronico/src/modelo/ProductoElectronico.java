
package modelo;

 
public class ProductoElectronico extends Producto{
    private int garantiaMeses;  // meses de garantía

    public ProductoElectronico(String id, String nombre, double precio, int stock, int garantiaMeses) {
        super(id, nombre, precio, stock);
        this.garantiaMeses = garantiaMeses;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public String getTipo() {
        return "Electrónico";
    }
    
}
