/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructuras;

/**
 *
 * @author necross
 */
public class Config {
    
    //Puerto de comunicacion RMI
    public final static int puerto = 4295;
    
    //Puerto de comunicacion Multicast
    public final static int puertoMul = 4295;
    
    
    //Directorio de descarga y subida de archivos
    public final static String dirDes = "D";
    
    
    public final static String dirMulticast = "224.0.0.24";

    public final static String[] ntpServers = {"ntp.cais.rnp.br",
        "chronos.csr.net","clock.danplanet.com"};
    
    
    /*Numero de servidores que van a ejecutar la clase*/
    public final static int numServidores = 1;
    
    
    
    
    
    /**Ejecuta la aplicacion asi no haya el numero minimo de servidores 
     * disponibles es decir numServidres > numero de servidores conectados*/
    public final static boolean inseguro = false;
    

}
