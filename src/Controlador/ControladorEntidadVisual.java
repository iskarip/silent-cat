package Controlador;

import Modelo.Direccion;
import Modelo.Entidad;
import Modelo.PoseEntidadCombatible;
import Vista.EntidadVisual;
import Vista.EstadoAnimacion;
import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.Map;

// Adapta una Entidad del Modelo al contrato EntidadVisual que pide la Vista.
// Es el único lugar donde se traducen conceptos del Modelo (pose, dirección, hitbox)
// a conceptos de la Vista (animación, fila de sprite, base para ordenar).
public class ControladorEntidadVisual implements EntidadVisual {

    private static final Map<PoseEntidadCombatible, EstadoAnimacion> ANIMACION_POR_POSTURA =
            new EnumMap<>(PoseEntidadCombatible.class);

    static {
        ANIMACION_POR_POSTURA.put(PoseEntidadCombatible.SENTADO, EstadoAnimacion.SENTADO);
        ANIMACION_POR_POSTURA.put(PoseEntidadCombatible.LEVANTANDOSE, EstadoAnimacion.LEVANTANDOSE);
    }

    private final Entidad entidad;

    public ControladorEntidadVisual(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad es obligatoria");
        }
        this.entidad = entidad;
    }

    @Override public int getX() { return entidad.getPosicionX(); }
    @Override public int getY() { return entidad.getPosicionY(); }

    @Override
    public int getBaseY() {
        Rectangle hitbox = entidad.getHitbox();
        return hitbox.y + hitbox.height;
    }

    @Override public String getRutaSprites() { return entidad.getRutaSprites(); }
    @Override public double getEscala() { return entidad.getEscalaSprite(); }

    @Override
    public int getFila() {
        Direccion direccion = entidad.getDireccion();
        return (direccion != null) ? direccion.getFila() : 0;
    }

    // Primero la pose especial (sentado, levantándose), después ataque, después movimiento
    @Override
    public EstadoAnimacion getEstadoAnimacion() {
        EstadoAnimacion porPostura = ANIMACION_POR_POSTURA.get(entidad.getPostura());
        if (porPostura != null) return porPostura;
        if (entidad.estaAtacando()) return EstadoAnimacion.ATACANDO;
        return entidad.estaMoviendose() ? EstadoAnimacion.CAMINANDO : EstadoAnimacion.IDLE;
    }

    @Override public boolean esTransicion() { return entidad.getPostura().esTransicion(); }
    @Override public double getProgresoTransicion() { return entidad.getProgresoPostura(); }
    @Override public boolean mostrarBarraVida() { return entidad.mostrarBarraVida(); }
    @Override public int getPorcentajeVida() { return entidad.getPorcentajeVida(); }
}
 