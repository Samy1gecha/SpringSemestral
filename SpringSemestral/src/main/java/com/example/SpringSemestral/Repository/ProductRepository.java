package Repository;

import Modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {
    private List<Producto> productos = new ArrayList<>();

    /**
     * Agrega un producto al repositorio.
     * @param producto Objeto Producto que se va a agregar
     */
    public void addProducto(Producto producto) {
        productos.add(producto);
    }

    /**
     * Elimina un producto del repositorio por su nombre.
     * @param nombre Nombre del producto que se va a eliminar
     */
    public void removeProducto(String nombre) {
        // Buscar y eliminar el producto con el nombre proporcionado
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                productos.remove(producto);
                break;
            }
        }
    }

    /**
     * Actualiza un producto existente en el repositorio.
     * @param newProducto Objeto Producto con la nueva información
     */
    public void updateProducto(Producto newProducto) {
        // Buscar el producto y actualizar sus datos
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getNombre().equalsIgnoreCase(newProducto.getNombre())) {
                productos.set(i, newProducto);
                break;
            }
        }
    }

    /**
     * Obtiene todos los productos del repositorio.
     * @return Lista de productos
     */
    public List<Producto> getProductos() {
        return productos;
    }
}
