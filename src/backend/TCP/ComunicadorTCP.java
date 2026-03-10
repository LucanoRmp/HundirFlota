package backend.TCP;

import salas.Sala;
import codigos.CodigoMensaje;
import logica.configuraciones.Mensaje;
import logica.controles.Control;
import java.io.IOException;
import codigos.CodigoTurno;

public class ComunicadorTCP implements Runnable {

    private final Sala sala;
    private final Control control;

    public ComunicadorTCP(Sala sala, Control control) {
        this.control = control;
        this.sala = sala;
    }

    @Override
    public void run() {
        String mensaje;
        try {
            mensaje = (String) sala.leerObjeto();
            while (mensaje != null) {
                procesarMensaje(mensaje);
                mensaje = (String) sala.leerObjeto();
            }
        } catch (IOException ex) {
            System.out.println("Comunicacion cerrada: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("Error al comunicarse con el servidor - " + ex.getMessage());
        } finally {
            try {
                if (sala != null && !sala.estaCerrada()) {
                    sala.cerrar();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void procesarMensaje(String mensaje) {
        CodigoMensaje cod;
        String valor = null;

        if (mensaje.contains(":")) {
            String[] partes = mensaje.split(":", 2);
            cod = CodigoMensaje.valueOf(partes[0]);
            valor = partes[1];
        } else {
            cod = CodigoMensaje.valueOf(mensaje);
        }

        switch (cod) {
            case SONAR -> {
                if (!control.esMiTurno()) {
                    control.recibirSonar(valor);
                } else {
                    control.procesarSonar(valor);
                }
            }

            case ATAQUE -> {
                if (!control.esMiTurno()) {
                    control.recibirAtaque(valor);
                } else {
                    control.procesarAtaque(valor);
                }
            }

            case CHAT -> {
                control.escribirChat(valor);
            }

            case TURNO -> {
                control.recibirTurno(CodigoTurno.valueOf(valor));
            }

            case LISTO, EMPEZAR -> {
                if (!control.juegoEmpezado()) {
                    control.listo(cod);
                } else {
                    control.listo(Boolean.parseBoolean(valor));
                }
            }

            case DESCONEXION -> {
                control.escribirChat(Mensaje.DESCONEXION);
                control.desconexion();
            }

            case RESULTADO -> {
                control.mostrarVictoria();
            }

            default -> {
                control.mensajeNoReconocido(cod + valor);
            }
        }
    }
}
