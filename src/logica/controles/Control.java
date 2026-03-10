package logica.controles;

import assets.Barco;
import assets.Chat;
import assets.Tablero;
import assets.componentes.Casilla;
import codigos.CodigoMensaje;
import codigos.CodigoConexion;
import codigos.CodigoDisparo;
import codigos.CodigoTurno;
import codigos.CodigoVentana;
import codigos.Efecto;
import codigos.TipoBarco;
import logica.configuraciones.Estilo;
import logica.configuraciones.Mensaje;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import logica.configuraciones.Datos;
import logica.configuraciones.Resolucion;
import logica.configuraciones.Seguridad;
import logica.configuraciones.Sonido;
import logica.gestores.GestorTablero;
import modelos.Usuario;

public class Control {

    //Controles
    private ControlBoton cBoton;
    private ControlConexion cConexion;
    private ControlVentana cVentana;
    private ControlMensaje cMensaje;
    private ControlFlota cFlota;
    private ControlPartida cPartida;
    private ControlAtaque cAtaque;
    private GestorTablero gTablero;

    //Datos de la partida
    private final Sonido SONIDO;
    private Usuario usuario;
    private boolean muteado, cierre;

    //---------------------------------ARRANQUE-------------------------------------
    public Control() {
        Estilo.iniciar();
        Resolucion.iniciar();
        SONIDO = new Sonido();
        iniciarSubcontroles();
        
        muteado = false;
        cierre = false;
    }

    //-----------------------------VENTANAS-----------------------------------------
    public void registro() {
        cVentana.crearVentana(CodigoVentana.LOGIN);
    }

    public void inicio() {
        SONIDO.reproducir(Efecto.FONDO);
        cVentana.crearVentana(CodigoVentana.MENU);
    }

    private void reinicio() {
        iniciarSubcontroles();
        System.out.println("gola");
        inicio();
    }

    private void iniciarSubcontroles() {
        gTablero = new GestorTablero(this);

        cBoton = new ControlBoton(this);
        cConexion = new ControlConexion(this);
        cVentana = new ControlVentana(this);
        cMensaje = new ControlMensaje(this);
        cFlota = new ControlFlota(this, gTablero);
        cPartida = new ControlPartida(this, gTablero);
        cAtaque = new ControlAtaque(this);
    }

    void ventanaCreate() {
        cVentana.crearVentana(CodigoVentana.CREATE);
    }

    void ventanaJoin() {
        cVentana.crearVentana(CodigoVentana.JOIN);
    }

    public void ventanaTutorial() {
        cVentana.crearVentana(CodigoVentana.TUTORIAL);
    }

    public String generarCodigo() {
        return cConexion.getCodigo();
    }

    void empezarVentanaPartida() {
        cVentana.empezarPartida();
    }

    void cerrarVentanaEspera() {
        cVentana.cerrarVentanaEspera();
    }

    public void mostrarVictoria() {
        cVentana.mostrarVictoria();
        usuario.setVictoria(usuario.getVictoria()+1);
        desconexion();
    }

    public boolean ventanaAbierta() {
        return cVentana.ventanaAbierta() != null;
    }
    
    public void ventanaCerrada(){
        enviarMensaje(CodigoMensaje.DESCONEXION);
    }
    
    public void mostrarMensaje(String mensaje){
        cVentana.mostrarMensaje(mensaje);
    }

    //------------------------------MENSAJES----------------------------------------
    void enviarMensajeChat() {
        cMensaje.procesarMensaje();
    }

    void enviarMensajeChat(String mensaje) {
        cMensaje.procesarMensaje(mensaje);
    }

    void enviarMensaje(CodigoMensaje codigo, String usuario, String mensaje) {
        cConexion.enviarMensaje(codigo, usuario, mensaje);
    }

    void enviarMensaje(CodigoMensaje codigo, String mensaje) {
        cConexion.enviarMensaje(codigo, mensaje);
    }

    void enviarMensaje(CodigoMensaje codigo) {
        cConexion.enviarMensaje(codigo);
    }

    public void escribirChat(String mensaje) {
        cMensaje.escribirChat(mensaje);
    }

    //----------------------------MOVIMIENTOS DE BARCOS-----------------------------
    public void iniciarArrastre(Barco barco, int i) {
        cFlota.moverBarco(barco, i);
    }

    public void cancelarArrastre(Barco barco, int i) {
        cFlota.noMoverBarco(barco, i);
    }

    public boolean validarPosicion(Barco barco) {
        return cFlota.validarPosicion(barco);
    }

    public void limpiarBarco(Barco barco) {
        cFlota.limpiarPos(barco);
    }

    public void colocarBarco(Barco barco) {
        cFlota.colocar(barco);
    }

//---------------------------INICIO DE PARTIDA----------------------------------
    public void redimensionJuego() {
        cPartida.redimension(cFlota.getFlota(), cMensaje.getChat());
    }

//----------------------------------ATAQUES-------------------------------------
    public boolean casillaSeleccionada(Casilla casilla) {
        return cAtaque.seleccionarCasilla(casilla);
    }

