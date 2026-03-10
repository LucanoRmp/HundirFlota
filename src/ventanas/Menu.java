package ventanas;

import assets.Tablero;
import codigos.CodigoBoton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import logica.controles.Control;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logica.configuraciones.Assets;
import logica.configuraciones.Resolucion;
import modelos.Usuario;

public class Menu extends JFrame {

    private final Control control;
    private final JButton mute;

    public Menu(Control control) {
        super("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Resolucion.width(1000), Resolucion.heigth(600));
        setLocationRelativeTo(null);
        setIconImage(Assets.LOGO);

        this.control = control;

//------------------------------------IZQUIERDA---------------------------------
        JPanel izq = new JPanel(new BorderLayout());

        JLabel tituloIzq = new JLabel("Nivel de bot");
        tituloIzq.setFont(new Font("Arial", Font.BOLD, Resolucion.fuente(40)));
        tituloIzq.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel niveles = new JPanel();
        niveles.setLayout(new BoxLayout(niveles, BoxLayout.Y_AXIS));
        niveles.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(37))));

        JButton facil = new JButton("Facil");
        configurarBtn(facil);
        facil.setBackground(new Color(34, 153, 12));
        niveles.add(facil);
        niveles.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(25))));

        JButton medio = new JButton("Medio");
        configurarBtn(medio);
        medio.setBackground(new Color(15, 60, 190));
        niveles.add(medio);
        niveles.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(25))));

        JButton dificil = new JButton("Dificil");
        configurarBtn(dificil);
        dificil.setBackground(new Color(180, 20, 20));
        niveles.add(dificil);
        niveles.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(25))));

        JButton extremo = new JButton("Extremo");
        configurarBtn(extremo);
        extremo.setBackground(new Color(110, 30, 130));
        niveles.add(extremo);
        niveles.add(Box.createVerticalGlue());

        JPanel contTutorial = new JPanel();

        JButton tutorial = new JButton("Guia");
        tutorial.setToolTipText("Ver guia");
        tutorial.addActionListener(control.getControlBoton());
        tutorial.setActionCommand(CodigoBoton.TUTORIAL.toString());

        contTutorial.add(tutorial);

        izq.add(tituloIzq, BorderLayout.NORTH);
        izq.add(niveles);
        izq.add(contTutorial, BorderLayout.SOUTH);

        add(izq, BorderLayout.WEST);

//------------------------------------CENTRO------------------------------------
        JPanel centro = new JPanel();

        Tablero t = new Tablero();
        t.redimension(40);
        centro.add(t);

        add(centro);

//---------------------------------DERECHA--------------------------------------
        JPanel der = new JPanel(new BorderLayout());

        JLabel tituloDer = new JLabel("Multijugador");
        tituloDer.setFont(new Font("Arial", Font.BOLD, Resolucion.fuente(40)));
        tituloDer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel partidas = new JPanel();
        partidas.setLayout(new BoxLayout(partidas, BoxLayout.Y_AXIS));
        partidas.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(37))));
        
        Usuario usuario = control.getUsuario();
        
        JLabel nombre = new JLabel(usuario.getNombre());
        nombre.setFont(new Font("Arial", Font.BOLD, Resolucion.fuente(20)));
        nombre.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel victorias = new JLabel("Nº victorias: "+usuario.getVictoria());
        victorias.setFont(new Font("Arial", Font.PLAIN, Resolucion.fuente(15)));
        victorias.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel derrotas = new JLabel("Nº derrotas: "+usuario.getDerrota());
        derrotas.setFont(new Font("Arial", Font.PLAIN, Resolucion.fuente(15)));
        derrotas.setAlignmentX(CENTER_ALIGNMENT);

        JButton create = new JButton("Crear sala");
        create.setMaximumSize(new Dimension(Resolucion.width(150), Resolucion.heigth(75)));
        create.setAlignmentX(CENTER_ALIGNMENT);
        create.setFocusable(false);
        create.addActionListener(control.getControlBoton());
        create.setActionCommand(CodigoBoton.CREAR.toString());

        JButton join = new JButton("Unirte a una sala");
        join.setMaximumSize(new Dimension(Resolucion.width(150), Resolucion.heigth(75)));
        join.setAlignmentX(CENTER_ALIGNMENT);
        join.setFocusable(false);
        join.addActionListener(control.getControlBoton());
        join.setActionCommand(CodigoBoton.UNIRSE.toString());
        
        partidas.add(nombre);
        partidas.add(victorias);
        partidas.add(derrotas);
        partidas.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(25))));
        partidas.add(create);
        partidas.add(Box.createRigidArea(new Dimension(Resolucion.width(200), Resolucion.heigth(25))));
        partidas.add(join);

        JPanel contMute = new JPanel();

        mute = new JButton();
        mute.setPreferredSize(new Dimension(Resolucion.width(25), Resolucion.heigth(25)));
        mute.setIcon(new ImageIcon(Assets.MUTE));
        mute.addActionListener(control.getControlBoton());
        mute.setActionCommand(CodigoBoton.MUTE.toString());

        contMute.add(mute);

        der.add(tituloDer, BorderLayout.NORTH);
        der.add(partidas);
        der.add(contMute, BorderLayout.SOUTH);

        add(der, BorderLayout.EAST);

        setVisible(true);
    }

    private void configurarBtn(JButton boton) {
        boton.setMaximumSize(new Dimension(Resolucion.width(150), boton.getPreferredSize().height));
        boton.setMinimumSize(new Dimension(Resolucion.width(150), boton.getPreferredSize().height));
        boton.setFont(new Font("Arial", Font.BOLD, Resolucion.fuente(17)));
        boton.setAlignmentX(CENTER_ALIGNMENT);
        boton.setForeground(Color.WHITE);
        boton.setFocusable(false);

        boton.addActionListener(control.getControlBoton());
        boton.setActionCommand(CodigoBoton.BOT.toString());
    }

    public void cambiarMute(boolean muteado) {
        if (muteado) {
            mute.setIcon(new ImageIcon(Assets.SONIDO));
        }else{
            mute.setIcon(new ImageIcon(Assets.MUTE));
        }
    }

}
