
package creacionales;

import entidades.IVehiculo;
import estructurales.ProxyVehiculo;
import java.util.ArrayList;
import java.util.List;

public class BuilderVehiculo {

private String marca;
    private String modelo;
    private Double precio; 
    private String tipo;   // Sedán, SUV, etc
    private int capacidad;
    private int ultimoDigitoPlaca;
private List<String> rutasFotos;    
    
    public BuilderVehiculo(){
        this.reset();
    }
    
    
    public void reset(){
        this.marca = null;
        this.modelo = null;
        this.tipo = null;
        this.capacidad = 0;
        this.ultimoDigitoPlaca = -1;
        this.rutasFotos = new ArrayList<>();
        System.out.println("[Builder] -> Estado reiniciado.");
    }
    
    public BuilderVehiculo conDatosBasicos(String marca, String modelo, int placa){
        this.marca = marca;
        this.modelo = modelo;
        this.ultimoDigitoPlaca = placa;
        System.out.println("[Buiñder] -> Datos basicos asigandos:" + marca + " " + modelo + "(Placa: " + placa + ")");
        return this;
    }
    public BuilderVehiculo conEspecificaciones(String tipo, int cap, double precio) {
        this.tipo = tipo;
        this.capacidad = cap;
        this.precio = precio;
        System.out.println("[Builder] -> Especificaciones asignadas: " + tipo + " (Precio: $" + precio + ")");
        return this;
    }
    public BuilderVehiculo agregarFoto(String ruta){
        if (this.rutasFotos == null) {
            this.rutasFotos = new ArrayList<>();
        }
        this.rutasFotos.add(ruta);
        System.out.println("[Builder] -> Foto agregada: " + ruta + " (Total: " + this.rutasFotos.size() + ")");
        return this;
    }
    public IVehiculo build() {
        // Validaciones antes de construir
        if (this.marca == null || this.modelo == null || this.precio == null) {
            throw new IllegalStateException("Error: Faltan datos obligatorios (marca, modelo o precio) para construir el vehículo.");
        }
        
        System.out.println("[Builder] -> Construyendo Proxy de " + this.marca + " " + this.modelo + "...");
        
        
        // Nota: El constructor de ProxyVehiculo debe coincidir con estos parámetros.
     
        ProxyVehiculo vehiculoFinal = new ProxyVehiculo(this.rutasFotos, this.precio.doubleValue());
        
        // 2. Reiniciamos el builder para que esté listo para otra construcción.
        this.reset();
        
        // 3. Retornamos el objeto ligero.
        return vehiculoFinal;
    }
    
}
