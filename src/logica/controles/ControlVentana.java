package logica.controles;

import codigos.CodigoVentana;
import static codigos.CodigoVentana.CREATE;
import static codigos.CodigoVentana.ESPERA;
import static codigos.CodigoVentana.GAME;
import static codigos.CodigoVentana.JOIN;
import static codigos.CodigoVentana.LOGIN;
import static codigos.CodigoVentana.MENU;
import static codigos.CodigoVentana.TUTORIAL;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import logica.configuraciones.Assets;
import ventanas.Create;
import ventanas.Game;
import ventanas.Join;
import ventanas.Login;
import ventanas.Menu;
import ventanas.Tutorial;
import ventanas.VentanaDeEspera;

public class ControlVentana extends ControlBase {

    private JFrame ventana;
    private JFrame ventanaAux;
    private JDialog espera;

    ControlVentana(Control control) {
        super(control);
    }

    void crearVentana(CodigoVentana ventana) {
        switch (ventana) {
            case MENU -> {
                cerrar();
                this.ventana = new Menu(control);
                control.setCierre(false);
            }

            case JOIN -> {
                cerrar();
                this.ventana = new Join(control);
                control.setCierre(false);
            }

            case CREATE -> {
                cerrar();
                this.ventana = new Create(control);
                control.setCierre(false);
            }

            case GAME -> {
                cerrar();
                this.ventana = new Game(control);
                control.setCierre(false);
            }

            case LOGIN -> {
                cerrar();
                this.ventana = new Login(control);
                control.setCierre(false);
            }

            case TUTORIAL -> {
                this.ventanaAux = new Tutorial(control);
            }

            case ESPERA -> {
                espera = new VentanaDeEspera(this.ventana, control);
            }
        }
    }

    void cerrar() {
        control.setCierre(true);
        
        if (ventana != null) {
            if (ventanaAux != null) {
                ventanaAux.dispose();
            }
            ventana.dispose();
        }
    }

    void cerrarVentanaEspera() {
        if (espera != null) {
            espera.dispose();
        }
    }

    CodigoVentana ventanaAbierta() {
        if (ventana == null) {
            return null;
        }

        return switch (this.ventana) {
            case Menu m -> {
                yield CodigoVentana.MENU;
            }

            case Join j -> {
                yield CodigoVentana.JOIN;
            }

            case Create c -> {
                yield CodigoVentana.CREATE;
            }

            case Game g -> {
                yield CodigoVentana.GAME;
            }

            default -> {
                yield null;
            }
        };
    }

    String getCodigo() {
        String codigo = "";

        if (ventana instanceof Join j) {
            codigo = j.getInputText();
        }

        return codigo;
    }
    
    String getUsuario(){
        String usuario = "";

        if (ventana instanceof Login l) {
            usuario = l.getUsuario();
        }

        return usuario;
    }
    
    String getPassword(){
        String password = "";

        if (ventana instanceof Login l) {
            password = l.getPassword();
        }

        return password;
    }

    void empezarPartida() {
        cerrarVentanaEspera();

        if (ventana instanceof Game game) {
            game.empezarPartida();
        }
    }

    void mostrarError(String error) {
        if (ventana instanceof Join j) {
            j.setInputText("");
        }

        JOptionPane.showMessageDialog(ventana, error, "Error", JOptionPane.WARNING_MESSAGE);
    }

    void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje);
    }

    void mostrarVictoria() {
        JOptionPane.showMessageDialog(ventana, "Enhorabuena has ganado la partida", "Victoria", JOptionPane.PLAIN_MESSAGE, Assets.VICTORIA);
    }

    void mostrarDerrota() {
        JOptionPane.showMessageDialog(ventana, "Vaya espero que no se rinda capitan", "Derrota", JOptionPane.PLAIN_MESSAGE, Assets.DERROTA);
    }

    void cambiarIconoMute(boolean muteado) {
        if (ventana instanceof Menu m) {
            m.cambiarMute(muteado);
        }
    }

}
