package Vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Sistema de particulas de polvo para el fondo del menu.
// Uso: actualizar() en cada tick del Timer, dibujar(g2) en paintComponent.
public class ParticulasAmbiente {

    private static class Particula {
        double x, y;
        double velocidadY;      // negativa = sube
        double amplitudVaiven;  // cuanto se mueve de costado
        double fase;            // desfasa el vaiven de cada particula
        double radio;
        int vida, vidaMaxima;
        int alphaMaximo;
    }

    private final List<Particula> particulas = new ArrayList<>();
    private final Random random = new Random();
    private int ancho;
    private int alto;

    public ParticulasAmbiente(int cantidad, int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        for (int i = 0; i < cantidad; i++) {
            Particula p = new Particula();
            reiniciar(p);
            p.vida = random.nextInt(p.vidaMaxima); // arrancan en etapas distintas
            particulas.add(p);
        }
    }

    // Llamar desde el panel si cambia de tamanio (componentResized)
    public void setTamanio(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    private void reiniciar(Particula p) {
        p.x = random.nextDouble() * ancho;
        p.y = random.nextDouble() * alto;
        p.velocidadY = -(0.1 + random.nextDouble() * 0.35);
        p.amplitudVaiven = 0.2 + random.nextDouble() * 0.6;
        p.fase = random.nextDouble() * Math.PI * 2;
        p.radio = 0.8 + random.nextDouble() * 2.2;
        p.vidaMaxima = 300 + random.nextInt(400); // en ticks (~5 a 12 seg a 60 FPS)
        p.vida = 0;
        p.alphaMaximo = 40 + random.nextInt(90);
    }

    public void actualizar() {
        for (Particula p : particulas) {
            p.vida++;
            p.y += p.velocidadY;
            p.x += Math.sin(p.vida * 0.02 + p.fase) * p.amplitudVaiven;

            boolean fueraDePantalla = p.y < -10 || p.x < -10 || p.x > ancho + 10;
            if (p.vida >= p.vidaMaxima || fueraDePantalla) {
                reiniciar(p);
            }
        }
    }

    public void dibujar(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Particula p : particulas) {
            // Fade in / fade out: 0 al nacer, maximo a mitad de vida, 0 al morir
            double progreso = (double) p.vida / p.vidaMaxima;
            double intensidad = Math.sin(progreso * Math.PI);
            int alpha = (int) (p.alphaMaximo * intensidad);
            alpha = Math.max(0, Math.min(255, alpha));

            g2.setColor(new Color(200, 190, 170, alpha)); // gris calido tipo polvo
            double d = p.radio * 2;
            g2.fillOval((int) (p.x - p.radio), (int) (p.y - p.radio), (int) d, (int) d);
        }
    }
}