/**
 * Esta clase fue creada para poder definir metodos que tengan que ver con 
 * la conexion a la red, ejemplo. Nombre del host, direccion ip del host
 * 
 */
package Estructuras;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

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
       /*try {
           java.net.InetAddress i = java.net.InetAddress.getLocalHost();
           ip= i.getHostAddress(); // IP address only
       }
       catch(Exception e){e.printStackTrace();}*/

        Enumeration<NetworkInterface> nets;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)){
              ip = displayInterfaceInformation(netint);
              if(ip!=null){
                 return ip;
              }
            }
            
            
            
        } catch (SocketException ex) {
            Logger.getLogger(infoRed.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return ip;
   }
    
    
    
    
    static  String displayInterfaceInformation(NetworkInterface netint) throws SocketException {
       // System.out.printf("Display name: %s\n", netint.getDisplayName());
        //System.out.printf("Name: %s\n", netint.getName());
        String nombreInter = netint.getName(); //nombre de la interfaz
        if (nombreInter ==null){
           return null;
        }
        if(nombreInter.charAt(0)=='e' || nombreInter.charAt(0)=='w'){
        
        }
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        int i = 0;
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            //if(i==1){
               String aux = inetAddress.getHostAddress();
               
               if(aux.length() < 15 && !aux.substring(0,3).equals("127")){
                   System.out.println(aux);
                   return aux;
               }
               //if(aux.substring(0,2))
            //}
            //System.out.printf("InetAddress: %s\n", inetAddress);
            i++;
        }
        return null;
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
