package entidades;

public class SeguroSura implements Seguro {

    @Override
    public double calcularPoliza(double valorVehiculo) {
        return valorVehiculo * 0.10;
    }

    @Override
    public String getNombreEntidad() {
        return "Seguro Premium";
    }
}
