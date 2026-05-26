
package entidades;


import java.util.ArrayList;
import java.util.List;

public class VehiculoReal implements IVehiculo {

    private List<Fotografia> fotos; // Objetos pesados
    private double precio;
    private List<Mantenimiento> historialMantenimiento;
    private Asesor asesor;

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
    public void mostrarDetalle() {
        System.out.println("--- DETALLES DEL VEHÍCULO REAL ---");
        System.out.println("Precio: $" + this.precio);
        System.out.println("Fotos cargadas en memoria: " + fotos.size());
        if (asesor != null) {
            System.out.println("Asesor asignado: " + asesor.getNombre() + " (" + asesor.getDatosContacto() + ")");
        } else {
            System.out.println("No hay asesor asignado.");
        }
    }

    @Override
    public double getPrecio() {
        return this.precio;
    }

    @Override
    public List<String> getRutasFotos() {
        List<String> rutas = new ArrayList<>();
        for (Fotografia foto : fotos) {
            rutas.add(foto.getRuta());
        }
        return rutas;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public List<Mantenimiento> getHistorialMantenimiento() {
        return historialMantenimiento;
    }

    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.historialMantenimiento.add(mantenimiento);
    }
}