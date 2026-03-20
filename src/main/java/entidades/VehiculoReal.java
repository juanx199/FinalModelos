
package entidades;


import java.util.ArrayList;
import java.util.List;

public class VehiculoReal implements IVehiculo {

    private List<Fotografia> fotos; // Objetos pesados
    private double precio;
    private List<String> historialMantenimiento;

    // constructor q proxy usa
    public VehiculoReal(List<String> rutas, double precio) {
        this.precio = precio;
        this.fotos = new ArrayList<>();
        this.historialMantenimiento = new ArrayList<>();
        
        // Simulamos la carga pesada de convertir rutas en objetos Fotografía
        for (String ruta : rutas) {
            this.fotos.add(new Fotografia("General", ruta));
        }
    }

    @Override
    public void mostrarDetalle() { // Arregla el error 1
        System.out.println("--- DETALLES DEL VEHÍCULO REAL ---");
        System.out.println("Precio: $" + this.precio);
        System.out.println("Fotos cargadas en memoria: " + fotos.size());
    }

    @Override
    public double getPrecio() {
        return this.precio;
    }

    @Override
    public List<String> getRutasFotos() { // Arregla el error 2
        List<String> rutas = new ArrayList<>();
        for (Fotografia foto : fotos) {
            rutas.add(foto.getRuta());
        }
        return rutas;
    }
}