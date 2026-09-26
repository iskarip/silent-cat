package Modelo;

public class NivelFactory {

    public Nivel crearNivel(int numeroNivel) throws NivelInvalidoException {
        switch (numeroNivel) {
            //case 1: va a ser el de la puerta oxidada, es la excepcion
            //nivel 2: es la excepcion
            //nivel 3 es el que ya tiene todos nuestras cosas cargadas por ahora
            
            case 1:
                return crearNivel3(); // temporal porque por ahora tenemos ese nivel cargado
            default:
                throw new NivelInvalidoException("No existe el nivel " + numeroNivel);
        }
    }

    //idea a agregar con acertijo e inventario
    private Nivel crearNivel1(){
        Acertijo acertijo = new AcertijoObjeto(1, "La puerta esta atascada por el oxido y no cede. Necesitas lubricar las bisagras.", "Lata de aceite");
    
        //aun no esta el mapa ni el gri, es intuitivo para agregarlo con estos nombres luego cuando termine la imagen.
        Nivel nivel= new Nivel(1, acertijo, "Recursos/Mapas/Grid/grid.planta_baja.txt" , "Recursos/Mapas/Imagen/mapa_planta_baja.png" , 7, 13);
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");
        nivel.setAcertijoX(300); //TODO: ajustar a la posicion real de la futura puerta secreta que estoy haciendo(mica) en el mapa
        nivel.setAcertijoY(200);
        nivel.agregarEnemigo(new Enemigo(100, 10, 1, 800, 80));
        nivel.agregarEnemigo(new Enemigo(100, 10, 2, 550, 320));

//el objeto que la puerta oxidada necesita para abrirse. El nombre tiene que coincidir EXACTO
//sin importar mayusculas, con el que le pasemos a acertijoObjeto de arriba ("Lata de Aceite") 
//porque asi lo relaciona inventario.contieneItem
//TTODO: ajustar la posicion (columna, fila) a donde la vamos a esconder en el mapa owo

        Item lataDeAceite = new Item("Lata de Aceite", "/Recursos/Sprites/Items/lata_aceite.png", 5, 10);
        nivel.agregarItem(lataDeAceite);

        return nivel;
    }

    private Nivel crearNivel2() {
        Acertijo acertijo = new AcertijoNumerico(2, "¿Cuántas vidas tiene un gato?", 9);

        Nivel nivel = new Nivel(2, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 7, 13);
        nivel.setRutaSpritesEnemigos("/Recursos/Sprites/Enemigos/Nurse/");
        nivel.setAcertijoX(300); // TODO: ajustar estos valores segun donde se quiera que este el acertijo en el mapa
        nivel.setAcertijoY(200);
        nivel.agregarEnemigo(new Enemigo(100, 10, 1, 800, 80));
        nivel.agregarEnemigo(new Enemigo(100, 10, 2, 550, 320));

        return nivel;
    }





    private Nivel crearNivel3() {
        Acertijo acertijo = new AcertijoNumerico(3, "¿Cuál es el animal más rápido del mundo?", 1);
        
        Nivel nivel = new Nivel(3, acertijo, "Recursos/Mapas/Grid/grid_sotano.txt","/Recursos/Mapas/Imagen/mapa_sotano.png", 12, 6);
        
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


    

