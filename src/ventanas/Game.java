package ventanas;

import assets.Barco;
import assets.Chat;
import assets.Tablero;
import codigos.CodigoBoton;
import codigos.TipoBarco;
import logica.controles.Control;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import logica.configuraciones.Assets;
import logica.configuraciones.Resolucion;

public class Game extends JFrame {

    private final JPanel panelBoton, panelFlota, panelFlotaAtaque;
    private Tablero tablero, tableroEnemigo;
    private final Chat chat;
    private final ArrayList<Barco> flota, flotaAtaque;
    private final Control control;

    public Game(Control control) {
        super("Hundir la flota");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setIconImage(Assets.LOGO);

        this.control = control;
        panelFlotaAtaque = new JPanel();
        panelFlotaAtaque.setLayout(new BoxLayout(panelFlotaAtaque, BoxLayout.Y_AXIS));

        GridBagConstraints gbc = new GridBagConstraints();

        JLayeredPane areaJuego = new JLayeredPane();
        areaJuego.setLayout(null);
        areaJuego.setPreferredSize(new Dimension(Resolucion.width(1000), Resolucion.heigth(800)));
        this.control.setAreaJuego(areaJuego);

        //---------------------------Tablero------------------------------------
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 0.7;
        gbc.fill = GridBagConstraints.BOTH;

        tablero = new Tablero();
        this.control.setTablero(tablero);

        areaJuego.add(tablero, JLayeredPane.DEFAULT_LAYER);

        //----------------------------Flota-------------------------------------
        panelFlota = new JPanel();
        panelFlota.setLayout(new BoxLayout(panelFlota, BoxLayout.Y_AXIS));
        panelFlota.setOpaque(false);
        panelFlota.setBounds(Resolucion.width(800), Resolucion.heigth(70), Resolucion.width(350), Resolucion.heigth(800));

        flota = new ArrayList();
        flotaAtaque = new ArrayList();

        Barco porta = new Barco(TipoBarco.PORTAAVIONES, this.control);
        Barco acora = new Barco(TipoBarco.ACORAZADO, this.control);
        Barco subma = new Barco(TipoBarco.SUBMARINO, this.control);
        Barco cruce = new Barco(TipoBarco.CRUCERO, this.control);
        Barco destruct = new Barco(TipoBarco.DESTRUCTOR, this.control);

        flota.add(porta);
        flota.add(acora);
        flota.add(subma);
        flota.add(cruce);
        flota.add(destruct);

        control.setFlota(flota);

        for (Barco barco : flota) {
            panelFlota.add(barco);
        }

        areaJuego.add(panelFlota, JLayeredPane.DRAG_LAYER);
        add(areaJuego, gbc);

        //----------------------------Chat--------------------------------------
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 50);
        gbc.anchor = GridBagConstraints.EAST;

        chat = new Chat(this.control);

        add(chat, gbc);
        this.control.setChat(chat);

        //--------------------------Boton Listo---------------------------------
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 25, 0);

        panelBoton = new JPanel();

        JButton listo = new JButton("Listo");
        listo.setPreferredSize(new Dimension(Resolucion.width(150), Resolucion.heigth(75)));

        panelBoton.add(listo);
        add(panelBoton, gbc);

        listo.addActionListener(this.control.getControlBoton());
        listo.setActionCommand(CodigoBoton.LISTO.toString());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (!control.cierreControlado()) {
                    control.ventanaCerrada();
                }
            }
        });

        setVisible(true);
    }

    public void empezarPartida() {
        remove(panelBoton);

        tableroEnemigo = new Tablero(control);

        control.setTableroEnemigo(tableroEnemigo);

        nuevaDistribucion();
        control.ventanaTutorial();

        revalidate();
        repaint();
    }

    private void nuevaDistribucion() {
        setLayout(new BorderLayout(0, 0));

        //1. ZONA IZQUIERDA: DATOS USUARIO
        JPanel panelIzq = new JPanel(new BorderLayout(0, 0));
        panelIzq.add(new JLabel("TU FLOTA", SwingConstants.CENTER), BorderLayout.NORTH);

        control.redimensionJuego();

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        JPanel panelTablero = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTablero.add(tablero);

        panelContenido.add(panelTablero);
        panelContenido.add(Box.createRigidArea(new Dimension(350, 25)));

        Barco porta = new Barco(TipoBarco.PORTAAVIONES, this.control);
        Barco acora = new Barco(TipoBarco.ACORAZADO, this.control);
        Barco subma = new Barco(TipoBarco.SUBMARINO, this.control);
        Barco cruce = new Barco(TipoBarco.CRUCERO, this.control);
        Barco destruct = new Barco(TipoBarco.DESTRUCTOR, this.control);

        flotaAtaque.add(porta);
        flotaAtaque.add(acora);
        flotaAtaque.add(subma);
        flotaAtaque.add(cruce);
        flotaAtaque.add(destruct);

        for (Barco barco : flotaAtaque) {
            panelFlotaAtaque.add(barco);
        }

        control.setFlotaAtaque(flotaAtaque);

        panelContenido.add(panelFlotaAtaque);

        panelIzq.add(panelContenido, BorderLayout.CENTER);
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));

        JButton btnTutorial = new JButton("Ayuda");
        btnTutorial.setToolTipText("Ver Tutorial");
        btnTutorial.addActionListener(this.control.getControlBoton());
        btnTutorial.setActionCommand(CodigoBoton.TUTORIAL.toString());

        panelBtn.add(btnTutorial);
        panelIzq.add(panelBtn, BorderLayout.SOUTH);

        // 2. ZONA CENTRAL: Tablero de Ataque
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(new JLabel("TABLERO DE ATAQUE", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel panelContEnemigo = new JPanel();

        panelContEnemigo.add(tableroEnemigo);
        panelCentral.add(panelContEnemigo, BorderLayout.CENTER);

        JPanel contAtaque = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        JButton ataque = new JButton("Atacar Casillas");
        ataque.addActionListener(control.getControlBoton());
        ataque.setActionCommand(CodigoBoton.ATACAR.toString());
        ataque.setPreferredSize(new Dimension(Resolucion.width(150), Resolucion.heigth(75)));

        contAtaque.add(ataque);
        panelCentral.add(contAtaque, BorderLayout.SOUTH);

        // 3. ZONA DERECHA: Chat
        JPanel panelDer = new JPanel(new BorderLayout());
        panelDer.add(new JLabel("CHAT", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel panelChat = new JPanel();

        panelChat.add(chat);
        panelDer.add(panelChat, BorderLayout.CENTER);

        // Añadir todo al JFrame principal
        add(panelIzq, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
        add(panelDer, BorderLayout.EAST);
    }

}
