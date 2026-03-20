
package entidades;

import java.util.List;

public interface IVehiculo {
    
    //Muestra la info completa del vehiculo
    //En proxy dispara la carga del bjeto real
    void mostrarDetalle();
    
    
    
    double getPrecio();
    //return precio en double
    List<String> getRutasFotos();
}
