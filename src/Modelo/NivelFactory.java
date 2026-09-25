package Modelo;

public class NivelFactory {

    public Nivel crearNivel(int numeroNivel) throws NivelInvalidoException {
        switch (numeroNivel) {
            case 1:
                return crearNivel2(); // temporal 
            default:
                throw new NivelInvalidoException("No existe el nivel " + numeroNivel);
        }
    }

    private Nivel crearNivel1() {
        Acertijo acertijo = new AcertijoNumerico(1, "¿Cuántas vidas tiene un gato?", 9);

        Nivel nivel = new Nivel(1, acertijo, "Recursos/Mapas/primer_piso.txt","/Recursos/Mapas/mapa_plantaBaja.png", 7, 13);
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");
        nivel.setAcertijoX(300); // TODO: ajustar estos valores segun donde se quiera que este el acertijo en el mapa
        nivel.setAcertijoY(200);
        nivel.agregarEnemigo(new Enemigo(100, 10, 1, 800, 80));
        nivel.agregarEnemigo(new Enemigo(100, 10, 2, 550, 320));

        // TODO cuando definan posiciones: nivel.agregarItem(...), nivel.agregarHabitacion(...)

itemBateria bateria1 = new itemBateria();
bateria1.setPosicionX(300); //posicion x para probar lo del itembateria nomas
bateria1.setPosicionY(200);
nivel.agregarItem(bateria1);




        return nivel;
    }

    private Nivel crearNivel2() {
        Acertijo acertijo = new AcertijoNumerico(2, "¿Cuál es el animal más rápido del mundo?", 1);
        
        Nivel nivel = new Nivel(2, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);
        
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");

        return nivel;
    }

}


    

