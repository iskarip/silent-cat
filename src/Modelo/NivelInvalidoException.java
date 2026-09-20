package Modelo;

public class NivelInvalidoException extends Exception {
    public NivelInvalidoException(String mensaje) {
        super(mensaje);
    }
    public NivelInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}