/*
 *Clase creada para llevar el control de las actividades en ejecucion en el
 servidor
 */

package RMI;

import Estructuras.Config;
import Estructuras.Mensajes;
import Estructuras.Multicast;
import Estructuras.infoRed;
import Estructuras.Clock;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author necross
 */

public class controlEjecucion {

    private List<String> actividad;
    private List<proceso> ejecucion;
    private List<String> ips;
    
    //Ip del cliente del proceso
    String ipCliente;
    
    //Aqui se guarda el hilo que se esta ejecutando
    private proceso ejecutando;
    //Nombre de la clase en ejecucion  x.class
    private String claseEje;
   
    //Hilo ejecutando aplicacion
    private Thread hEje;
    
    public controlEjecucion(){
       actividad = new ArrayList<String>();
       ejecucion = new ArrayList<proceso>();
       ips = new ArrayList<String>();
    
    }
    
    /**
     * Agrega a la lista ejecución la siguiente clase
     */
    public boolean agregarEjecucion(String nombre,String cliente){
        //agregamos a la lista de actividades
        actividad.add(nombre);
        //Creamos el proceso
        ips.add(cliente);
        proceso p = new proceso(nombre);
        //Agregamos a la lista de ejcucion el proceso
        ejecucion.add(p);
        
        //Bloquea la llamada del rmi
        p.bloquear();
        return true;
        
    }
    
    
    /**
     * Elimina la primera ocurrencia del proceso
     */
    public  boolean  eliminarEjecucion(String nombre,String ip) {
        //Verifica primero si se esta ejecutando si es asi lo detiene
        if(claseEje != null && ejecutando !=null){
            //si tiene el mismo nombre el de la activad que se esta ejecutando
            if(nombre.equals(claseEje) && ipCliente.equals(ip)){
               this.detenerEjecución();
               System.out.println("Interrumpida la ejecucion de "+nombre);
               return true;
            }
        }
        
        
        if(actividad.contains(nombre)){
           int indice = actividad.indexOf(nombre);
           ejecucion.remove(indice);
           ips.remove(indice);
        }
        return true;
    }
    
    
    public void infoTamano(){
       System.out.println("Act: "+actividad.size()+" Eje: "+ejecucion.size());
    }
    
    
    /*Ejecuta la lista de activades en la cola*/
    public  void ejecutar(){
           System.out.println("Ejecutando "+this.claseEje);
              //eje.ejecutarClase();
              //ejecutando.notify(); 
              //Inicia hilo
              hEje = new Thread(ejecutando);
              hEje.start();
                try {
                    //Espera hasta que muera el hilo
                    hEje.join();
                    System.out.println("Termina ejecutar "+this.claseEje);
                } catch (InterruptedException ex) {
                    Logger.getLogger(controlEjecucion.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Ejecucion de "+this.claseEje+" interrumpida.");
                }
               // System.out.println("Ejecuto otro ");
              claseEje = null;
              ejecutando = null;
              hEje = null;
              ipCliente = null;
    }
    
    
   
    
    
    
    
    //retorna true si no hay procesos ejecutandose
    public boolean vacio(){
        return actividad.isEmpty();
    }
    
    
    public boolean sinProc(){
       return claseEje == null;
    }
    
    
    /**
     * 
     */
    private void detenerEjecución(){
       //Mata el proceso
       ejecutando.matar();
        //Detien el hilo actual
        //hEje.interrupt();
       //ejecutando.interrupt();
       //borra referencias del proceso actual
        //claseEje = null;
        //ejecutando = null;
        //hEje = null;
    }
    
    
    /**
     *Carga el proceso para que se ejecute
     */
    public void cargarProceso(String nombre,String ipcliente){
       if(actividad.isEmpty()){
           claseEje = nombre;
           ejecutando = new proceso(claseEje);
           hEje = new Thread(ejecutando);
           ipCliente = ipcliente;
       }
    
    }
    
    
    
    /**
     Carga para ejecutar el siguiente proceso
     */
    public void siguienteProceso(){
        if(!actividad.isEmpty()){//Mientras que pueda ejecutar
              String act = actividad.remove(0);
              claseEje = act;
              proceso eje = ejecucion.remove(0);
              ejecutando = eje;
              //ip
              ipCliente = ips.remove(0);
              
              //desbloquea la llamada de rmi
              eje.desbloquear();
         }
    
    }
    
    




}



/**
 Clase creada para correr la aplicacion en un Hilo
 */
class proceso implements Runnable{
    /**Nombre de la clase en ejecucion*/
    private String clase;
  
