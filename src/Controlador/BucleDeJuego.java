package Controlador;

import javax.swing.Timer;
import java.awt.Toolkit;

public class BucleDeJuego implements CicloDeVida {

    private static final double TIEMPO_OPTIMO_NS = 1_000_000_000.0 / 60.0;
    private long tiempoAnterior = 0;
    private double acumulador = 0.0;
    private final Timer timer;

    private final Runnable accionActualizar;
    private final Runnable accionPintar;
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
        if (verificadorPausa.estaPausadoOInactivo()) {
            tiempoAnterior = System.nanoTime();
            acumulador = 0.0;
            accionPintar.run();
            Toolkit.getDefaultToolkit().sync();
            return;
        }

        long tiempoActual = System.nanoTime();
        double transcurrido = (double) (tiempoActual - tiempoAnterior);
        tiempoAnterior = tiempoActual;

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

        if (acumulador > TIEMPO_OPTIMO_NS) {
            acumulador = 0.0;
        }

        accionPintar.run();
        Toolkit.getDefaultToolkit().sync();
    }
}