package logica.controles;

import assets.Barco;
import assets.Chat;
import assets.Tablero;
import codigos.CodigoMensaje;
import codigos.CodigoTurno;
import java.awt.Component;
import java.util.ArrayList;
import codigos.CodigoDisparo;
import logica.gestores.GestorTablero;

public class ControlPartida extends ControlBase {

    private Tablero tablero;
    private volatile boolean juego, enemigo, partida;
    private CodigoTurno turno;

    public ControlPartida(Control control, GestorTablero gestor) {
        super(control, gestor);

        juego = false;
        enemigo = false;
        partida = false;
    }

    void redimension(ArrayList<Barco> flota, Chat chat) {
        tablero.redimension();

        for (Component c : flota) {
            if (c instanceof Barco b) {
                b.redimension();
            }
        }

        chat.redimension();
    }

    public void empezarPartida() {
        if (control.estaPreparado() && enemigo && control.getConexion().esServidor()) {
            empieza();
            control.enviarMensaje(CodigoMensaje.EMPEZAR);
            control.empezarVentanaPartida();
            partida = true;

        } else if (control.estaPreparado() && enemigo) {
            control.empezarVentanaPartida();
            partida = true;
        }
    }

    boolean juegoEmpezado() {
        return juego;
    }

    boolean partidaEmpezada() {
        return partida;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getSizeCasilla() {
        return tablero.getSizeCasilla();
    }
    
    public void setFlota(ArrayList<Barco> flota){
    }

    public void juegoEmpezado(boolean juego) {
        this.juego = juego;
    }

    public void enemigoListo(boolean enemigo) {
        this.enemigo = enemigo;
    }

    private void empieza() {
        if (control.getConexion().esServidor()) {
            turno = (int) (Math.random() * 2) % 2 != 0 ? CodigoTurno.SERVIDOR : CodigoTurno.CLIENTE;

            control.empieza(turno);
        }
    }

    void terminoTurno() {
        if (control.getConexion().esMiTurno()) {
            turno = turno == CodigoTurno.SERVIDOR ? CodigoTurno.CLIENTE : CodigoTurno.SERVIDOR;
            control.turnoDe(turno);
        }
    }

    void setTurno(CodigoTurno turno) {
        this.turno = turno;
    }

    String procesarAtaque(String ataque) {
        String resultado = gestorTablero.disparo(ataque);
        ArrayList<Barco> hundidos = gestorTablero.getHundidos();
        
        if (resultado.contains(CodigoDisparo.AGUA.toString())) {
            tablero.pintarDisparo(resultado);
        }
        
        if (hundidos != null) {
            control.bloquearBarcosHundidos(hundidos);
        }

        return resultado;
    }
    
    String procesarSonar(String ataque) {
        return gestorTablero.sonar(ataque);
    }

    boolean perdida() {
        return gestorTablero.estaDerrotado();
    }
}
