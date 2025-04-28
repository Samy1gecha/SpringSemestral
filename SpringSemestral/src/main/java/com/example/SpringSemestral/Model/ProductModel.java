package Model;

public class Producto {
    private String nombre;      // Nombre del producto
    private String descripcion; // Descripción del producto
    private double precio;      // Precio del producto
    private int stock;          // Cantidad disponible en inventario

    /**
     * Constructor de Producto
     * @param nombre Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio Precio del producto
     * @param stock Cantidad en stock del producto
     */
    public Producto(String nombre, String descripcion, double precio, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos Getter y Setter para acceder a los atributos

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Producto: " + nombre + ", Descripción: " + descripcion + ", Precio: " + precio + ", Stock: " + stock;
    }
}
