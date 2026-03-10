package ventanas;

import codigos.CodigoBoton;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import logica.configuraciones.Assets;
import logica.configuraciones.Input;
import logica.configuraciones.InputPassword;
import logica.controles.Control;

public class Login extends JFrame {
    
    private Input inputUsu;
    private InputPassword inputPw;

    public Login(Control control) {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setIconImage(Assets.LOGO);

        JLabel cabecera = new JLabel("Hundir la flota");
        cabecera.setFont(new Font("Arial", Font.BOLD, 50));
        cabecera.setHorizontalAlignment(SwingConstants.CENTER);
        add(cabecera, BorderLayout.NORTH);

        JPanel registro = new JPanel();
        registro.setLayout(new BoxLayout(registro, BoxLayout.Y_AXIS));

        JPanel usuario = new JPanel(new BorderLayout());
        usuario.setMaximumSize(new Dimension(400, 100));

        JLabel tituloUsu = new JLabel();
        tituloUsu.setText("Usuario:");
        tituloUsu.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel contInputUsu = new JPanel(new BorderLayout());
        inputUsu = new Input("Introduce tu usuario...");
        inputUsu.setFont(new Font("Arial", Font.PLAIN, 15));

        contInputUsu.add(inputUsu, BorderLayout.NORTH);

        usuario.add(tituloUsu, BorderLayout.NORTH);
        usuario.add(contInputUsu);

        registro.add(usuario);

        JPanel password = new JPanel(new BorderLayout());
        password.setMaximumSize(new Dimension(400, 100));

        JLabel tituloPw = new JLabel();
        tituloPw.setText("Contraseña:");
        tituloPw.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel contInputPw = new JPanel(new BorderLayout());
        inputPw = new InputPassword("Introduce tu contraseña...");
        inputPw.setFont(new Font("Arial", Font.PLAIN, 15));
        contInputPw.add(inputPw, BorderLayout.NORTH);

        password.add(tituloPw, BorderLayout.NORTH);
        password.add(contInputPw);

        registro.add(password);

        JPanel contEnviar = new JPanel();
        contEnviar.setMaximumSize(new Dimension(400, 50));

        JButton enviar = new JButton("Enviar");
        enviar.setAlignmentX(CENTER_ALIGNMENT);
        enviar.addActionListener(control.getControlBoton());
        enviar.setActionCommand(CodigoBoton.INICIAR_SESION.toString());
        
        contEnviar.add(enviar);

        registro.add(contEnviar);

        JButton crear = new JButton("Crear cuenta");
        crear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crear.setAlignmentX(CENTER_ALIGNMENT);
        crear.addActionListener(control.getControlBoton());
        crear.setActionCommand(CodigoBoton.REGISTRARSE.toString());

        registro.add(crear);

        registro.setBorder(new javax.swing.border.EmptyBorder(50, 75, 50, 75));

        add(registro);

        setVisible(true);
    }

    public String getUsuario() {
        return inputUsu.getText();
    }

    public String getPassword() {
        return inputPw.getText();
    }

}
