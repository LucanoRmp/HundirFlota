package logica.controles;

import assets.Barco;
import assets.componentes.Casilla;
import assets.Tablero;
import codigos.CodigoCasilla;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import codigos.CodigoMensaje;

public class ControlAtaque extends ControlBase {

    private ArrayList<Barco> flotaAtaque;
    private ArrayList<String> casillas;
    private Casilla centro;
    private Barco atacante;
    private Tablero tablero;

    public ControlAtaque(Control control) {
        super(control);
        casillas = new ArrayList<>();
    }

    void seleccionarBarco(Barco barco) {
        int i = 0;
        boolean limpiar = false;

        if (!flotaAtaque.contains(barco)) {
            return;
        }

        while (i < flotaAtaque.size() && !limpiar) {
            Barco b = flotaAtaque.get(i);

            if (!b.equals(barco)) {
                b.setOpacidad(0.5f);
            } else {
                if (estaBarcoSeleccionado() && atacante.equals(b)) {
                    limpiarBarcos();
                    limpiar = true;
                    atacante = null;

                    tablero.eliminarListeners();
                } else {
                    if (atacante == null) {
                        tablero.anyadirListeners();
                    } else {
                        tablero.eliminarListeners();
                        tablero.anyadirListeners();
                    }

                    b.setOpacidad(1f);
                    atacante = b;
                }

                tablero.limpiarCasillas();
            }

            i++;
        }
    }

    private void limpiarBarcos() {
        atacante = null;

        for (Barco b : flotaAtaque) {
            b.setOpacidad(1f);
        }
        
        casillas.removeAll(casillas);
    }

    private boolean estaBarcoSeleccionado() {
        return atacante != null;
    }

    Barco getAtacante() {
        return atacante;
    }

    void setFlotaAtaque(ArrayList<Barco> flotaAtaque) {
        this.flotaAtaque = flotaAtaque;
    }

    boolean seleccionarCasilla(Casilla casilla) {
        int disparos = tablero.casillasMarcadas();
        boolean seleccionado = disparos == atacante.getDisparos();

        if (disparos > atacante.getDisparos()) {
            tablero.limpiarCasillas();
            disparos = 0;
        }

        if (!seleccionado && casilla.getEstado() != CodigoCasilla.SELECCIONADA) {
            casilla.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            casilla.setEstado(CodigoCasilla.SELECCIONADA);
            disparos++;
            seleccionado = disparos == atacante.getDisparos();
            casillas.add(casilla.toString());

        } else if (casilla.getEstado() == CodigoCasilla.SELECCIONADA) {
            casilla.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            casilla.setEstado(CodigoCasilla.NO_SELECCIONADA);
            disparos--;
            seleccionado = disparos == atacante.getDisparos();
            casillas.remove(casilla.toString());
        }

        return seleccionado;
    }

    public void setTableroEnemigo(Tablero tablero) {
        this.tablero = tablero;
    }

    void limpiarSeleccion() {
        limpiarBarcos();
        tablero.limpiarCasillas();
        tablero.eliminarListeners();
        casillas.removeAll(casillas);
    }

    void procesarAtaque(String resultado) {
        tablero.pintarDisparo(resultado);
    }

    void procesarSonar(String resultado) {
        tablero.pintarSonar(Boolean.parseBoolean(resultado), centro.toString());
    }

    String getCasillas(CodigoMensaje tipo) {
        String resultado = String.join(",", casillas);
        
        if (tipo == CodigoMensaje.ATAQUE) {
            resultado += ":" + atacante.getFuerza();
        }
        
        return resultado;
    }

    boolean casillasSeleccionadas() {
        return tablero.getCasillaAtacada() != null && tablero.getCasillaAtacada().size() >= 1;
    }

    void barcosHundidos(ArrayList<Barco> hundidos) {
        for (Barco hundido : hundidos) {
            for (Barco barco : flotaAtaque) {
                if (barco.equals(hundido)) {
                    barco.hundido(true);
                    barco.setEnabled(false);
                }
            }
        }
    }

    boolean seleccionarArea(Casilla centro) {
        this.centro = centro;
        int letra = centro.toString().charAt(0);
        int numero = Integer.parseInt(centro.toString().substring(1));
        boolean seleccion = tablero.casillasMarcadas() > 1;

        if (centro.getEstado() == CodigoCasilla.SELECCIONADA) {
            tablero.limpiarCasillas();
            seleccion = false;

        } else if (!seleccion) {
            if (tablero.casillasMarcadas() == 1) {
                tablero.limpiarCasillas();
            }

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

                    Casilla casilla = tablero.getCasilla(codCasilla);

                    if (casilla != null) {
                        if (casilla.getEstado() == CodigoCasilla.NO_SELECCIONADA) {
                            casilla.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                            casilla.setEstado(CodigoCasilla.SELECCIONADA);
                            casillas.add(casilla.toString());
                        }
                    }
                }
            }

            seleccion = true;
        }

        return seleccion;
    }
}
