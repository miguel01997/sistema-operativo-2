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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Cesar Hernandez
 */
public class Clock {

    /* Todas los tiempos contienen MILISEGUNDOS */

    // La última vez que se preguntó la hora al servidor NTP.
    public long lastAskedTime = 0;
    
    // La última vez que se hizo una modificación (de cualquier archivo).
    public long lastModifiedTime = 0;

    // El momento en que se activó el servidor actual.
    public long activationTime = 0;

    /* Cuando se crea un reloj en un servidor, se pregunta la hora al NTP
     * de modo que se sabe el momento en que se activó.
     */
    public Clock(){
        askTime();
        activationTime = lastAskedTime;
    }

    /**
     * Modifica el timestamp de un archivo según el servidor NTP
     * Cuando pregunta la hora, permite modificar el valor de
     * lastModifiedTime, el cual contiene la última vez que modificó un
     * archivo ESTE servidor (en milisegundos de long).
     */

    public void setTimeStamp(File f){

        askTime();
        f.setLastModified(NtpMessage.toLongms(lastAskedTime));
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
        lastAskedTime = NtpMessage.toLongms(msg.originateTimestamp);

        //System.out.println(msg.toString());
        }catch(Exception e){
            System.out.println("Imposible conectar con NTP. Saliendo");
            //Aborta ejecucion
            System.exit(0);
        }
    }

    /*
     * Produce un String con la fecha en formato entendible al usuario.
     */

    public String dateFormat(long ms){

        String date = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(
                new Date(ms));
        return date;
    }

}