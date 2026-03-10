package logica.controles;

import logica.gestores.GestorTablero;

public abstract class ControlBase {
    protected Control control;
    protected GestorTablero gestorTablero;
    
    ControlBase(Control control){
        this.control = control;
    }
    
    ControlBase(Control control, GestorTablero gestor){
        this.control = control;
        gestorTablero = gestor;
    }
}
