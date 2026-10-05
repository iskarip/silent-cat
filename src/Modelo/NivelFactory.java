package Modelo;

import java.util.function.BiFunction;

public class NivelFactory {

    //SPRITES ENTIDADES

    private static final String SPRITES_NURSE = "/Recursos/Sprites/Enemigos/Nurse/";
    private int nextId = 1;

    // Sirve para crear niveles según su número. Si el número no es válido, lanza una excepción.
    // Se puede ampliar para crear más niveles en el futuro.

    public Nivel crearNivel(int numeroNivel) throws NivelInvalidoException {
        switch (numeroNivel) {
            
            case 1:
                return crearNivel1(); //la planta baja 
            case 2:
                return crearNivel2(); //el sotano
            default:
                throw new NivelInvalidoException("No existe el nivel " + numeroNivel);
        }
    }

    private Nivel crearNivel1(){

        Acertijo candadoPlantaBaja = new AcertijoNumerico(1, "Ingresar código al candado", 14, 8, 1234, "/Recursos/Sprites/Acertijos/candado.png");
       
        Nivel nivel= new Nivel(1, candadoPlantaBaja , "Recursos/Mapas/Grid/grid_planta_baja.txt" , "/Recursos/Mapas/Imagen/mapa_planta_baja.png" , 16, 14);
        
        agregarVarias(nivel,(columna, fila) -> new Enemigo(100, 10, nextId++, columna, fila, SPRITES_NURSE), new int[]{4, 11}, new int[]{22, 3});

        //nivel.setAcertijoEnTile(14, 9); 

        agregarVariosItems(nivel, (columna,fila) -> new ItemMedicina (columna, fila, 25), new int[]{10, 13}, new int[]{25, 13});
        
        agregarVarias(nivel,(columna, fila) -> new Rata (columna, fila), new int[]{15, 12});
        
        nivel.agregarZona(new ZonaMensaje(22, 8, 1, "Esto parece una puerta..."));
        nivel.agregarZona(new ZonaMensaje(18, 6, 2, "Esta roto, no puedo pasar por acá"));

        nivel.agregarZona(new PuntoFlashback(25, 8, 2));

        Item llave = new Item("llave", "/Recursos/Sprites/Items/llave.png", 15, 12);
        llave.setEscala(0.5);
        llave.setMensajeRecoger("Una llave... tal vez abra alguna puerta.");
        nivel.agregarItem(llave); // ESTA LLAVE ES PURAMENTE DE PRUEBA

        return nivel;
    }

    private Nivel crearNivel2() {
        Acertijo palancaAscensor = new AcertijoObjeto (2, "La puerta esta trabada",20, 10, "palanca");
        
        Nivel nivel = new Nivel(2, palancaAscensor, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);


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

        agregarVarias(nivel,(columna, fila) -> new Enemigo(100, 10, nextId++, columna, fila, SPRITES_NURSE), new int[]{15, 9}, new int[]{2, 8});

      //  nivel.agregarHabitacion(crearHabitacionPalanca());

        //el flashback automatico del gato al acercarse a tal punto del sotano
        //queda ajustar la x, y y el radio con las coordenadas exactas
        nivel.agregarZona(new PuntoFlashback(25, 9, 2));

        //los mensajes tipo sh que dijimos
        //hay q ajustarlos todavia (no se leer coordenadas)

        nivel.agregarZona(new ZonaMensaje(15, 8, 2, "Huele a sangre..."));
        nivel.agregarZona(new ZonaMensaje(2, 8, 2, "El aire se siente muy pesado."));

        return nivel;
    }


  //  private Nivel crearNivel3() {
  //      Acertijo acertijo = new AcertijoNumerico(2, "¿Cuántas vidas tiene un gato?", 9);

  //      Nivel nivel = new Nivel(2, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 7, 13);

 //       return nivel;
 //   }

  /*    private Habitacion crearHabitacionSecreta() {
    Acertijo acertijoPuerta = new AcertijoObjeto(3,"Necesita una llave",22,8 ,"llave");

  Habitacion habitacion = new Habitacion(
            "Habitación Secreta",
            acertijoPuerta,
            "Recursos/Mapas/Grid/grid_habitacion_sotano.txt",
            "/Recursos/Mapas/Imagen/habitacion_sotano.png",
            6, 7,       // posicionInicialX/Y heredado de EspacioBase — ver nota abajo
            300, 200,   // TODO: puertaX/puertaY reales, la posición de la puerta en el mapa del sótano
            6, 7        // TODO: puntoAccesoX/puntoAccesoY reales, dónde aparece el personaje dentro del cuarto
    );

    return habitacion;
    }
 */
    

    private void agregarVarias(Nivel nivel, BiFunction<Integer, Integer, Entidad> creador, int[]... posiciones) {
        for (int[] pos : posiciones) {
            nivel.agregarEntidad(creador.apply(pos[0], pos[1]));
        }
    }

    private void agregarVariosItems(Nivel nivel, BiFunction<Integer, Integer, Item> creador, int[]... posiciones) {
        for (int[] pos : posiciones) {
            nivel.agregarItem(creador.apply(pos[0], pos[1]));
        }
    }

}


    

