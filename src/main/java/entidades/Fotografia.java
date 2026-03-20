
package entidades;


public class Fotografia {

    private String parte; //  "Motor", "Interior", "Frontal"
    private String archivoImagen; // La ruta o nombre del archivo

    /**
     * Constructor para la fotografía.
     * @param parte Categoría de la foto.
     * @param archivoImagen Ruta del archivo.
     */
    public Fotografia(String parte, String archivoImagen) {
        this.parte = parte;
        this.archivoImagen = archivoImagen;
    }

    /**
     * Retorna la ruta del archivo. 
     * Este es el método que llama VehiculoReal para cumplir con IVehiculo.
     */
    public String getRuta() {
        return archivoImagen;
    }

    public String getParte() {
        return parte;
    }

    //Para depuración en consola
    @Override
    public String toString() {
        return "Foto [" + parte + "]: " + archivoImagen;
    }
}
