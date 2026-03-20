
package creacionales;

import entidades.Seguro;
import entidades.SeguroFallabela;
import entidades.SeguroSura;

public class FactorySeguros {
    public static Seguro crearSeguro(String tipo) {
        switch (tipo) {
            case "BASICO":
                return new SeguroFallabela();
            case "PREMIUM":
                return new SeguroSura();
            default:
                throw new IllegalArgumentException("Tipo de seguro desconocido: " + tipo);
        }
    }
}
