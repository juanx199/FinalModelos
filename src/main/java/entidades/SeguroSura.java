package entidades;

public class SeguroSura implements IAseguradora {

    @Override
    public double calcularPoliza(double valorVehiculo) {
        return valorVehiculo * 0.10;
    }

    @Override
    public String getNombreEntidad() {
        return "Seguro sura";
    }
}
