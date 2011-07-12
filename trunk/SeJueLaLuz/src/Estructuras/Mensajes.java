/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructuras;


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


         }else if(msj.startsWith("estoy")){
            char[] chars = msj.toCharArray();
            char ip = chars[2];
           
         }
    }
}
