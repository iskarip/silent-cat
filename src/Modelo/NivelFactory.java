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
            case 3:
                return crearNivel3(); //la planta alta
            default:
                throw new NivelInvalidoException("No existe el nivel " + numeroNivel);
        }
    }

    private Nivel crearNivel1() {
        // 1. Se declara e instancia el acertijo
        Acertijo candadoPlantaBaja = new AcertijoNumerico(1, "Necesito una clave...", 14, 8, 4271, "/Recursos/UI/Acertijos/Candado/Fondo/candado.png", 0.48);
       
        // 2. Se le pasa 'acertijo' al constructor de Nivel
        Nivel nivel = new Nivel(1, candadoPlantaBaja, "Recursos/Mapas/Grid/grid_planta_baja.txt", "/Recursos/Mapas/Imagen/mapa_planta_baja2.png", 16, 14);
        nivel.setTipoSuelo(TipoSuelo.MADERA);
        agregarVarias(nivel, (columna, fila) -> new EnemigoComun(100, 10, nextId++, columna, fila, SPRITES_NURSE), new int[]{4, 11}, new int[]{22, 4});
        agregarVariosItems(nivel, (columna, fila) -> new ItemMedicina(columna, fila, 25), new int[]{10, 13}, new int[]{25, 13});
        agregarVarias(nivel, (columna, fila) -> new Rata(columna, fila), new int[]{15, 10});
        
        //pruebaenemigo sensible a la luz
        agregarVarias(nivel, (columna, fila) -> new EnemigoWatcher(100, 10, nextId++, columna, fila, SPRITES_WATCHER), new int[]{26, 9});
        
        nivel.agregarZona(new ZonaMensaje(22, 8, 1, "Esto parece una puerta..."));
        nivel.agregarZona(new ZonaMensaje(18, 6, 2, "Faltan la mitad de los escalones. Tiene que haber otro camino."));
        nivel.agregarZona(new ZonaMensaje(7, 9, 1, "El ascensor no responde. Está muerto."));
        nivel.agregarZona(new PuntoFlashback(25, 8, 2));

        Item llave = new Item("llave", "/Recursos/Sprites/Items/llave.png", 21, 5);
        llave.setEscala(0.5);
        nivel.agregarItem(llave);

        nivel.agregarHabitacion(crearHabitacionSecreta()); //la habitacion secreta, que se abre con la llave

        return nivel;
    }

    private Nivel crearNivel2() {
            Acertijo palancaAscensor = new AcertijoObjeto (2, "Si encuentro algo para hacer palanca, quizás ceda.",10, 2, "palanca");
            
            Nivel nivel = new Nivel(2, palancaAscensor, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);
            nivel.setTipoSuelo(TipoSuelo.METAL);

            agregarVariosItems(nivel, (columna, fila) -> new ItemMedicina(columna, fila, 20), new int[]{20, 9}, new int[]{10, 9}, new int[]{2, 11});

            agregarVariosItems(nivel, (columna, fila) -> new ItemBateria(columna, fila), new int[]{4, 7}, new int[]{4, 12}, new int[]{19, 4});

            agregarVarias(nivel,(columna, fila) -> new EnemigoComun(100, 10, nextId++, columna, fila, SPRITES_NURSE), new int[]{15, 9}, new int[]{2, 8});

            nivel.agregarHabitacion(crearHabitacionPalanca());

            nivel.agregarZona(new PuntoFlashback(25, 9, 2));

            nivel.agregarZona(new ZonaMensaje(15, 8, 2, "Huele a sangre..."));
            nivel.agregarZona(new ZonaMensaje(2, 8, 2, "El aire se siente muy pesado."));
            nivel.agregarZona(new ZonaMensaje(10, 2, 1, "Este ascensor parece funcionar, pero la puerta está atascada. \n Necesito algo para hacer palanca."));
            agregarVarias(nivel, (columna, fila) -> new EnemigoWatcher(100, 20, nextId++, columna, fila, SPRITES_WATCHER), new int[]{19, 4});

            ItemPista nota2 = new ItemPista("nota2", "/Recursos/Sprites/Items/nota2.png", "/Recursos/Sprites/Items/nota2.png", 4, 6);
            nota2.setMensajeRecoger("Una nota arrugada...");
            nivel.agregarItem(nota2);

            return nivel;
        }

        private Nivel crearNivel3 (){

            Nivel nivel = new Nivel(3, "Recursos/Mapas/Grid/grid_planta_alta.txt","/Recursos/Mapas/Imagen/mapa_planta_alta.png", 3, 5);

            nivel.agregarEntidad(new Gato(11, 3));
            nivel.agregarZona(new ZonaMensaje(11, 3, 2, "Te encontre <3 UwU"));
            
            return nivel;

        }


        
        private Habitacion crearHabitacionPalanca() {
            Acertijo acertijoPuerta = new AcertijoNumerico(3,
                    "Necesito una clave...",
                    18, 2,
                    8634,
                    "/Recursos/UI/Acertijos/Panel/panel.png");

            Habitacion habitacion = new Habitacion("Cuarto de la palanca",
                    acertijoPuerta,
                    "Recursos/Mapas/Grid/grid_habitacion_sotano.txt",
                    "/Recursos/Mapas/Imagen/habitacion_sotano.png",
                    7, 6,        // posicionInicial
                    19, 2);      // puerta
            habitacion.setTipoSuelo(TipoSuelo.METAL);
            Item palanca = new Item("palanca", "/Recursos/Sprites/Items/palanca.png", 1, 3);
            habitacion.agregarZona(new ZonaMensaje(1, 3, 1, "Una tuberia rota. Quizás sea de ayuda."));
            palanca.setEscala(0.5);
            habitacion.agregarItem(palanca);

            return habitacion;
        }

        private Habitacion crearHabitacionSecreta() {
            Acertijo acertijoPuerta = new AcertijoObjeto(4,
                    "Necesito algo para abrir esta puerta",
                    22, 8,
                    "llave")
;

           Habitacion habitacion = new Habitacion("Cuarto de la puerta",
                    acertijoPuerta,
                    "Recursos/Mapas/Grid/grid_habitacion_planta_baja.txt",
                    "/Recursos/Mapas/Imagen/habitacion_planta_baja.png",
                    9, 6,        // posicionInicial: tile donde aparece el personaje adentro
                    22, 9);     // puerta

            ItemPista nota1 = new ItemPista("nota1", "/Recursos/Sprites/Items/nota1.png", "/Recursos/Sprites/Items/nota1.png", 8, 5);
            nota1.setMensajeRecoger("Una nota arrugada...");
            habitacion.agregarItem(nota1);
            habitacion.agregarZona(new ZonaMensaje(9, 1, 1, "Mmm... esto parece el codigo para algo..."));
            habitacion.agregarZona(new ZonaMensaje(6,3,2, "..." ));
            
            return habitacion;
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


    