    public boolean areaSeleccionada(Casilla casilla) {
        return cAtaque.seleccionarArea(casilla);
    }

    public void barcoSeleccionado(Barco barco) {
        if (cConexion.esMiTurno()) {
            cAtaque.seleccionarBarco(barco);
        }
    }

    public void procesarAtaque(String resultado) {
        procesarSonido(resultado);

        cAtaque.procesarAtaque(resultado);
        cMensaje.procesarMensaje(CodigoMensaje.ATAQUE, resultado);
        cPartida.terminoTurno();
    }

    public void recibirAtaque(String ataque) {
        String resultado = cPartida.procesarAtaque(ataque);

        procesarSonido(resultado);

        cConexion.enviarMensaje(CodigoMensaje.ATAQUE, resultado);
        if (cPartida.perdida()) {
            derrota();
        }
    }

    public void procesarSonar(String resultado) {
        procesarSonido(resultado);

        cAtaque.procesarSonar(resultado);
        cMensaje.procesarMensaje(CodigoMensaje.SONAR, resultado);
        cPartida.terminoTurno();
    }

    public void recibirSonar(String ataque) {
        String resultado = cPartida.procesarSonar(ataque);

        procesarSonido(resultado);

        cConexion.enviarMensaje(CodigoMensaje.SONAR, resultado);
    }

    void bloquearBarcosHundidos(ArrayList<Barco> hundidos) {
        cAtaque.barcosHundidos(hundidos);
    }
//------------------------------------SONIDO------------------------------------

    private void procesarSonido(String mensaje) {
        if (mensaje.contains("true") || mensaje.contains("false")) {
            Boolean sonar = Boolean.valueOf(mensaje);

            if (sonar) {
                SONIDO.reproducir(Efecto.ENCONTRADO);
            } else {
                SONIDO.reproducir(Efecto.NO_ENCONTRADO);
            }
        } else {
            String[] disparos = mensaje.split(",");

            for (String disparo : disparos) {
                String[] contenido = disparo.split(":");
                switch (CodigoDisparo.valueOf(contenido[1])) {
                    case TOCADO -> {
                        SONIDO.reproducir(Efecto.DISPARO);
                    }
                    case AGUA -> {
                        SONIDO.reproducir(Efecto.AGUA);
                    }
                    case RESISTIDO -> {
                        SONIDO.reproducir(Efecto.RESISTIDO);
                    }
                    case HUNDIDO -> {
                        SONIDO.reproducir(Efecto.HUNDIDO);
                    }
                    case DESTRUIDO -> {
                        SONIDO.reproducir(Efecto.DESTRUIDO);
                    }
                }
            }
        }
    }
    
    public void mutear(){
        if (!muteado) {
            SONIDO.detener(Efecto.FONDO);
        }else{
            SONIDO.reproducir(Efecto.FONDO);
        }
        
        muteado = !muteado;
        cVentana.cambiarIconoMute(muteado);
    }

//----------------------------------LISTENERS-----------------------------------
    void crearPartida() {
        cConexion.crearPartida();
    }

    void unirsePartida() {
        cConexion.unirsePartida();
    }

    void cancelar() {
        cConexion.cancelar();
        inicio();
    }

    boolean ventanaAbierta(CodigoVentana codigo) {
        return cVentana.ventanaAbierta().equals(codigo);
    }

    void conectarse() {
        cConexion.conectarPartida(cVentana.getCodigo());
    }

    void mandarAtaque() {
        if (cAtaque.getAtacante().getTipo() == TipoBarco.SUBMARINO) {
            CodigoMensaje tipo = cAtaque.getCasillas(CodigoMensaje.SONAR).chars().filter(c -> c == ',').count() == 0 ? CodigoMensaje.ATAQUE : CodigoMensaje.SONAR;
            cConexion.enviarMensaje(tipo, cAtaque.getCasillas(tipo));
        } else {
            cConexion.enviarMensaje(CodigoMensaje.ATAQUE, cAtaque.getCasillas(CodigoMensaje.ATAQUE));
        }

        cAtaque.limpiarSeleccion();
    }
//--------------------------------------BBDD------------------------------------

    void iniciarSesion() {
        String nick = cVentana.getUsuario();
        String password = cVentana.getPassword();

        if (!nick.isBlank() && !password.isBlank() && Character.isLetter(nick.charAt(0))) {
            cConexion.iniciarSesion(new Usuario(nick, Seguridad.encriptar(password)));
        } else if (!nick.isBlank() && !Character.isLetter(nick.charAt(0))) {
            mostrarError("El primer caracter debe ser una letra");
        } else {
            mostrarError("Debes de rellenar todos los campos");
        }
    }

    void registrarUsuario() {
        String nick = cVentana.getUsuario();
        String password = cVentana.getPassword();

        if (!nick.isBlank() && !password.isBlank() && Character.isLetter(nick.charAt(0))) {
            cConexion.registrarUsuario(new Usuario(nick, Seguridad.encriptar(password)));
        } else if (!nick.isBlank() && !Character.isLetter(nick.charAt(0))) {
            mostrarError("El primer caracter debe ser una letra");
        } else {
            mostrarError("Debes de rellenar todos los campos");
        }
    }

//------------------------------------ESTADOS-----------------------------------
    public boolean juegoEmpezado() {
        return cPartida.juegoEmpezado();
    }

