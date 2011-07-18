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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author necross
 */

public class controlEjecucion {

    private List<String> actividad; //nombre de las clases la cola
    private List<proceso> ejecucion; //proceso asociado a la clase a ejecutar
    private List<String> ips;  //ips de los servidores en la cola
    private List<Integer> transacciones;  //numero de transacciones
    
    //Ip del cliente del proceso
    private String ipCliente;
    
    //Aqui se guarda el hilo que se esta ejecutando
    private proceso ejecutando;
    //Nombre de la clase en ejecucion  x.class
    private String claseEje;
    
    private int numTransaccion;
   
    //Hilo ejecutando aplicacion
    private Thread hEje;
    
    public controlEjecucion(){
       actividad = new ArrayList<String>();
       ejecucion = new ArrayList<proceso>();
       ips = new ArrayList<String>();
       transacciones = new ArrayList<Integer>();
    
    }

    /**Retorna el nombre de la clase en ejecucion*/
    public String getClaseEje() {
        return claseEje;
    }

    /**Retorna la ip del cliente del proceso en ejecucion*/
    public String getIpCliente() {
        return ipCliente;
    }
    
    
    /**Retorna el indice de la transaccion que se esta ejecutando*/
    public int getNumTransaccion(){
       return this.numTransaccion;
    }
    
