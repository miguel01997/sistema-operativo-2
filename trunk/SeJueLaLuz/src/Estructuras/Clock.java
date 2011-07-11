/*
 * Establece la hora para un servidor en particular.
 * Para utilizarla, cada servidor instancia su propio reloj (Clock),
 * de modo que mantiene la información de la fecha y hora exacta,
 * según servidores externos de NTP. De esta forma todos los que
 * usen la clase Clock, estarán sincronizados por un mismo reloj
 * físico.
 * << Utilizar esta clase cada vez que se requiera un manejo de
 * horarios o fechas >>.
 */

package Estructuras;
import java.io.*;
import java.net.*;

/**
 *
 * @author Cesar Hernandez
 */
public class Clock {

    // Para obtener el tiempo, hacer NtpMessage.originateTimestamp
    public Clock(){
        askTime();
    }

    /**
     *
     * Solicita la hora a un servidor NTP que esté disponible.
     */

    public void askTime(){

        try{
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(Config.ntpServers[0]);
        byte[] buf = new NtpMessage().toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address,
                123);
        socket.send(packet);
        socket.receive(packet);
        NtpMessage msg = new NtpMessage(buf);

        System.out.println(msg.toString());
        }catch(Exception e){
            System.out.println("Imposible conectar con NTP");
        }
    }

    public static void main(String args[]){

        Clock e = new Clock();
    }

}