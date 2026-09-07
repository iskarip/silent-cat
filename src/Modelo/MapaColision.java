package Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la matriz de colisión de un espacio jugable (un Nivel o una
 * Habitacion). Se encarga solo de leer el archivo .txt y responder si una
 * posición en píxeles es caminable.
 */

public class MapaColision {

    private boolean[][] mapa;
    private static final int TILE = 32;

    public void cargar(String rutaArchivo) {
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
                mapa[f][c] = (linea.charAt(c) == '0');
            }
        }
    }

    public boolean esPosicionValida(int pixelX, int pixelY) {
        if (mapa == null) return true;

        int columna = pixelX / TILE;
        int fila = pixelY / TILE;

        if (fila < 0 || fila >= mapa.length) return false;
        if (columna < 0 || columna >= mapa[0].length) return false;

        return mapa[fila][columna];
    }
}