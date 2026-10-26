package Controlador;

import Vista.ReproductorSonido;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.PausaPanel;
import Modelo.Progreso;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

//Se agrego el uso de hilos en el guardarPartida
//La primera muestra que el trabajo pesado va en un hilo aparte.
// La segunda muestra que el resultado vuelve al hilo de eventos, 
//que es el único que puede tocar la interfaz. Si te preguntan por qué se hace así
//->la ventana no se traba y los componentes Swing no son seguros para hilos.


public class ControladorPausa {

    private final NivelPanel nivelPanel;
    private final PausaPanel pausaPanel;
    private final JuegoFrame ventanaPrincipal;
    private final ControladorNivel controladorNivel;
    private final ControladorProgreso controladorProgreso;
    private final ActionListener alPausar = e -> pausar();
    private final ActionListener alReanudar = e -> reanudar();
    private final ActionListener alVolverAlMenu = e -> volverAlMenu();
    private final ActionListener alGuardar = e -> guardarPartida();

    public ControladorPausa(NivelPanel nivelPanel, JuegoFrame ventanaPrincipal, ControladorNivel controladorNivel, ControladorProgreso controladorProgreso) {
        this.nivelPanel = nivelPanel;
        this.pausaPanel = nivelPanel.getPausaPanel();
        this.ventanaPrincipal = ventanaPrincipal;
        this.controladorNivel = controladorNivel;
        this.controladorProgreso = controladorProgreso;

        configurarEventos();
    }

    private void configurarEventos() {
        // Accionar pausa desde el botón flotante en pantalla
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().addActionListener(alPausar);
        }

        // Acciones dentro de la ventana de pausa
        if (pausaPanel != null) {
            pausaPanel.getBotonReanudar().addActionListener(alReanudar);
            pausaPanel.getBotonGuardarPartida().addActionListener(alGuardar);
            pausaPanel.getBotonMenuPrincipal().addActionListener(alVolverAlMenu);
        }
    }

    public void desconectar() {
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().removeActionListener(alPausar);
        }
        if (pausaPanel != null) {
            pausaPanel.getBotonReanudar().removeActionListener(alReanudar);
            pausaPanel.getBotonMenuPrincipal().removeActionListener(alVolverAlMenu);
            pausaPanel.getBotonGuardarPartida().removeActionListener(alGuardar);
        }
    }

    public void pausar() {
        controladorNivel.setPausado(true);
        if (pausaPanel != null) {
            pausaPanel.limpiarMensaje();
            pausaPanel.setBounds(0, 0, nivelPanel.getWidth(), nivelPanel.getHeight());
            pausaPanel.setVisible(true);
        }
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().setVisible(false);
        }
    }

    public void guardarPartida() {
    //1. En el hilo de eventos: se lee la partida (el modelo vive aca, no se comparte con otro hilo)
            Progreso instantanea = controladorProgreso.tomarInstantanea();
        if (instantanea == null) {
            pausaPanel.mostrarMensaje("No se pudo guardar la partida");
            return;
        }

        pausaPanel.cargando(true);

        // 2. En otro hilo: solo se escribe en la base de datos. El juego y la ventana no se traban.
        new SwingWorker<Boolean, Void>() {
           
            
        @Override
            protected Boolean doInBackground() {
            
                System.out.println("[Guardado] doInBackground en hilo: " + Thread.currentThread().getName()
            + " | hilo de eventos: " + SwingUtilities.isEventDispatchThread());
                
            return controladorProgreso.guardar(instantanea); // segundo plano
            }

        @Override
            protected void done() { // vuelve solo al hilo de eventos: aca si se puede tocar la vista
                
            System.out.println("[Guardado] done en hilo: " + Thread.currentThread().getName()
            + " | hilo de eventos: " + SwingUtilities.isEventDispatchThread());    
                
                try {
                    if (get()) {
                        pausaPanel.mostrarMensaje("Partida guardada");
                        ventanaPrincipal.getMenuPanel().setContinuarVisible(true); // ya hay algo para continuar
                    } else {
                        pausaPanel.mostrarMensaje("No se pudo guardar la partida");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    // ExecutionException trae la excepcion que haya saltado en doInBackground()
                    pausaPanel.mostrarMensaje("No se pudo guardar la partida");
                }
                pausaPanel.cargando(false);
            }
        }.execute();
    }
    

    public void reanudar() {
        controladorNivel.setPausado(false);
        if (pausaPanel != null) {
            pausaPanel.setVisible(false);
        }
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().setVisible(true);
        }
        nivelPanel.requestFocusInWindow(); // Devuelve el foco al teclado del juego
    }

    public void volverAlMenu() {
        controladorNivel.detener();
        if (pausaPanel != null) {
            pausaPanel.setVisible(false);
        }
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().setVisible(true);
        }
        ventanaPrincipal.mostrarPantallaConFundido("menu");
        ReproductorSonido.reproducirEnLoop(ControladorPrincipal.MUSICA_MENU);
    }

    public void alternarPausa() {
    if (controladorNivel.isPausado()) {
        reanudar();
    } else {
        pausar();
    }
    }

}
