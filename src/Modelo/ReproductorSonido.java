package Modelo;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*; //el * le dice a java que importe todas las clases de este paquete, porque voy a usar varias cosas del mismo lugar.

public class ReproductorSonido {
      private static final List<Clip> clipsActivos = new ArrayList<>(); 
    
    public static void reproducir(String ruta){ //porque la accion de reproducir un sonido no depende de ningun estado propio de un objeto en particular, es mas una utilidad
            try{ //el try y catch es como un manejo de excepciones, es para que no se rompa feo
                AudioInputStream audioIn= AudioSystem.getAudioInputStream //es para que java pueda interpretar y leer el sonido
                (ReproductorSonido.class.getClassLoader().getResource(ruta)); //busca el archivo dentro de la carpeta del proyecto
            
                Clip clip= AudioSystem.getClip();
                clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    clipsActivos.remove(clip);
                }
            });
                
                clip.open(audioIn);
                clip.start();
                }
                catch(Exception e){
                    System.out.println("No se pudo reproducir el sonido: " + e.getMessage());
                }
            }


    }
