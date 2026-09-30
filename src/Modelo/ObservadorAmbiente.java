
package Modelo;

    // Contrato entre las zonas del modelo y quien las muestra (el controlador/vista).
    // El modelo solo conoce esta interfaz, nunca las clases de Vista (MVC).
    // Mismo patrón Observer que ObservadorPersonaje.
    
    public interface ObservadorAmbiente {

        // Una ZonaMensaje pide mostrar este texto en pantalla.
        void mostrarMensaje(String mensaje);

        // Un PuntoFlashback pide mostrar la imagen del flashback.
        void mostrarFlashback();
    }
        

