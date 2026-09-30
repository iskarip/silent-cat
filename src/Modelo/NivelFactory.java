package Modelo;

public class NivelFactory {

    public Nivel crearNivel(int numeroNivel) throws NivelInvalidoException {
        switch (numeroNivel) {
            
            case 1:
                return crearNivel1(); //la planta baja
            case 2:
                return crearNivel2(); // temporal porque por ahora tenemos ese nivel cargado
            default:
                throw new NivelInvalidoException("No existe el nivel " + numeroNivel);
        }
    }

    private Nivel crearNivel1(){
        Acertijo acertijo = new AcertijoObjeto(1, "La puerta esta atascada por el oxido y no cede. Necesitas lubricar las bisagras.", "Lata de aceite");
    
        Nivel nivel= new Nivel(1, acertijo, "Recursos/Mapas/Grid/grid_planta_baja.txt" , "/Recursos/Mapas/Imagen/mapa_planta_baja.png" , 16, 14);
        
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");
        //nivel.setAcertijoEnTile(22, 9); TODO: hay que ajustar el panel de acertijo y que coincida cuando implementemos el iventario
        nivel.agregarEnemigo(new Enemigo(100, 10, 1, 4, 11));
        nivel.agregarEnemigo(new Enemigo(100, 10, 2, 22, 3));

        ItemMedicina medicina1 = new ItemMedicina(10,13,10);
        nivel.agregarItem(medicina1);

        ItemMedicina medicina2 = new ItemMedicina(25,13,10);
        nivel.agregarItem(medicina2);
        nivel.agregarZona(new ZonaMensaje(22, 8, 1, "Esto parece una puerta..."));
       
        nivel.agregarZona(new ZonaMensaje(18, 6, 1, "Esta roto, no puedo pasar poca acá"));

        nivel.agregarZona(new PuntoFlashback(25, 8, 2));

        // Estaria bueno cambiar la manera de crear itemes, tengo una idea preguntarme -- ISKARI

        //Item lataDeAceite = new Item("Lata de Aceite", "/Recursos/Sprites/Items/lata_aceite.png", 5, 10);
       // nivel.agregarItem(lataDeAceite);

        return nivel;
    }

    private Nivel crearNivel2() {
        Acertijo acertijo = new AcertijoObjeto (2, "¿Cuál es el animal más rápido del mundo?", "palanca");
        
        Nivel nivel = new Nivel(2, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);

        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");

        ItemMedicina medicina1 = new ItemMedicina(20,9,7);
        nivel.agregarItem(medicina1);

        ItemMedicina medicina2 = new ItemMedicina(10,9,25);
        nivel.agregarItem(medicina2);

        ItemMedicina medicina3 = new ItemMedicina(2,11,25);
        nivel.agregarItem(medicina3);

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

        nivel.agregarHabitacion(crearHabitacionPalanca());

        //el flashback automatico del gato al acercarse a tal punto del sotano
        //queda ajustar la x, y y el radio con las coordenadas exactas
        nivel.agregarZona(new PuntoFlashback(25, 9, 2));

        //los mensajes tipo sh que dijimos
        //hay q ajustarlos todavia (no se leer coordenadas)

        nivel.agregarZona(new ZonaMensaje(15, 8, 2, "Huele a sangre..."));
        nivel.agregarZona(new ZonaMensaje(2, 8, 2, "El aire se siente muy pesado."));

        return nivel;
    }


    private Nivel crearNivel3() {
        Acertijo acertijo = new AcertijoNumerico(2, "¿Cuántas vidas tiene un gato?", 9);

        Nivel nivel = new Nivel(2, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 7, 13);
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");
        nivel.agregarEnemigo(new Enemigo(100, 10, 1, 800, 80));
        nivel.agregarEnemigo(new Enemigo(100, 10, 2, 550, 320));

        return nivel;
    }

    private Habitacion crearHabitacionPalanca() {
    Acertijo acertijoPuerta = new AcertijoNumerico(3, "La puerta tiene una cerradura numerada. ¿Cuál es el código?", 1234);

    Habitacion habitacion = new Habitacion(
            "Cuarto de la palanca",
            acertijoPuerta,
            "Recursos/Mapas/Grid/grid_habitacion_sotano.txt",
            "/Recursos/Mapas/Imagen/habitacion_sotano.png",
            0, 0,       // posicionInicialX/Y heredado de EspacioBase — ver nota abajo
            300, 200,   // TODO: puertaX/puertaY reales, la posición de la puerta en el mapa del sótano
            5, 5        // TODO: puntoAccesoX/puntoAccesoY reales, dónde aparece el personaje dentro del cuarto
    );

    //Item palanca = new Item("Palanca", "/Recursos/Sprites/Items/palanca.png", 3, 3);
   //habitacion.agregarItem(palanca);

    return habitacion;
}


}


    

