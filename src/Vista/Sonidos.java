package Vista;

// Catálogo de todos los sonidos del juego. Es el único lugar donde se escriben rutas de audio.
// El resto del código usa estas constantes.
public final class Sonidos {

    private Sonidos() { } // no se instancia, solo agrupa constantes

    //-- MUSICA MENU --
    public static final String MUSICA_MENU = null;
    public static final String SONIDO_BOTON_MENU = null;

    // -- PASOS --
    public static final String PASOS_MADERA = "Recursos/Sonidos/Pasos/pasos_madera.wav";
    public static final String PASOS_METAL = "Recursos/Sonidos/Pasos/pasos_metal.wav";

    // -- MÚSICA DE NIVELES --
        public static final String MUSICA_NIVEL_1 = null;
        public static final String MUSICA_NIVEL_2 = "Recursos/Sonidos/Musica/nivel2.wav";
        public static final String MUSICA_NIVEL_3 = null;

    // -- SONIDO DE PUERTA -- 
    public static final String EFECTO_PUERTA = "Recursos/Sonidos/Objetos/puerta.wav";
}