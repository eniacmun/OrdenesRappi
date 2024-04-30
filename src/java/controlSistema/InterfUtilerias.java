package controlSistema;

import javax.swing.*;
import general.*;
import java.awt.*;
import utilerias.*;

public class InterfUtilerias {

    /**
     * Method mensajeError
     */
    public static void mensajeError(JFrame framePrincipal, String cadena) {
        JOptionPane.showMessageDialog(framePrincipal, cadena, "Cerrar Sistema", JOptionPane.ERROR_MESSAGE);
    }

    //Other
    public static void mensajeTermina(JFrame framePrincipal) {
        JOptionPane.showMessageDialog(framePrincipal, "Cerrar Sistema", "Cerrar Sistema", JOptionPane.ERROR_MESSAGE);
    }

    public static void mensajeCorte(JFrame framePrincipal, String cadena) {
        JOptionPane.showMessageDialog(framePrincipal, cadena, "Cerrar Sistema", JOptionPane.ERROR_MESSAGE);
    }

    public static void mensajeInformativo(JFrame framePrincipal, String cadena) {
        JOptionPane.showMessageDialog(framePrincipal, cadena, "Error de Captura", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method mensajePreventivo
     */
    public static void mensajePreventivo() {
    }

    public static void centrarFrame(JDialog f) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = f.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        f.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

}
