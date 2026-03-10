package logica.controles;

import backend.Cliente;
import backend.Servidor;
import backend.Sistema;
import codigos.CodigoMensaje;
import codigos.CodigoTurno;
import java.io.IOException;
import modelos.Usuario;

public class ControlConexion extends ControlBase {

    private Servidor servidor;
    private Cliente cliente;
    private Sistema sistema;
    private String codigo;

    ControlConexion(Control control) {
        super(control);
        sistema = new Sistema(control);
    }

    //--------------------------ARRANQUE DE USUARIOS--------------------------------
    private void iniciarServidor() {
        servidor = new Servidor(control);
        generarCodigo();
    }

    private void iniciarCliente() {
        cliente = new Cliente(control);
    }
    
//--------------------------------SISTEMA---------------------------------------
    void iniciarSesion(Usuario usuario){
        sistema.iniciarSesion(usuario);
    }
    
    void registrarUsuario(Usuario usuario){
        sistema.registrarUsuario(usuario);
    }

//-------------------------------SERVIDOR---------------------------------------
    void crearPartida() {
        iniciarServidor();

        control.ventanaCreate();

        new Thread(() -> {
            servidor.encender(codigo);
        }).start();
    }

    private void generarCodigo() {
        codigo = "";

        for (int i = 0; i < 6; i++) {
            int n = ((int) ((Math.random() * 35)));

            if (n >= 10) {
                n += 55;
                codigo += (char) n;
            } else {
                codigo += n + "";
            }
        }
    }

    public boolean esServidor() {
        return servidor != null;
    }

//-------------------------------CLIENTE----------------------------------------
    void unirsePartida() {
        control.ventanaJoin();

        iniciarCliente();
    }

    void conectarPartida(String codigo) {
        cliente.conectar(codigo);
    }

    boolean esCliente() {
        return cliente != null;
    }

//--------------------------MENSAJES--------------------------------------------
    void enviarMensaje(CodigoMensaje codigo) {
        if (esCliente()) {
            cliente.enviarMensaje(codigo);
        } else {
            servidor.enviarMensaje(codigo);
        }
    }

    void enviarMensaje(CodigoMensaje codigo, String mensaje) {
        if (esCliente()) {
            cliente.enviarMensaje(codigo, mensaje);
        } else {
            servidor.enviarMensaje(codigo, mensaje);
        }
    }

    void enviarMensaje(CodigoMensaje codigo, String usuario, String mensaje) {
        if (esCliente()) {
            cliente.enviarMensaje(codigo, usuario, mensaje);
        } else {
            servidor.enviarMensaje(codigo, usuario, mensaje);
        }
    }

    void leerMensaje(CodigoMensaje cod) {
        if (esCliente()) {
            cliente.leerMensaje(cod);
        } else {
            servidor.leerMensaje(cod);
        }
    }

    void estaPreparado(boolean preparado) {
        if (esCliente()) {
            cliente.estaPreparado(preparado);
        } else {
            servidor.estaPreparado(preparado);
        }
    }

    boolean estaPreparado() {
        boolean preparado;

        if (esCliente()) {
            preparado = cliente.esPreparado();
        } else {
            preparado = servidor.esPreparado();
        }

        return preparado;
    }

    void desconectar() throws IOException, InterruptedException {
        if (esCliente() && cliente != null) {
            cliente.desconectar();
        } else if (servidor != null){
            servidor.desconectar();
        }
    }

    public String getCodigo() {
        return codigo;
    }

    void turnoJugador(CodigoTurno jugador) {
        if (esCliente()) {
            cliente.turno(jugador);
        } else {
            servidor.turno(jugador);
        }
    }

    boolean esMiTurno() {
        boolean miTurno;

        if (esCliente()) {
            miTurno = cliente.esMiTurno();
        } else {
            miTurno = servidor.esMiTurno();
        }

        return miTurno;
    }

    void cancelar() {
        if (servidor != null) {
            servidor.cancelarBusqueda();
        }
    }

    void anyadirPuntuacion(Usuario usuario) {
        sistema.registrarPartida(usuario);
    }

}