    /**
     * Agrega a la lista ejecución la siguiente clase nombre.class asociado
     * al cliente cliente
     */
    public boolean agregarEjecucion(String nombre,String cliente,int numTransa){
        
        System.out.println("Encola "+nombre + " de "+cliente+ " trans num "+numTransa);
        //agregamos a la lista de actividades
        actividad.add(nombre);
        //System.out.println(">>>>>"+nombre);
        //Creamos el proceso
        ips.add(cliente);
        proceso p = new proceso(nombre);
        //Agregamos a la lista de ejcucion el proceso
        ejecucion.add(p);
        /**Agrega el numero a la transaccion a la lista*/
        
        transacciones.add(numTransa);
        //Bloquea la llamada del rmi
        //si retorna falso es porque se mando a eliminar la ejecucion
        boolean ejecutar = p.bloquear();
        return ejecutar;
        
    }
    
    
    /**
     * Elimina la primera ocurrencia del proceso
     */
    public  boolean  eliminarEjecucion(String nombre,String ip,int numTransa) {
        //Verifica primero si se esta ejecutando si es asi lo detiene
        if(claseEje != null && ejecutando !=null ){
            //si tiene el mismo nombre el de la activad que se esta ejecutando
            if(nombre.equals(claseEje) && ipCliente.equals(ip) 
                    && numTransaccion==numTransa ){
               this.detenerEjecución();
               System.out.println("Interrumpida la ejecucion de "+nombre +
                       "  cliente: "+ip + " transa num "+numTransa);
               return true;
            }
            
            return this.elimProcEnCola(nombre, ip,numTransa);
        }
        return false;
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
                    enviarListo(ejecutando, this.claseEje);
                } catch (InterruptedException ex) {
                    Logger.getLogger(controlEjecucion.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Ejecucion de "+this.claseEje+" interrumpida.");
                    
                }
               // System.out.println("Ejecuto otro ");
              claseEje = null;
              ejecutando = null;
              hEje = null;
              ipCliente = null;
              this.numTransaccion = -1;
    }
    
    
   
    
    private void enviarListo(proceso proc,String clase){
       try {          
            Date fecha = new Date();  
            long longDate= fecha.getTime();
            String mensaje = "listo "+ infoRed.miIp() +" "+ infoRed.miHost()+" "+
                    clase +" "+ longDate;
            proc.multi.enviarMensaje(mensaje);
        } catch (Exception ex) {
            Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //retorna true si no hay procesos ejecutandose
    public boolean vacio(){
        return actividad.isEmpty();
    }
    
    /**Retorna un booleano que indica si existe un proceso en ejecucion*/
    public boolean sinProc(){
       return claseEje == null;
    }
    
    
    /**
     * 
     */
    private void detenerEjecución(){
       //Mata el proceso
       ejecutando.matar();
       
    }
    
    
    /**
     *Carga el proceso para que se ejecute
     */
    public void cargarProceso(String nombre,String ipcliente,int numTransac){
       if(actividad.isEmpty()){
           claseEje = nombre;
           ejecutando = new proceso(claseEje);
           hEje = new Thread(ejecutando);
           ipCliente = ipcliente;
           this.numTransaccion = numTransac;
       }
    
    }
    
    
    
    /**
     Carga para ejecutar el siguiente proceso
     */
    public void siguienteProceso(){
         try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("No pudo dormir el hilo en siguiente Proceso");
        }
        if(!actividad.isEmpty()){//Mientras que pueda ejecutar
              String act = actividad.remove(0);
              claseEje = act;
              proceso eje = ejecucion.remove(0);
              ejecutando = eje;
              //ip
              ipCliente = ips.remove(0);
              //numero de transaccion
              numTransaccion = transacciones.remove(0);
              //desbloquea la llamada de rmi
              eje.desbloquear();
         }
    
    }
    
    
    /**Elimina la primera ocurrencia del proceso asociado 
     * a un cliente en la cola*/
    public boolean elimProcEnCola(String nomClass,String ipClien,int numTransa){
       //buscamos si el cliente tiene un proceso en la cola
       /* System.out.println(">>>>>");
        System.out.println(ips);
        System.out.println(actividad);
        System.out.println(">>>>>");*/
        
        String aux;
        ArrayList<Integer> l = new ArrayList<Integer>();
        String []  ip = new String[ips.size()];
        ips.toArray(ip);
        //Buscamos todas las ocurrencias de ejecucion del cliente
        for(int i = 0;i<ip.length;i++){
           if(ip[i].equals(ipClien)){
              l.add(i);
           }
        }
        int tam = l.size();
        if(tam==0)//Si no hay procesos de ese cliente en la cola
            return false;
        
        
        for(int i = 0;i<tam;i++){
            int pos = l.remove(0).intValue();
            String ac = actividad.get(pos);
            //Numero de transaccion
            int numTransac= transacciones.get(pos);
            //System.out.println("Actividad "+ac);
           if(  ac.equals(nomClass) && numTransac==numTransa ){
               ips.remove(i);
               actividad.remove(i);
               proceso p = ejecucion.remove(i);
               transacciones.remove(i);
               p.interrumpir();//Marca el proceso para que muera
               p.desbloquear();//debloquea para que termine
               System.out.println("Interrumpida la ejecucion de "+nomClass +
                       " cliente: "+ipClien +" transa num "+numTransa);
               return true;//hay un proceso con ese nombre en la cola
           }
        }
        
        return false;
        
    }
    
    
    /**Busca si un proceso esta en la cola*/
   /* 
    public boolean procEnCola(String nomClass,String ipClien){
        //buscamos si el cliente tiene un proceso en la cola
        
        String aux;
        ArrayList<Integer> l = new ArrayList<Integer>();
        String []  ip = new String[ips.size()];
        ips.toArray(ip);
        for(int i = 0;i<ip.length;i++){
           if(ip[i].equals(ipClien)){
              l.add(i);
           }
        }
        int tam = l.size();
        if(tam==0)//Si no hay procesos de ese cliente en la cola
            return false;
        
        String[] clases = new String[tam];
        l.toArray(clases);//Arreglo de nombre de las clases
        for(int i = 0;i<clases.length;i++){
           if(clases[i].equals(nomClass)){
               return true;//hay un proceso con ese nombre en la cola
           }
        }
        
        return false;
    }*/

}



/**
 Clase creada para correr la aplicacion en un Hilo
 */
class proceso implements Runnable{
    /**Nombre de la clase en ejecucion*/
    private String clase;
   
   /**Hilo de ejecucion*/ 
   private Thread th;
   
   /**Si es true el proceso puede ejecutar sino el proceso fue interrumpido*/
   private boolean ejecutar = true;

   Multicast multi = new Multicast();
    /*Proceso en ejecución*/
    private Process proc;
    
     public proceso(String clase) {
        this.clase = clase;
        this.th = new Thread();
    }
    
        public void run() {
      
            ejecutarClase();
           
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

                 if (this.ejecutar == true){
                    File tempFile = new File(Config.dirDes+"/"+nclass);

                    serviciosRmi.clock.setTimeStamp(tempFile);
                }
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
                     //System.out.println(line);
             }
             in.close();     //cierra el buffer
        } catch (IOException ex) {
            Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    }
    
    
    
    
    private boolean block = true;
    public boolean bloquear(){
       System.out.println("Bloqueado "+clase+".");
            while(block){ 
                 try {
                     Thread.sleep(500);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(proceso.class.getName()).log(Level.SEVERE, null, ex);
                 }
            }
        return ejecutar;
    }
   
    
    public void desbloquear(){
        block = false;
        
    }
    
    public void interrumpir(){
       ejecutar = false;
    }
    

}
