package logica.controles;

import codigos.CodigoBoton;
import codigos.CodigoVentana;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class ControlBoton extends ControlBase implements ActionListener {

    ControlBoton(Control control) {
        super(control);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = (Component) e.getSource();
        c.setEnabled(false);

        new Timer(750, arg -> c.setEnabled(true)).start();

        switch (CodigoBoton.valueOf(e.getActionCommand())) {
            case INICIAR_SESION ->{
                control.iniciarSesion();
            }
            
            case REGISTRARSE ->{
                control.registrarUsuario();
            }
            
            case CREAR -> {
                control.crearPartida();
            }

            case UNIRSE -> {
                control.unirsePartida();
            }

            case CANCELAR -> {
                if (control.ventanaAbierta(CodigoVentana.GAME)) {
                    control.cerrarVentanaEspera();
                } else {
                    control.cancelar();
                }
            }

            case CONEXION -> {
                control.conectarse();
            }

            case MENSAJE -> {
                control.enviarMensajeChat();
            }

            case LISTO -> {
                if (!control.partidaEmpezada()) {
                    control.listoPartida();
                }
            }

            case TUTORIAL -> {
                control.ventanaTutorial();
            }

            case ATACAR -> {
                if (control.getConexion().esMiTurno()) {
                    if (control.getControlAtaque().casillasSeleccionadas()) {
                        control.mandarAtaque();
                    } else {
                        control.mostrarError("Debes seleccionar las casillas");
                    }

                } else {
                    control.mostrarError("No es tu turno aun");
                }
            }
            
            case BOT ->{
                control.mostrarMensaje("El bot aun no ha sido implementado");
            }
            
            case MUTE ->{
                control.mutear();
            }
        }
    }
}
