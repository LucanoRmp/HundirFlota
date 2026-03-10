package backend.TCP;

import codigos.CodigoMensaje;
import codigos.CodigoTurno;
import logica.controles.Control;
import java.io.IOException;
import salas.Sala;

public abstract class TCP {
    private boolean preparado;
    protected final Control control;
    protected Sala sala;
    protected boolean miTurno;
    private Thread comunicacion;

    public TCP(Control control) {
        this.control = control;
        
        preparado = false;
        miTurno = true;
    }

    protected void setSala(Sala sala) throws IOException {
        this.sala = sala;
        this.sala.setReuseAddress(true);
        
        comunicacion = new Thread(new ComunicadorTCP(sala, control));
        comunicacion.start();
    }

    public void desconectar() throws IOException, InterruptedException {
        comunicacion.join();
        sala.cerrar();
    }

    public abstract void leerMensaje(CodigoMensaje mensaje);

    public void enviarMensaje(CodigoMensaje tipo, String mensaje) {
        try {
            switch (tipo) {
                case CHAT -> {
                    sala.enviarObjeto(tipo + ":[" + control.getNick() + "] " + mensaje);
                }

                default -> {
                    sala.enviarObjeto(tipo + ":" + mensaje);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al enviar paquetes - " + ex.getMessage());
        }
    }

    public void enviarMensaje(CodigoMensaje tipo, String usuario, String mensaje) {
        try {
            switch (tipo) {
                case CHAT -> {
                    sala.enviarObjeto(tipo + ":" + usuario + mensaje);
                }

                default -> {
                    sala.enviarObjeto(tipo + ":" + mensaje);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al enviar paquetes - " + ex.getMessage());
        }
    }

    public void enviarMensaje(CodigoMensaje mensaje) {
        try {
            if (mensaje == CodigoMensaje.LISTO) {
                sala.enviarObjeto(mensaje + ":" + preparado);
            } else {
                sala.enviarObjeto(mensaje.toString());
            }
        } catch (IOException ex) {
            System.out.println("Error al enviar paquetes - " + ex.getMessage());
        }
    }

    public void estaPreparado(boolean preparado) {
        this.preparado = preparado;
    }

    public boolean esPreparado() {
        return preparado;
    }

    public abstract void turno(CodigoTurno jugador);

    public boolean esMiTurno() {
        return miTurno;
    }
}
