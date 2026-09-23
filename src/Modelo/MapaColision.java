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
    public static final int TILE = 32; // Tamaño de cada tile en píxeles (32x32), Es public para que otras clases puedan usarlo para calcular posiciones en píxeles a partir de coordenadas de tile.

    public void cargar(String rutaArchivo) { // Lee el archivo de texto y construye la matriz de colisión
        List<String> lineas = new ArrayList<>();

        // 1. Intento leer desde recursos (Classpath)
        java.io.InputStream is = getClass().getResourceAsStream(rutaArchivo.startsWith("/") ? rutaArchivo : "/" + rutaArchivo);

        try {
            BufferedReader lector;
            if (is != null) {
                lector = new BufferedReader(new java.io.InputStreamReader(is));
            } else {
                // 2. Si no es recurso interno, lee como archivo del disco
                lector = new BufferedReader(new FileReader(rutaArchivo));
            }

            try (lector) {
                String linea;
                while ((linea = lector.readLine()) != null) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando el mapa de colisión desde " + rutaArchivo + ": " + e.getMessage());
            return;
        }

        if (lineas.isEmpty()) {
            System.out.println("El archivo de mapa está vacío: " + rutaArchivo);
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
        System.out.println("Mapa de colisión cargado con éxito: " + columnas + "x" + filas);
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

    public int getFilas() {
    return (mapa == null) ? 0 : mapa.length;
    }

    public int getColumnas() {
        return (mapa == null || mapa.length == 0) ? 0 : mapa[0].length;
    }

}