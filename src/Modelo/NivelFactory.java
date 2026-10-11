package Modelo;

import java.util.function.BiFunction;

public class NivelFactory {

    //SPRITES ENTIDADES

    private static final String SPRITES_NURSE = "/Recursos/Sprites/Enemigos/Nurse/";
    private static final String SPRITES_WATCHER = "/Recursos/Sprites/Enemigos/Watcher/";
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

    private Nivel crearNivel1() {
        // 1. Se declara e instancia el acertijo
        Acertijo candadoPlantaBaja = new AcertijoNumerico(1, "", 14, 8, 1234, "/Recursos/UI/Acertijos/Candado/Fondo/candado.png", 0.48);
       
        // 2. Se le pasa 'acertijo' al constructor de Nivel
        Nivel nivel = new Nivel(1, candadoPlantaBaja, "Recursos/Mapas/Grid/grid_planta_baja.txt", "/Recursos/Mapas/Imagen/mapa_planta_baja2.png", 16, 14);

        agregarVarias(nivel, (columna, fila) -> new EnemigoComun(100, 10, nextId++, columna, fila, SPRITES_NURSE), new int[]{4, 11}, new int[]{22, 4});
        agregarVariosItems(nivel, (columna, fila) -> new ItemMedicina(columna, fila, 25), new int[]{10, 13}, new int[]{25, 13});
        agregarVarias(nivel, (columna, fila) -> new Rata(columna, fila), new int[]{27, 14}, new int[]{6, 13});
        
        //pruebaenemigo sensible a la luz
        agregarVarias(nivel, (columna, fila) -> new EnemigoWatcher(100, 10, nextId++, columna, fila, SPRITES_WATCHER), new int[]{26, 9});
        
        nivel.agregarZona(new ZonaMensaje(22, 8, 1, "Esto parece una puerta..."));
        nivel.agregarZona(new ZonaMensaje(18, 6, 2, "Falta la mitad de los escalones. Tiene que haber otro camino."));
        nivel.agregarZona(new ZonaMensaje(10, 13, 2, "El ascensor no responde. Está muerto."));
        nivel.agregarZona(new PuntoFlashback(25, 8, 2));

        Item llave = new Item("llave", "/Recursos/Sprites/Items/llave.png", 21, 5);
        llave.setEscala(0.5);
        nivel.agregarItem(llave);

        nivel.agregarHabitacion(crearHabitacionSecreta()); //la habitacion secreta, que se abre con la llave

        return nivel;
    }

    private Nivel crearNivel2() {
        Acertijo palancaAscensor = new AcertijoObjeto (2, "La puerta esta trabada",20, 10, "palanca");
        
        Nivel nivel = new Nivel(2, palancaAscensor, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);

        agregarVariosItems(nivel, (columna, fila) -> new ItemMedicina(columna, fila, 20), new int[]{20, 9}, new int[]{10, 9}, new int[]{2, 11});

        agregarVariosItems(nivel, (columna, fila) -> new ItemBateria(columna, fila), new int[]{4, 7}, new int[]{4, 12}, new int[]{19, 4});

        agregarVarias(nivel,(columna, fila) -> new EnemigoComun(100, 10, nextId++, columna, fila, SPRITES_NURSE), new int[]{15, 9}, new int[]{2, 8});

        nivel.agregarHabitacion(crearHabitacionPalanca());

        //el flashback automatico del gato al acercarse a tal punto del sotano
        //queda ajustar la x, y y el radio con las coordenadas exactas
        nivel.agregarZona(new PuntoFlashback(25, 9, 2));

        //los mensajes tipo sh que dijimos
        //hay q ajustarlos todavia (no se leer coordenadas)

        nivel.agregarZona(new ZonaMensaje(15, 8, 2, "Huele a sangre..."));
        nivel.agregarZona(new ZonaMensaje(2, 8, 2, "El aire se siente muy pesado."));

        agregarVarias(nivel, (columna, fila) -> new EnemigoFotosensible(100, 20, nextId++, columna, fila, SPRITES_WATCHER), new int[]{19, 4});
        return nivel;
    }


    
        private Habitacion crearHabitacionPalanca() {
            Acertijo acertijoPuerta = new AcertijoNumerico(3,
                    "",
                    18, 2,
                    1234,
                    "/Recursos/UI/Acertijos/Panel/panel.png");

            return new Habitacion("Cuarto de la palanca",
                    acertijoPuerta,
                    "Recursos/Mapas/Grid/grid_habitacion_sotano.txt",
                    "/Recursos/Mapas/Imagen/habitacion_sotano.png",
                    7, 6,        // posicionInicial: tile donde aparece el personaje adentro
                    18, 2);     // puerta
        }

        private Habitacion crearHabitacionSecreta() {
            Acertijo acertijoPuerta = new AcertijoObjeto(4,
                    "Necesito algo para abrir esta puerta",
                    22, 8,
                    "llave")
;

            return new Habitacion("Cuarto de la puerta",
                    acertijoPuerta,
                    "Recursos/Mapas/Grid/grid_habitacion_sotano.txt",
                    "/Recursos/Mapas/Imagen/habitacion_sotano.png",
                    7, 6,        // posicionInicial: tile donde aparece el personaje adentro
                    22, 9);     // puerta
        }

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


    

