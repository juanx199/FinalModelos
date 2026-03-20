
package entidades;


public class Fotografia {

    private String area; //  "Motor", "Interior", "Frontal"
    private String archivoImagen; // La ruta o nombre del archivo

    /**
     * Constructor para la fotografía.
     * @param parte Categoría de la foto.
     * @param archivoImagen Ruta del archivo.
     */
    public Fotografia(String area, String archivoImagen) {
        this.area = area;
        this.archivoImagen = archivoImagen;
    }

    /**
     * Retorna la ruta del archivo. 
     * Este es el método que llama VehiculoReal para cumplir con IVehiculo.
     */
    public String getRuta() {
        return archivoImagen;
    }

    public String getArea() {
        return area;
    }

    //Para depuración en consola
    @Override
    public String toString() {
        return "Foto [" + area + "]: " + archivoImagen;
    }
}
