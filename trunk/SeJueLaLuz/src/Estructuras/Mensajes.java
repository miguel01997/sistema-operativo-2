/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructuras;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author natalya
 */
public class Mensajes implements Runnable{

    private String msj = "";

    private String respuesta = "";

    Multicast multi = new Multicast();


    public Mensajes(String mensaje) {
        msj = mensaje;
    }


    public void run() {
        if(msj.equals("activo?")){
            respuesta = "estoy " + infoRed.miIp() + " "+ infoRed.miHost();
            System.out.println("Esta es la respuesta "+ respuesta);
            multi.enviarMensaje(respuesta);
         }else if(msj.startsWith("listo")){
           
         }        
    }
}
