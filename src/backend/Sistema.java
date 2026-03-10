package backend;

import codigos.CodigoSQL;
import logica.controles.Control;
import modelos.Usuario;

public class Sistema {
    private Cliente mensajero;
    
    public Sistema(Control control){
        mensajero = new Cliente(control);
    }
    
    private void enviarUsuario(Usuario usuario, CodigoSQL estado){
        mensajero.conectar(usuario, estado);
    }
    
    public void iniciarSesion(Usuario usuario){
        enviarUsuario(usuario, CodigoSQL.INICIAR);
    }
    
    public void registrarUsuario(Usuario usuario){
        enviarUsuario(usuario, CodigoSQL.REGISTRAR);
    }

    public void registrarPartida(Usuario usuario) {
        enviarUsuario(usuario, CodigoSQL.ACTUALIZAR);
    }
}
