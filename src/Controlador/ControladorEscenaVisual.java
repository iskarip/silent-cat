package Controlador;

import Modelo.Entidad;
import Modelo.EspacioJugable;
import Modelo.Item;
import Modelo.MapaColision;
import Vista.EntidadVisual;
import Vista.EscenaVisual;
import Vista.ItemVisual;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

// Adapta un EspacioJugable del Modelo (Nivel o Habitacion) al contrato EscenaVisual de la Vista.
// Trabaja con la interfaz EspacioJugable, así que sirve igual para un nivel que para una habitación.
public class ControladorEscenaVisual implements EscenaVisual {

    private final EspacioJugable espacio;

    // Un adaptador por entidad, para no crear objetos nuevos en cada cuadro (60 por segundo)
    private final Map<Entidad, ControladorEntidadVisual> adaptadoresEntidad = new IdentityHashMap<>();
    private final Map<Item, ControladorItemVisual> adaptadoresItem = new IdentityHashMap<>();

    public ControladorEscenaVisual(EspacioJugable espacio) {
        if (espacio == null) {
            throw new IllegalArgumentException("El espacio es obligatorio");
        }
        this.espacio = espacio;
    }

    @Override
    public String getRutaImagenFondo() {
        return espacio.getRutaImagenFondo();
    }

    @Override
    public int getAnchoPx() {
        MapaColision mapa = espacio.getMapaColision();
        return (mapa == null) ? 0 : mapa.getColumnas() * MapaColision.TILE;
    }

    @Override
    public int getAltoPx() {
        MapaColision mapa = espacio.getMapaColision();
        return (mapa == null) ? 0 : mapa.getFilas() * MapaColision.TILE;
    }

    @Override
    public int getTamanioTile() {
        return MapaColision.TILE;
    }

    // Solo los ítems que todavía no se recogieron
    @Override
    public List<ItemVisual> getItems() {
        List<ItemVisual> visibles = new ArrayList<>();
        if (espacio.getListaItems() == null) return visibles;

        for (Item item : espacio.getListaItems()) {
            if (!item.isRecogido()) {
                visibles.add(adaptadoresItem.computeIfAbsent(item, ControladorItemVisual::new));
            }
        }
        return visibles;
    }

    // Solo las entidades vivas
    @Override
    public List<EntidadVisual> getEntidades() {
        List<EntidadVisual> visibles = new ArrayList<>();
        if (espacio.getListaEntidades() == null) return visibles;

        for (Entidad entidad : espacio.getListaEntidades()) {
            if (entidad.estaVivo()) {
                visibles.add(adaptadoresEntidad.computeIfAbsent(entidad, ControladorEntidadVisual::new));
            }
        }
        return visibles;
    }
}