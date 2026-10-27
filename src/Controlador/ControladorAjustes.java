package Controlador;

import Modelo.Configuracion;
import Modelo.ConfiguracionDAO;
import Vista.ReproductorSonido;
import Vista.AjustesPanel;
import Vista.JuegoFrame;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class ControladorAjustes {
    
    private final AjustesPanel vista;
    private final JuegoFrame ventanaPrincipal;
    private final ConfiguracionDAO configuracionDAO;

    public ControladorAjustes(AjustesPanel vista, JuegoFrame ventanaPrincipal, ConfiguracionDAO configuracionDAO) {
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;
        this.configuracionDAO = configuracionDAO;

        cargarConfiguracionGuardada();
        configurarListeners();
    }

        private void cargarConfiguracionGuardada() {
        Configuracion config = configuracionDAO.cargar();
        ReproductorSonido.setVolumenMusica(config.getVolumenMusica());
        ReproductorSonido.setVolumenEfectos(config.getVolumenEfectos());

        vista.setVolumenMusica((int) (config.getVolumenMusica() * 100));
        vista.setVolumenEfectos((int) (config.getVolumenEfectos() * 100));
    }

    private void configurarListeners() {
        // Cambiar el slider aplica el volumen al momento
        vista.getSliderMusica().addChangeListener(e ->
            ReproductorSonido.setVolumenMusica(vista.getSliderMusica().getValue() / 100f)
        );
        vista.getSliderEfectos().addChangeListener(e ->
            ReproductorSonido.setVolumenEfectos(vista.getSliderEfectos().getValue() / 100f)
        );

        // Guardar en la Base de datos recien al salir
        vista.getBotonVolver().addActionListener(e -> guardarYVolver());
    }
    private void guardarYVolver() {
        // 1. En el hilo de eventos: se leen los sliders y se arma la foto inmutable
        float musica = vista.getSliderMusica().getValue() / 100f;
        float efectos = vista.getSliderEfectos().getValue() / 100f;
        Configuracion configuracion = new Configuracion(musica, efectos);
 
        vista.getBotonVolver().setEnabled(false); // evita clicks repetidos mientras guarda
 
        // 2. En otro hilo: solo se escribe en la base de datos
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                configuracionDAO.guardar(configuracion);
                return null;
            }
 
            // 3. Vuelve al hilo de eventos: aca si se puede tocar la vista
            @Override
            protected void done() {
                try {
                    get(); // si doInBackground fallo, la excepcion sale aca
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("No se pudo guardar la configuracion: " + ex.getMessage());
                }
                vista.getBotonVolver().setEnabled(true);
                ventanaPrincipal.mostrarPantalla("menu");
            }
        }.execute();
    }
}