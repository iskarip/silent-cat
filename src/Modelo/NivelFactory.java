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

        return nivel;
    }

    private Nivel crearNivel2() {
        Acertijo acertijo = new AcertijoNumerico(2, "¿Cuál es el animal más rápido del mundo?", 1);
        
        Nivel nivel = new Nivel(2, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);
        
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");

        ItemMedicina medicina1 = new ItemMedicina(20,9,7);
        nivel.agregarItem(medicina1);

        ItemBateria bateria1 = new ItemBateria(4,7);
        nivel.agregarItem(bateria1);

        ItemBateria bateria2 = new ItemBateria(4,12);
        nivel.agregarItem(bateria2);

        ItemBateria bateria3 = new ItemBateria(19,4);
        nivel.agregarItem(bateria3);

        Enemigo enemigo1 = new Enemigo(100, 10, 1, 0, 0);
        enemigo1.colocarEnTile(15, 9);
        nivel.agregarEnemigo(enemigo1);

        Enemigo enemigo2 = new Enemigo(100, 10, 2, 0, 0);
        enemigo2.colocarEnTile(2, 8);
        nivel.agregarEnemigo(enemigo2);

        return nivel;
    }

}


    