   Thread th;

   Multicast multi = new Multicast();
    /*Proceso en ejecución*/
    private Process proc;
    
     public proceso(String clase) {
        this.clase = clase;
        this.th = new Thread();
    }
    
        public void run() {
      
            ejecutarClase();
            try {          
                DateFormat formatter ;
                Date fecha = new Date();  
                String str_date = fecha.toString();
                formatter = new SimpleDateFormat("dd-MMM-yy");
                fecha = (Date)formatter.parse(str_date); 
                long longDate=fecha.getTime();
                String mensaje = "listo "+ infoRed.miIp() +" "+ infoRed.miHost()+" "+
                        clase +" "+ longDate;
                multi.enviarMensaje(mensaje);
               // multi.run();  //se queda pegado al ejecturar >>
            } catch (ParseException ex) {
                Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

   
    
        
    public void ejecutarClase(){
            try {
                String nombre = this.clase;
                String nclass = nombreClase(nombre);
                //System.out.println("Nombre >> "+nclass+ " "+Config.dirDes);
                //Usamos el classpath para indicar donde estaran las clases
                String dirD =    "java -classpath "+Config.dirDes+" "+nclass +" > " ;
                String[] command = {"sh","-c",dirD + Config.dirDes+"/"+nclass };
                 Process   process = Runtime.getRuntime().exec(command);
                 this.proc  = process;
                 //Esperamos a que se termine de ejecutar
                 process.waitFor();
                 File tempFile = new File(Config.dirDes+"/"+nclass);

                 serviciosRmi.clock.setTimeStamp(tempFile);
                 //process.destroy();
                 //System.out.println("BBB");
                 
            } catch (InterruptedException ex) {
                System.out.println("Proceso "+this.clase+" interrumpido,");
                //Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
            }
             catch (IOException ex) {
                 
                Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void matar(){
       this.proc.destroy();
       //Envia msj de kill por si el proceso sigue vivo
       this.matarProcPegados(this.nombreClase(clase));
       
       
    }
    
    
    /**Si un proceso no muere con destroy se busca su pid y se envia un kill*/
    private void matarProcPegados(String nclass){
       String dirD =    "java -classpath "+Config.dirDes+" "+nclass +" > " ;
       //Solicita la lista de procesos ejecutados por la maquina virtual      
       String[] command = {"sh","-c","jps -lv" };
       Process   process;
       String line;   
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader (process.getInputStream())); 
             while ((line = in.readLine()) != null) {//buscamos el proceso nclass 
                 String [] aux; 
                 aux = line.split(" ");
                 if(aux.length==2){
                     if(aux[1].equals(nclass)){//si el proceso se esta ejecutando
                         //lanzamos comando kill
                         String [] ckill = {"sh","-c", "kill "+aux[0]};
                         process = Runtime.getRuntime().exec(ckill);
                     }
                 }
                     System.out.println(line);
             }
             in.close();     //cierra el buffer
        } catch (IOException ex) {
            Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    }
    
    
    
    
    private boolean block = true;
    public void bloquear(){
       System.out.println("Bloqueado "+clase+".");
            while(block){ 
                 try {
                     Thread.sleep(500);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
                 }
            }
        
    }
   
    
    public void desbloquear(){
        block = false;
        
    }
    

}
