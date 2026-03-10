package backend;

import salas.Sala;
import logica.configuraciones.Datos;
import backend.TCP.TCP;
import codigos.CodigoConexion;
import codigos.CodigoMensaje;
import codigos.CodigoTurno;
import logica.controles.Control;
import java.io.IOException;
import java.net.Socket;

public class Servidor extends TCP {

    private boolean listoServer, listoCliente;

    public Servidor(Control control) {
        super(control);
        listoServer = false;
        listoCliente = false;
    }

    public void encender(String codigo) {
        Thread hiloConexion = new Thread(() -> {
            try {
                sala = new Sala(new Socket(Datos.ipServer, Datos.portServer));
                sala.setKeepAlive(true);

                sala.enviarObjeto(codigo + ":true");
                
                CodigoConexion respuesta = (CodigoConexion) sala.leerObjeto();
                
                if (respuesta == CodigoConexion.PARTIDA_CREADA) {
                    respuesta = (CodigoConexion) sala.leerObjeto();
                    
                    if (respuesta == CodigoConexion.OK) {
                        setSala(sala);
                        setServidorListo();
                    }else{
                        cancelarBusqueda();
                    }
                } else {
                    sala.cerrar();
                }
                
                
            } catch (IOException ex) {
                System.out.println("Cancelaste - " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al comunicarse con el servidor - " + ex.getMessage());
            }
        });

        hiloConexion.setDaemon(true);
        hiloConexion.start();
    }

    private void setServidorListo() {
        listoServer = true;
        verificarEmpezar();
    }

    @Override
    public void leerMensaje(CodigoMensaje mensaje) {
        if (mensaje.equals(CodigoMensaje.LISTO)) {
            listoCliente = true;
            verificarEmpezar();
        }
    }

    private void verificarEmpezar() {
        if (listoServer && listoCliente) {
            try {
                sala.enviarObjeto(CodigoMensaje.EMPEZAR.toString());
                control.comenzarJuego();
            } catch (IOException ex) {
                System.out.println("Error al verificar quien empieza - " + ex.getMessage());
            }
        }
    }

    @Override
    public void turno(CodigoTurno jugador) {
        miTurno = jugador == CodigoTurno.SERVIDOR;
    }

    public void cancelarBusqueda() {
        try {
            if (sala != null) {
                sala.enviarObjeto(CodigoConexion.CANCELAR);
                sala.cerrar();
                sala = null;
            }
        } catch (IOException ex) {
            System.out.println("Error al cancelar busqueda - " + ex.getMessage());
        }
    }
}
