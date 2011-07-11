/**
 * Esta clase fue creada para poder definir metodos que tengan que ver con 
 * la conexion a la red, ejemplo. Nombre del host, direccion ip del host
 * 
 */
package Estructuras;

/**
 *
 * @author necross
 */
public class infoRed {
 
    /*
     *Retorna la direccion ip del equipo
     */
    public static String miIp(){
       String ip = "";
       try {
           java.net.InetAddress i = java.net.InetAddress.getLocalHost();
           ip= i.getHostAddress(); // IP address only
       }
       catch(Exception e){e.printStackTrace();}
       return ip;
   }
    
    
    
    /**
     * Retorna el nombre del host
     */
    public static String miHost(){
       String host = "";
       try {
           java.net.InetAddress i = java.net.InetAddress.getLocalHost();
           host= i.getHostName(); // Retorna solo el nombre del host
       }
       catch(Exception e){e.printStackTrace();}
       return host;
    }
    
    
    public static void miInfo(){
     try {
           java.net.InetAddress i = java.net.InetAddress.getLocalHost();
           System.out.println(i);                  // Nombre y direccion IP 
           System.out.println(i.getHostName());    // Nombre
           System.out.println(i.getHostAddress()); // IP direccion
       }
       catch(Exception e){e.printStackTrace();}
    }
    
    
    
}
