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

    private manejoServArch msa;
    
    Multicast multi = new Multicast();


    public Mensajes(String mensaje, manejoServArch maneja) {
        msj = mensaje;
        msa = maneja;
    }


    public void run() {

        if(msj.equals("activo?")){
            respuesta = "estoy " + infoRed.miIp() + " "+ infoRed.miHost();
            //System.out.println(respuesta);
            multi.enviarMensaje(respuesta);
         }else if(msj.startsWith("listo")){
            String[] parte = msj.split(" ");
            String ip = parte[1];
            String nombre = parte[2];
            String clase = parte[3];
            String fecha = parte[4];
            Long lFecha = Long.parseLong(fecha);
            System.out.println("Esta listo ip "+ ip + " nombre "+ nombre
                    + " clase "+ clase + " fecha "+ lFecha);
            ActualizarF actf = new ActualizarF();
            if (actf.verifArch(clase, lFecha)){
                actf.actualizarArchivo(ip, clase);
            }

         }else if(msj.startsWith("estoy")){
            String[] parte = msj.split(" ");
            String ip = parte[1];
            String nombre = parte[2];
            if(this.msa != null){
                this.msa.limpiarTabla();  
                String [] archIp = msa.retorListArchServ(ip);
                this.msa.agregarArchivosServidor(ip, nombre, archIp);
            }
            System.out.println("Activo: ip "+ ip + " nombre "+ nombre );
         }
    }
}
