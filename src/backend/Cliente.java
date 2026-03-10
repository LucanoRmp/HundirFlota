package backend;

import logica.configuraciones.Datos;
import backend.TCP.TCP;
import codigos.CodigoMensaje;
import codigos.CodigoConexion;
import static codigos.CodigoConexion.ERROR;
import static codigos.CodigoConexion.NO_EXISTE;
import static codigos.CodigoConexion.OK;
import codigos.CodigoSQL;
import codigos.CodigoTurno;
import logica.controles.Control;
import java.io.IOException;
import java.net.Socket;
import modelos.Usuario;
import salas.Sala;

public class Cliente extends TCP {

    public Cliente(Control control) {
        super(control);
    }

    public void conectar(String codigo) {
        Thread hiloConexion = new Thread(() -> {
            try {
                sala = new Sala(new Socket(Datos.ipServer, Datos.portServer));
                sala.setKeepAlive(true);

                sala.enviarObjeto(codigo.toUpperCase() + ":false");

                CodigoConexion resultado = (CodigoConexion) sala.leerObjeto();

                if (resultado == null) {
                    resultado = CodigoConexion.ERROR;
                }

                switch (resultado) {
                    case OK -> {
                        setSala(sala);
                        sala.enviarObjeto(CodigoMensaje.LISTO.toString());
                    }

                    case ERROR, NO_EXISTE -> {
                        control.mostrarError(resultado);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error en conexión WAN: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("Error en el casteo de codigo conexion - " + ex.getMessage());
            }
        });

        hiloConexion.setDaemon(true);
        hiloConexion.start();
    }

    void conectar(Usuario usuario, CodigoSQL estado) {
        Thread hiloConexion = new Thread(() -> {
            try {
                Sala salaTemp = new Sala(new Socket(Datos.ipServer, Datos.portServer));
                salaTemp.setKeepAlive(true);

                salaTemp.enviarObjeto(CodigoConexion.SISTEMA.toString() + ":" + estado);
                salaTemp.enviarObjeto(usuario);
                
                CodigoConexion resultado = (CodigoConexion) salaTemp.leerObjeto();
                
                if (resultado == null) {
                    resultado = CodigoConexion.NO_EXISTE;
                }

                switch (resultado) {
                    case OK -> {
                        String[] cadUser = ((String) salaTemp.leerObjeto()).split(";");
                        
                        Usuario user = new Usuario(cadUser[0], cadUser[1]);
                        user.setVictoria(Integer.parseInt(cadUser[2]));
                        user.setDerrota(Integer.parseInt(cadUser[3]));
                        
                        control.setUsuario(user);
                        
                        control.inicio();
                    }

                    case ERROR -> {
                        if (estado == CodigoSQL.INICIAR) {
                            control.mostrarError("Contraseña no valida");
                        }else if (estado == CodigoSQL.REGISTRAR){
                            control.mostrarError("El usuario ya existe");
                        }
                    }

                    case NO_EXISTE -> {
                        control.mostrarError("No existe ninguna cuenta con ese nick");
                    }
                }
                
                salaTemp.cerrar();
            } catch (IOException ex) {
                System.out.println("Error en conexión WAN: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("Error en el casteo de codigo conexion - " + ex.getMessage());
            }
        });

        hiloConexion.setDaemon(true);
        hiloConexion.start();
    }

    @Override
    public void leerMensaje(CodigoMensaje mensaje) {
        if (mensaje.equals(CodigoMensaje.EMPEZAR)) {
            control.comenzarJuego();
        }
    }

    @Override
    public void turno(CodigoTurno jugador) {
        miTurno = jugador == CodigoTurno.CLIENTE;
    }

}
