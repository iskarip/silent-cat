package Controlador;

import javax.swing.Timer;
import java.awt.Toolkit;

public class BucleDeJuego implements CicloDeVida {

    private static final double TIEMPO_OPTIMO_NS = 1_000_000_000.0 / 60.0;
    private long tiempoAnterior = 0;
    private double acumulador = 0.0;
    private final Timer timer; //java.swing.Timer

    private final Runnable accionActualizar; //que actualizar (viene de afuera)
    private final Runnable accionPintar; // que pintar ( viene de afuera)
    private final VerificadorPausa verificadorPausa;

    @FunctionalInterface
    public interface VerificadorPausa {
        boolean estaPausadoOInactivo();
    }

    public BucleDeJuego(Runnable accionActualizar, Runnable accionPintar, VerificadorPausa verificadorPausa) {
        this.accionActualizar = accionActualizar;
        this.accionPintar = accionPintar;
        this.verificadorPausa = verificadorPausa;
        
        // Timer de Swing que dispara cada 16ms (~60 FPS)
        this.timer = new Timer(16, e -> cicloPrincipal());
    }

    public void iniciar() {
        this.tiempoAnterior = System.nanoTime();
        this.acumulador = 0.0;
        timer.start();
    }

    public void detener() {
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public void alReanudar() {
    tiempoAnterior = System.nanoTime();
    acumulador = 0.0;
    }

    @Override
    public void alDetener() {
        detener();
    }

    private void cicloPrincipal() {

        //si esta pausado o inactivo: no actualiza nada, pero sigue pintando  
        if (verificadorPausa.estaPausadoOInactivo()) {
            tiempoAnterior = System.nanoTime();
            acumulador = 0.0;
            accionPintar.run();
            Toolkit.getDefaultToolkit().sync();
            return;
        }
//Si no, calcula cuanto tiempo paso desde la vuelta anterior y lo acumula.
//mientras haya acumulado un paso completo 1/60, ejecuta accion actualizar y lo descuenta. despues pinta
        long tiempoActual = System.nanoTime();
        double transcurrido = (double) (tiempoActual - tiempoAnterior);
        tiempoAnterior = tiempoActual;
//proteccion: si paso mas de 100ms, recorta el valor, hace como maximo 2 pasos por vuelta
//para no colgarse
        if (transcurrido > 100_000_000.0) {
            transcurrido = 100_000_000.0;
        }

        acumulador += transcurrido;

        int pasos = 0;
        while (acumulador >= TIEMPO_OPTIMO_NS && pasos < 2) {
            accionActualizar.run();
            acumulador -= TIEMPO_OPTIMO_NS;
            pasos++;
        }
//el acumulador es necesario si la compu es lenta, para que se acuerde del tiempo que falta
//da un pasito extra para compensar

//si la compu es rapida, espera.

//el resultado seria que el juego va a la misma velocidad en cualquier maquina
        if (acumulador > TIEMPO_OPTIMO_NS) {
            acumulador = 0.0;
        }

        accionPintar.run();
        Toolkit.getDefaultToolkit().sync();
    }
}