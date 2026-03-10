package assets;

import assets.componentes.Casilla;
import codigos.CodigoCasilla;
import codigos.Ruta;
import logica.controles.Control;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import codigos.CodigoDisparo;
import logica.configuraciones.Assets;
import logica.configuraciones.Datos;

public final class Tablero extends JPanel {

    private final HashMap<String, Casilla> tablero;
    private final int FILAS = Datos.FILAS + 1, COLUMNAS = Datos.COLUMNAS + 1;
    private int sizeCasilla;

    public Tablero() {
        sizeCasilla = 70;

        setLayout(new GridLayout(FILAS, COLUMNAS));
        setBounds(0, 0, FILAS * sizeCasilla, COLUMNAS * sizeCasilla);

        tablero = new HashMap();

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Casilla casilla = new Casilla("", SwingConstants.CENTER);

                if (i != 0 && j != 0) {
                    String cod = (char) (i + 64) + "" + j;

                    casilla.setCodigo(cod);
                    casilla.setIcon(Assets.obtenerMar(((i - 1) * 10) + (j - 1)));
                    casilla.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    tablero.put(cod, casilla);
                } else if (i == 0 && j != 0) {
                    casilla.setText(j + "");
                } else if (i != 0 && j == 0) {
                    casilla.setText((char) (i + 64) + "");
                } else {
                    casilla.setText(" ");
                }

                add(casilla);
            }
        }
    }

    public Tablero(Control control) {
        sizeCasilla = 70;

        setLayout(new GridLayout(FILAS, COLUMNAS));
        setBounds(0, 0, FILAS * sizeCasilla, COLUMNAS * sizeCasilla);

        tablero = new HashMap();

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Casilla casilla = new Casilla("", SwingConstants.CENTER, control);

                if (i != 0 && j != 0) {
                    String cod = (char) (i + 64) + "" + j;

                    casilla.setCodigo(cod);
                    casilla.setIcon(Assets.obtenerMar((((i - 1) * 10) + (j - 1))));
                    casilla.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    tablero.put(cod, casilla);
                } else if (i == 0 && j != 0) {
                    casilla.setEstado(CodigoCasilla.MARCO);
                    casilla.setText(j + "");
                } else if (i != 0 && j == 0) {
                    casilla.setEstado(CodigoCasilla.MARCO);
                    casilla.setText((char) (i + 64) + "");
                } else {
                    casilla.setEstado(CodigoCasilla.MARCO);
                }

                add(casilla);
            }
        }
    }

    public Casilla getCasilla(String key) {
        return tablero.get(key);
    }

    public int getSizeCasilla() {
        return sizeCasilla;
    }

    public void redimension() {
        sizeCasilla = 25;

        setBounds(0, 0, FILAS * sizeCasilla, COLUMNAS * sizeCasilla);
        escalarImagenes(sizeCasilla);

        revalidate();
        repaint();
    }
    
    public void redimension(int sizeCasilla) {
        setBounds(0, 0, FILAS * sizeCasilla, COLUMNAS * sizeCasilla);
        escalarImagenes(sizeCasilla);

        revalidate();
        repaint();
    }

    private void escalarImagenes(int sizeCasilla) {
        for (Casilla c : tablero.values()) {
            c.setIcon(new ImageIcon(((ImageIcon) c.getIcon()).getImage().getScaledInstance(sizeCasilla, sizeCasilla, Image.SCALE_SMOOTH)));

            c.setPreferredSize(new Dimension(sizeCasilla, sizeCasilla));
        }
    }

    public void anyadirListeners() {
        for (Casilla casilla : tablero.values()) {
            casilla.anyadirListeners();
        }
    }

    public void eliminarListeners() {
        for (Casilla casilla : tablero.values()) {
            casilla.eliminarListeners();
        }
    }

    public void limpiarCasillas() {
        for (Casilla casilla : tablero.values()) {
            casilla.limpiarCasillas();
        }
    }

    public int casillasMarcadas() {
        int marcadas = 0;

        for (Casilla casilla : tablero.values()) {
            if (casilla.getEstado().equals(CodigoCasilla.SELECCIONADA)) {
                marcadas++;
            }
        }

        return marcadas;
    }

    public void pintarDisparo(String disparos) {
        String[] disparo = disparos.split(",");

        for (String d : disparo) {
            String[] contenido = d.split(":");
            tablero.get(contenido[0]).disparo(CodigoDisparo.valueOf(contenido[1]));
        }
    }

    public void pintarSonar(boolean signal, String centro) {
        int letra = centro.charAt(0);
        int numero = Integer.parseInt(centro.substring(1));

        for (int l = -1; l < 2; l++) {
            for (int n = -1; n < 2; n++) {
                String codCasilla = "";

                if (letra > 'A' && letra < 'J') {
                    codCasilla += (char) (letra + l) + "";
                } else if (letra == 'J' && l != 1) {
                    codCasilla += (char) (letra + l) + "";
                } else if (letra == 'A' && l != -1) {
                    codCasilla += (char) (letra + l) + "";
                }

                if (numero > 1 && numero < 10) {
                    codCasilla += (numero + n) + "";
                } else if (numero == 10 && n != 1) {
                    codCasilla += (numero + n) + "";
                } else if (numero == 1 && n != -1) {
                    codCasilla += (numero + n) + "";
                }

                Casilla casilla = tablero.get(codCasilla);

                if (casilla != null) {
                    casilla.setSignal(signal);
                }
            }

        }
    }

    public HashSet<String> getCasillaAtacada() {
        HashSet<String> codigos = new HashSet<>();
        HashSet<Casilla> casillas = obtenerCasillas();

        for (Casilla c : casillas) {
            codigos.add(c.toString());
        }

        return codigos;
    }

    private HashSet<Casilla> obtenerCasillas() {
        HashSet<Casilla> casillas = new HashSet<>();

        for (Casilla c : tablero.values()) {
            if (c.getEstado() == CodigoCasilla.SELECCIONADA) {
                casillas.add(c);
            }
        }

        return casillas;
    }
}