    public boolean partidaEmpezada() {
        return cPartida.partidaEmpezada();
    }

    public void comenzarJuego() {
        cVentana.crearVentana(CodigoVentana.GAME);

        cPartida.juegoEmpezado(true);
    }

    public void listo(CodigoMensaje cod) {
        cConexion.leerMensaje(cod);
    }

    void listoPartida() {
        if (cFlota.barcosColocados()) {
            cVentana.crearVentana(CodigoVentana.ESPERA);
            cFlota.cambiarListenersFlota();

            cConexion.estaPreparado(true);

            enviarMensaje(CodigoMensaje.LISTO);
            empezarPartida();
        }
    }

    public void cancelarListo() {
        cVentana.cerrarVentanaEspera();
        cFlota.cambiarListenersFlota();
        cConexion.estaPreparado(false);

        enviarMensaje(CodigoMensaje.LISTO);
    }

    public void listo(boolean preparado) {
        cPartida.enemigoListo(preparado);

        if (preparado) {
            empezarPartida();
        }
    }

    private void empezarPartida() {
        cPartida.empezarPartida();
    }

    void empieza(CodigoTurno jugador) {
        cConexion.enviarMensaje(CodigoMensaje.TURNO, jugador.toString());

        recibirTurno(jugador);
    }

    void turnoDe(CodigoTurno jugador) {
        cConexion.enviarMensaje(CodigoMensaje.TURNO, jugador.toString());

        recibirTurno(jugador);
    }

    public void recibirTurno(CodigoTurno jugador) {
        cPartida.setTurno(jugador);
        cConexion.turnoJugador(jugador);
        if (cConexion.esMiTurno()) {
            enviarMensajeChat(Mensaje.TURNO + Datos.nick);
        }
    }

    private void derrota() {
        cConexion.enviarMensaje(CodigoMensaje.RESULTADO);
        cVentana.mostrarDerrota();
        usuario.setDerrota(usuario.getDerrota()+1);
        desconexion();
    }
    
    public boolean cierreControlado() {
        return cierre;
    }

    //---------------------------------ERRORES--------------------------------------
    public void desconexion() {
        try {
            Thread.sleep(3000);
            
            cConexion.anyadirPuntuacion(usuario);
                    
            cConexion.desconectar();
            cVentana.cerrar();
            
            reinicio();
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void mostrarError(CodigoConexion error) {
        switch (error) {
            case ERROR -> {
                cVentana.mostrarError("Error no has podido unirte a partida");
            }

            case NO_EXISTE -> {
                cVentana.mostrarError("Error no existe ningun servidor para jugar ahora mismo");
                inicio();
            }

            case PARTIDA_CREADA -> {
                cVentana.mostrarError("Ya eres servidor en otra ventana");
                inicio();
            }
        }
    }

    public void mostrarError(String mensajeError) {
        cVentana.mostrarError(mensajeError);
    }

    public void mensajeNoReconocido(String mensaje) {
        cVentana.mostrarError("Paquete no esperado resultado: " + mensaje);
    }

    //----------------------------SETTERS & GETTERS---------------------------------
    public void setCierre(boolean cierre){
        this.cierre = cierre;
    }
    
    public String getNick() {
        return Datos.nick;
    }
    
    public Usuario getUsuario(){
        return usuario;
    }

    public void setChat(Chat chat) {
        cMensaje.setChat(chat);
    }

    public void setTablero(Tablero tablero) {
        cPartida.setTablero(tablero);
    }

    Tablero getTablero() {
        return cPartida.getTablero();
    }

    public void setAreaJuego(JLayeredPane areaJuego) {
        cFlota.setAreaJuego(areaJuego);
    }

    public int getSizeCasilla() {
        return cPartida.getSizeCasilla();
    }

    public void setFlotaAtaque(ArrayList<Barco> flotaAtaque) {
        cAtaque.setFlotaAtaque(flotaAtaque);
    }

    public void setTableroEnemigo(Tablero tableroEnemigo) {
        cAtaque.setTableroEnemigo(tableroEnemigo);
    }

    public ActionListener getControlBoton() {
        return cBoton;
    }

    ControlAtaque getControlAtaque() {
        return cAtaque;
    }

    boolean estaPreparado() {
        return cConexion.estaPreparado();
    }

    public void setFlota(ArrayList<Barco> flota) {
        cPartida.setFlota(flota);
    }

    public ControlFlota getFlota() {
        return cFlota;
    }

    public ControlConexion getConexion() {
        return cConexion;
    }

    public boolean esMiTurno() {
        return cConexion.esMiTurno();
    }

    public String getCoordBarco(Barco barco) {
        return cFlota.getCoordBarco(barco);
    }

    public boolean isAtacante(TipoBarco tipo) {
        return cAtaque.getAtacante().getTipo() == tipo;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        Datos.nick = usuario.getNombre();
    }
}
