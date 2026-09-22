package Modelo;

public class NivelFactory {

    public Nivel crearNivel(int numeroNivel) throws NivelInvalidoException {
        switch (numeroNivel) {
            case 1:
                return crearNivel1();
            case 2:
                return null;
            case 3:
                return null; 
            default:
                throw new NivelInvalidoException("No existe el nivel " + numeroNivel);
        }
    }

    private Nivel crearNivel1() {
        Acertijo acertijo = new AcertijoNumerico(1, "¿Cuántas vidas tiene un gato?", 9);

        Nivel nivel = new Nivel(1, acertijo, "Recursos/Mapas/primer_piso.txt","/Recursos/Mapas/mapa_plantaBaja.png", 32, 32);
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");
        nivel.agregarEnemigo(new Enemigo(100, 10, 1, 400, 150));
        nivel.agregarEnemigo(new Enemigo(100, 10, 2, 550, 320));

        // TODO cuando definan posiciones: nivel.agregarItem(...), nivel.agregarHabitacion(...)

        return nivel;
    }

}


    

