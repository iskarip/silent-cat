package Controlador;

import Vista.NivelPanel;
import Vista.JuegoFrame;
import Vista.ReproductorSonido;
import java.awt.event.ActionListener;

public class ControladorGameOver implements CicloDeVida {

    private final NivelPanel vista;
    private final JuegoFrame ventanaPrincipal;
    private final ControladorNivel controladorNivel;

    private ActionListener alVolverMenu;
    private ActionListener alReintentar;

    public ControladorGameOver(NivelPanel vista, JuegoFrame ventanaPrincipal, ControladorNivel controladorNivel) {
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;
        this.controladorNivel = controladorNivel;

        configurarListeners();
    }

    private void configurarListeners() {
        // Acción de volver al menú principal
        this.alVolverMenu = e -> {
            controladorNivel.detener();
            vista.getFinDelJuego().setVisible(false);
            vista.reiniciarEstadoNivel();
            ventanaPrincipal.mostrarPantalla("menu");
            ReproductorSonido.reproducirEnLoop(ControladorPrincipal.MUSICA_MENU);
        };

        // Acción de reintentar desde checkpoint
        this.alReintentar = e -> controladorNivel.reintentarDesdeCheckpoint();

        // Asignación de listeners y sonidos de hover
        this.vista.getFinDelJuego().getBotonVolverMenu().addActionListener(this.alVolverMenu);
        this.vista.getFinDelJuego().getBotonVolverMenu().setAccionHover(() ->
                ReproductorSonido.reproducir("Recursos/Sonidos/UI/sonido3.wav")
        );

        this.vista.getFinDelJuego().getBotonReintentar().addActionListener(this.alReintentar);
        this.vista.getFinDelJuego().getBotonReintentar().setAccionHover(() ->
                ReproductorSonido.reproducir("Recursos/Sonidos/UI/sonido3.wav")
        );
    }

    @Override
    public void alDetener() {
        // Al detener el nivel, removemos los listeners para evitar la acumulación
        if (alVolverMenu != null) {
            vista.getFinDelJuego().getBotonVolverMenu().removeActionListener(alVolverMenu);
        }
        if (alReintentar != null) {
            vista.getFinDelJuego().getBotonReintentar().removeActionListener(alReintentar);
        }
    }
}