//Prueba Multicast
import Estructuras.Multicast;
import Estructuras.infoRed;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author necross
 */
public class pMulticast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Multicast m = new Multicast();
        //Para lanzar un hilo que escucha
        /*Thread t= new Thread(m);
        t.start();
        
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(pMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        m.enviarMensaje("llamame al"+infoRed.miHost());
        
        
    }

}
