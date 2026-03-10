package logica.controles;

import assets.Chat;
import codigos.CodigoMensaje;
import logica.configuraciones.Estilo;
import logica.configuraciones.Mensaje;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import codigos.CodigoDisparo;

public class ControlMensaje extends ControlBase {

    private Chat chat;

    public ControlMensaje(Control control) {
        super(control);
    }

    void procesarMensaje() {
        enviarMensajeChat(CodigoMensaje.CHAT, obtenerMensaje());
    }

    void procesarMensaje(String mensaje) {
        enviarMensajeChat(Mensaje.SISTEMA, mensaje);
    }

    void procesarMensaje(CodigoMensaje codigo, String mensaje) {
        if (codigo.equals(CodigoMensaje.ATAQUE)) {
            for (String ataque : mensaje.split(",")) {
                enviarMensajeChat(Mensaje.SISTEMA, leerMensaje(ataque));
            }
        }else{
            if (Boolean.parseBoolean(mensaje)) {
                enviarMensajeChat(Mensaje.SISTEMA, "El sonar a detectado algo");
            }else{
                enviarMensajeChat(Mensaje.SISTEMA, "El sonar no ha detectado nada");
            }
        }
    }

    private void enviarMensajeChat(CodigoMensaje codigo, String mensaje) {
        if (mensaje != null && !mensaje.isBlank()) {
            control.enviarMensaje(codigo, mensaje);

            escribirChat("[" + control.getNick() + "] " + mensaje, Estilo.ALIADO);
            chat.setInputText("");
        }
    }

    private void enviarMensajeChat(String usuario, String mensaje) {
        if (mensaje != null && !mensaje.isBlank()) {
            control.enviarMensaje(CodigoMensaje.CHAT, usuario, mensaje);

            escribirChat(usuario + mensaje, Estilo.SISTEMA);
        }
    }

    private void escribirChat(String mensaje, SimpleAttributeSet style) {
        try {
            if (chat != null) {
                chat.escribirChat(mensaje, style);
            }
        } catch (BadLocationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void escribirChat(String mensaje) {
        SimpleAttributeSet style;

        if (mensaje.contains("[Sistema]")) {
            style = Estilo.SISTEMA;
        } else {
            style = Estilo.ENEMIGO;
        }

        escribirChat(mensaje, style);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

    private String obtenerMensaje() {
        return chat.getInputText();
    }

    private String leerMensaje(String ataque) {
        String mensaje = "La casilla ";

        String[] contenido = ataque.split(":");
        switch (CodigoDisparo.valueOf(contenido[1])) {
            case AGUA -> {
                mensaje += contenido[0] + " fue atacada y no encontro nada.";
            }

            case TOCADO -> {
                mensaje += contenido[0] + " fue atacada y encontro un barco.";
            }

            case RESISTIDO -> {
                mensaje += contenido[0] + " fue atacada pero resistio al impacto.";
            }

            case HUNDIDO -> {
                mensaje += contenido[0] + " fue atacada y hundio el barco.";
            }
            
            case DESTRUIDO ->{
                mensaje += contenido[0] + " fue destruido este ataque";
            }
        }

        return mensaje;
    }
}
