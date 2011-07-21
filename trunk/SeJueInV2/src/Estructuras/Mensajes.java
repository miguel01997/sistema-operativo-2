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
    
    private boolean esServ = true;
    
    Multicast multi = new Multicast();
   

    public Mensajes(String mensaje, manejoServArch maneja, boolean servi) {
        msj = mensaje;
        msa = maneja;
        esServ = servi;
    }


    public  void run() {
        
        if(msj.equals("activo?") && esServ){
            if(this.msa != null){
              this.msa.limpiarTabla();     
            }
            respuesta = "estoy " + infoRed.miIp() + " "+ infoRed.miHost();
            //System.out.println(respuesta);
            multi.enviarMensaje(respuesta);
         }else if(msj.startsWith("listo")){
            String[] parte = msj.split(" ");
            String ip = parte[1];
            String nombre = parte[2];
            String clase = parte[3];
            String fecha = parte[4];
            long lFecha = Long.parseLong(fecha);
            System.out.println("Esta listo ip "+ ip + " nombre "+ nombre
                    + " clase "+ clase + " fecha "+ Clock.dateFormat(lFecha));
            sejuelaluzinterfaz.SeJueLaLuzInterfazView.agregarLog("Esta listo ip "+ ip + " nombre "+ nombre
                    + " clase "+ clase + " fecha "+ Clock.dateFormat(lFecha));
            ActualizarF actf = new ActualizarF();
            if (actf.verifArch(clase, lFecha)){
                actf.actualizarArchivo(ip, nombreClase(clase));
            }

         }else if(msj.startsWith("estoy")){
            String[] parte = msj.split(" ");
            String ip = parte[1];
            String nombre = parte[2];
            
            String [] archIp = msa.retorListArchServ(ip);
            //System.out.println("Agrego servidor "+ip);
            this.msa.agregarArchivosServidor(ip, nombre, archIp);
            if (!esServ){

                this.msa.mostrarTodosArchivos();
            }
            
            System.out.println("Estoy: ip "+ ip + " nombre "+ nombre );
            sejuelaluzinterfaz.SeJueLaLuzInterfazView.agregarLog("Estoy: ip "+ ip + " nombre "+ nombre );
         }
    }

    //Retorna el nombre de la clase
    private String nombreClase(String nombre){
       if(nombre !=null){
            int tam = nombre.length();
            if(tam > 6){
               return nombre.substring(0, tam-6);
            }
       }
       return "";
    }
}
