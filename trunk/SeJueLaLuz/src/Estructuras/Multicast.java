/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructuras;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author necross
 */
public class Multicast implements Runnable {
    
    private String ipMulti = Config.dirMulticast;
    
    private MulticastSocket socket;
    
    private int puertoMulticast = Config.puertoMul;
    
    private byte[] buffer;
    
    //Para recibir los paquetes
    private DatagramPacket packet;
    
    
    //Direccion multicast del grupo en estructura Inet
    private InetAddress grupo;
    
    /*Referencia al manejador de archivo*/
    private manejoServArch ma;
    
    
    /**Constructor   */
    public Multicast(){
        try {
            socket = new MulticastSocket(puertoMulticast);
        } catch (IOException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         try {
            //direccion  del grupo multicast
            grupo = InetAddress.getByName(ipMulti);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            //asocia el host a la direccion multicast
            socket.joinGroup(grupo);
        } catch (IOException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }
        //buffer
        buffer = new byte[500];
        packet = new DatagramPacket(buffer, buffer.length);
       
        
        
    }
    
    
    
    
    public void enviarMensaje(String msj){
       if(msj!=null){
           byte[] buf = msj.getBytes();
           DatagramPacket dp = new DatagramPacket(buf,buf.length,grupo,
                   puertoMulticast);
            try {
                socket.send(dp);
            } catch (IOException ex) {
                Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
            }
           
       }
        
    }
    
    
    public void oirMulticast(){
        try {
            //recibe el paquete del datagrama
             socket.receive(packet);
        } catch (IOException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }
        //texto que recibi
        
        String msj = new String(packet.getData(),0,packet.getLength());
                
        //AQUI SE CREARIA UN HILO NUEVO PARA TRATAR LO QUE ESCUCHE DEL MULTICAST
        Mensajes mensaje = new Mensajes(msj);
        mensaje.run();
        System.out.println("Leo del Multicast: "+msj);
    }

    
    
    
    
    
    private boolean detener = false;
    
    public void run() {
        System.out.println("Escuchando del multicast host: "+infoRed.miHost());
        while(!detener){
           this.oirMulticast();
        }
    }
    
    public void detener(){
        this.detener = true;
    }
    
    /*Guarda referencia a manejador de archivo*/
    public void setManjadorArchivo(manejoServArch ma){
       this.ma = ma;
    }
    
    
    

}
