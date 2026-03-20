
package estructurales;

import entidades.IVehiculo;
import entidades.VehiculoReal;
//import entidades.Fotografia;
import java.util.List;
//import java.util.ArrayList;


public class ProxyVehiculo implements IVehiculo {
    
    // Referencia al objeto real (inicialmente nula)
    private VehiculoReal vehiculoReal;
    
    // Datos ligeros que el Proxy SÍ conoce de antemano
    private List<String> rutasOriginales;
    private double precioBase;
    
    
    // * El constructor recibe solo los datos ligeros necesarios para el catálogo.
     //* @param rutas Lista de rutas de archivos de imagen (String).
     //* @param precio El precio base del vehículo.
    public ProxyVehiculo(List<String> rutas, double precio) {
        this.rutasOriginales = rutas;
        this.precioBase = precio;
        this.vehiculoReal = null; // Aún no instanciamos el objeto pesado
        System.out.println("[Proxy] Creado proxy ligero para vehículo de $" + precio);
    }

    // --- MÉTODOS DE LA INTERFAZ IVehiculo (Implementación) ---

    /**
     * Muestra los detalles. Si el vehículo real no existe, lo crea primero.
     */
    @Override
    public void mostrarDetalle() {
        // Lógica de Lazy Loading
        asegurarVehiculoReal();
        // Delegamos la tarea al objeto real
        this.vehiculoReal.mostrarDetalle();
    }

    /**
     * Retorna el precio. Como es un dato ligero, el Proxy lo responde directamente
     * sin necesidad de cargar el objeto real. ¡Aquí está la optimización!
     * @return El precio base.
     */
    @Override
    public double getPrecio() {
        return this.precioBase;
    }

    /**
     * Retorna la lista de rutas de fotos. Si el vehículo real no existe, lo crea.
     * @return Lista de rutas de fotos (String).
     */
    @Override
    public List<String> getRutasFotos() {
        // Lógica de Lazy Loading
        asegurarVehiculoReal();
        // Delegamos la tarea al objeto real, que ya tendrá las fotos cargadas
        return this.vehiculoReal.getRutasFotos();
    }

    // --- MÉTODO PRIVADO DE APOYO (Lógica del Patrón) ---

    /**
     * Verifica si el objeto pesado (VehiculoReal) ya fue cargado.
     * Si no, lo instancia (simulando una carga costosa de BD o disco).
     */
    private void asegurarVehiculoReal() {
        if (this.vehiculoReal == null) {
            System.out.println("[Proxy] -> Cargando VehiculoReal (operación costosa)...");
            
            // 1. Simular la creación del objeto pesado pasándole los datos ligeros.
            // Nota: El constructor de VehiculoReal debe aceptar estos parámetros.
            this.vehiculoReal = new VehiculoReal(this.rutasOriginales, this.precioBase);
            
            // 2. Opcional: Podrías hacer validaciones adicionales aquí.
            System.out.println("[Proxy] -> VehiculoReal cargado exitosamente.");
        }
    }
}