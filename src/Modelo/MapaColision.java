package Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Representa la matriz de colisión de un espacio jugable (un Nivel o una
// Habitacion). Se encarga solo de leer el archivo .txt y responder si una
// posición en píxeles es caminable.

public class MapaColision {

    private boolean[][] mapa;
    private static final int TILE = 32;

    public void cargar(String rutaArchivo) { // Lee el archivo de texto y construye la matriz de colisión
        List<String> lineas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error cargando el mapa de colisión: " + e.getMessage());
            return;
        }

        int filas = lineas.size();
        int columnas = lineas.get(0).length();
        mapa = new boolean[filas][columnas];

        for (int f = 0; f < filas; f++) {
            String linea = lineas.get(f);
            for (int c = 0; c < columnas; c++) {
                if (c < linea.length()) {
                    mapa[f][c] = (linea.charAt(c) == '0');
                } else {
                    mapa[f][c] = false;
                }
            }
        }
    }

    public boolean esPosicionValida(int pixelX, int pixelY) { // Dado un par de coordenadas en píxeles, devuelve true si la posición es caminable (0) y false si no lo es (1). Si el mapa no está cargado, devuelve true por defecto.
        if (mapa == null) return true;

        int columna = pixelX / TILE;
        int fila = pixelY / TILE;

        if (fila < 0 || fila >= mapa.length) return false;
        if (columna < 0 || columna >= mapa[0].length) return false;

        return mapa[fila][columna];
    }

    public boolean esRectanguloValido(int x, int y, int ancho, int alto) { // Dado un rectángulo definido por su esquina superior izquierda (x, y) y sus dimensiones (ancho, alto), devuelve true si todas las esquinas del rectángulo son caminables (0) y false si alguna de ellas no lo es (1). Si el mapa no está cargado, devuelve true por defecto.
       
        if (mapa == null) return true;

        if (!esPosicionValida(x, y)) return false;
        if (!esPosicionValida(x + ancho, y)) return false;
        if (!esPosicionValida(x, y + alto)) return false;
        if (!esPosicionValida(x + ancho, y + alto)) return false;
       
        return true;
    }

}