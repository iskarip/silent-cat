package Modelo;

public class FabricaAcertijo {

    // --FABRICA DE ACERTIJOS --
    // Centraliza y organiza la informacion de los acertijos.
    // Por el momento, clase provisoria por las dudas hasta que concretemos los acertijos

    public static Acertijo crearAcertijo (String tipo, int id, String descripcion, String respuesta) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de acertijo no puede ser nulo");
        }

        switch (tipo.trim().toLowerCase()) {
            case "numerico":
                try {
                    int clave = Integer.parseInt(respuesta.trim());
                    return new AcertijoNumerico(id, descripcion, clave);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("La respuesta debe ser un numero valido: " + respuesta);
                }
            case "objeto":
                return new AcertijoObjeto(id, descripcion, respuesta.trim());

            default:
                throw new IllegalArgumentException("Tipo de acertijo desconocido");
        }
    }
}
