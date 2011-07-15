

package Estructuras;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import RMI.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author necross
 */
public class Cliente {
        //ip del servidor

        private String ip = "192.168.1.123";

        
        //Para manejo de los servidores
        private manejoServArch ma;
        
        
        String directorioDescarga = Config.dirDes;
        final int puerto = Config.puerto;
       
        
        /**Manejador de ejecucion*/
        private ManejadorEjecucion me;
       
    /**
     * @param args the command line arguments
     */
    public Cliente() {
        //Crea directorio de descarga
        this.crearDirDescarga();
        ma = new manejoServArch();
        me = new ManejadorEjecucion(ma);
    }
    
    
    
    public void iniciar(){

                //Se afilia al multicast
                //Multicast mul = new Multicast();
                
                //Thread tMul = new Thread(mul);
                
               // mul.setManjadorServArch(ma);

                //tMul.start();
                prueba();
              
    }
    

    
    
    
    public void prueba(){
        //try{ 
        //Pregunta por servidores vivos al multicast.
        //Solicita la lista de ip a los servidores vivos
        //interfazServicioRmi sr;
        //sr = (interfazServicioRmi)
        //Naming.lookup( "rmi://"+ip+":"+puerto+"/Servicio");                
        //lista de ficheros en el servidor
        //this.listarArchivos(sr);

        //para preguntar cuáles servidores están activos
        //this.servidoresActivos();

        //Detener proceso en servidor
        //sr.terminarEjecucion("p.class",ip);
        
        
        

        //ejecutar archivo en el servidor
        me.ejecutarEnServidores("p2.class", ip);
        
        //Si esta ocupado se agrega ejecución

        //Subir archivo
        //this.enviarArchivo("manifest.mf");

        //descargar archivo
        //this.solicitarArchivo("pServidor.class");

        //sr.tamanoEjecucion();
    //}
      /*  catch (MalformedURLException murle ) {
        System.out.println ();
        System.out.println (
        "MalformedURLException");
        System.out.println ( murle ); }
        catch (RemoteException re) {
        System.out.println ();
        System.out.println ( "RemoteException");
        System.out.println (re); }
        catch (NotBoundException nbe) {
        System.out.println ();
        System.out.println ("NotBoundException");
        System.out.println (nbe);}
        catch (java.lang.ArithmeticException ae) {
        System.out.println ();
        System.out.println ("java.lang.Arithmetic Exception");
        System.out.println (ae);}   */ 

    }
    
    
    
    
    
    
    /**
     * Metodo para solicitar archivo al servidor desde el cliente
    */
    public void solicitarArchivo(String nombre ,interfazServicioRmi sr ) {
         byte [] buffer = null;
        try {
            buffer = sr.solicitarFichero(nombre);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
         if(buffer != null){
            File file = new File(this.directorioDescarga+"/"+nombre);
            try {
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                try {
                    output.write(buffer,0,buffer.length);
                    output.flush();
                    output.close();
                    System.out.println("Archivo "+nombre+" Creado");
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }

         }
         else{
             System.out.println("No se encontro el archivo "+nombre);
         }
    
    }
    
    
    /****
     * Envia un archivo al servidor que se encuentr en el directorio de descarga
     * de nombre nombre
     */
    public boolean enviarArchivo(String nombre,interfazServicioRmi sr){
        File f = new File(this.directorioDescarga+"/"+nombre);
        if(f.exists() && f.isFile() ){
            
            byte[] buffer = new byte[(int)f.length()];
            try {
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));
                try {
                    input.read(buffer,0,buffer.length);
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    boolean enviado = sr.recibirFichero(buffer, nombre);
                    if(enviado){
                       System.out.println("Archivo "+nombre+" enviado.");
                    }
                    else{
                       System.out.println("Archivo "+nombre+" no pudo ser enviado.");
                    }
                    return  enviado;
                } catch (RemoteException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }
        return false;
       
    }
    
    
    
    
    
    /**
     Crea el directorio donde se descargaran los archivos
     */
    private void crearDirDescarga(){
        File f = new File(this.directorioDescarga);
        if(f.exists() ){
            if(!f.isDirectory()){
               f.mkdir();
               System.out.println("Directorio de descarga "+
                       this.directorioDescarga+" creado.");
            }
        }
        else{
           f.mkdir();
            System.out.println("Directorio de descarga "+
                       this.directorioDescarga+" creado.");
        }
    }
    
    
    
    /**
     * Solicita los archivos de un servidor rmi
     */
    public void listarArchivos(interfazServicioRmi sr){
        System.out.println("Listando Archivos:");
        try {
            String [] lista = sr.listarFicheros();
            if(lista!=null){
               for(int i =0;i<lista.length;i++){
                  System.out.println(lista[i]);
               }
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
   /**
    Manda a ejecutar el archivo nombre en un servidor
    */
    private void ejecutar(interfazServicioRmi sr,String nombre,String ipCliente){
       //File f = new File(this.directorioDescarga+"/"+nombre);
       File f = new File("../"+nombre);
        if(f.exists() && f.isFile() ){
            
            byte[] buffer = new byte[(int)f.length()];
            try {
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));
                try {
                    input.read(buffer,0,buffer.length);
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    //SERVIDOR
                    byte[] arch = sr.ejecutar(buffer, nombre,ipCliente);
                    //Si hay una respuesta
                    if(arch != null){
                    //Crea el archivo de respuesta en el cliente
                        crearArchivoSolicitado(arch, nombreClase(nombre));
                    }
                    /*if(enviado){
                       System.out.println("Archivo "+nombre+" enviado.");
                    }
                    else{
                       System.out.println("Archivo "+nombre+" no pudo ser enviado.");
                    }
                    return  enviado;*/
                } catch (RemoteException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }
        else{
            System.out.println("No se encontro archivo "+nombre);
        }
          // return false;
      }
    
    
    /*
    Crea un archivo en el directorio de descarga
    */
    private boolean crearArchivoSolicitado(byte [] buffer, String nombre){
       if(buffer != null){
            //File file = new File(directorioDescarga+"/"+nombre);
            File file = new File("../"+nombre);
            if(!file.isDirectory()){
                    BufferedOutputStream output = null;
                try {
                    output = new BufferedOutputStream(new FileOutputStream(file));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.write(buffer,0,buffer.length);
                    output.flush();
                    output.close();
                    System.out.println("Archivo "+nombre+" Creado");
                    return true;
                } catch (IOException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             else{
             System.out.println("No se creo el archivo "+nombre);
            }

         }
         else{
             System.out.println("No se creo el archivo "+nombre);
             
         }
         return false;
    
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

    /*Envia mensaje por la dirección multicast
     * para preguntar por los servidores activos
     */
    private void servidoresActivos(){
         Multicast multi = new Multicast();
         multi.enviarMensaje("activo?");
    }
    
    
    
    /*Busca servidores activos y envia la solicitud de ejecución de la clase 
     nombre y le asocia la ip ipCliente
     */
    public void enjecutarEnServidores(String nombre,String ipCliente){
       
    
    }
    
    
    
    
}

/*****************************************************************************/


/*****************************************************************************/

/*****************************************************************************//*Clase creada para manejar los hilos de ejecución*/
class ManejadorEjecucion {

    
    //Para tener control de servidores
    private manejoServArch ma;
    
    /*numero de servidores que van a ejecutar la clase*/
    private int numServi = Config.numServidores;

    private Ejecucion[] arrEje;
    /**Grupo de hilos*/
    private ThreadGroup tg;
    
    
    /*Constructor que recibe el manejador de servidores*/
    public ManejadorEjecucion(manejoServArch ma){
       this.ma = ma;
       arrEje = new Ejecucion[numServi];
    }
    
    
    /**Busca servidores activos y envia la solicitud de ejecución de la clase 
     nombre y le asocia la ip ipCliente
     */
    public  void ejecutarEnServidores(String nombreClass,String ipCliente){
       String [] ipServidores = ma.listarServidores();
       
       if(ipServidores !=null){
           if(ipServidores.length>=numServi){//Ejecución segura
              //selecciona los serviodores que ejecutan las clases
              String [] aux = ma.solicitarServidor(numServi);
              boolean res;
              res = crearEjecucion(nombreClass, ipCliente, aux);
              if(!res){//Si no se pudo conectar a un servidor se sale
                 System.exit(0);
              }
              synchronized(this){
              //Thread te = new Thread(this);
              //te.start();
              ejeTodosHilos();
              System.out.println("Ejecuta todos los hilos "+aux.length);
                try {
                    wait();//Espera a que algun hijo termine
                } catch (InterruptedException ex) {
                    Logger.getLogger(ManejadorEjecucion.class.getName()).log(Level.SEVERE, null, ex);
                }
              }
              //System.out.println("Sale del wait");
              //verifica el servidor que termino
              this.verificarHilos();
              
              
              
           }else{
              System.out.println("Numero de servidores menor al numero minimo requerido");
           
           }
       } 
       else{//Ejecucion insegura
          System.out.println("Servidores no existen");
       }
    }
   
    
    
    private boolean crearEjecucion(String nombreClass,String ipCliente,
            String[] ipServidor){
        String ipAux = "";
      try{
        //Buscamos las ip de los servidores   
        for(int i=0;i<ipServidor.length;i++){
           //ipAux = ipServidor[i];
            interfazServicioRmi sr;
           sr = (interfazServicioRmi)
           Naming.lookup( "rmi://"+ipServidor[i]+":"+Config.puerto+"/Servicio");    
           //Guarda en la lista de ejecucion la referencia al rmi
           arrEje[i] = new Ejecucion(sr, nombreClass, ipCliente,this);
           System.out.println("Servidores "+ipServidor[i]);
        }
        
      }
        catch (MalformedURLException murle ) {
        System.out.println ();
        System.out.println (
        "MalformedURLException");
        System.out.println ( murle ); }
        catch (RemoteException re) {
        System.out.println ();
        System.out.println ( "RemoteException");
        System.out.println("Error al conectar con el servidor "+ipAux);
        System.out.println (re); 
        return false; 
        }
        catch (NotBoundException nbe) {
        System.out.println ();
        System.out.println ("NotBoundException");
        System.out.println (nbe);}
        catch (java.lang.ArithmeticException ae) {
        System.out.println ();
        System.out.println ("java.lang.Arithmetic Exception");
        System.out.println (ae);} 
      return true;
    
    }
    
    
    /**Ejecuta todos los hilos de ejecución*/
    private void ejeTodosHilos(){
        Thread[] hilos = new Thread[numServi];
        //tg = new ThreadGroup(""+numServi);
        
        //Ejecuta todos los hilos
        for(int i = 0 ; i<numServi;i++){
           Thread t = new Thread(arrEje[i]);
           t.start();
        }
    }
    
    
    public void run() {
        ejeTodosHilos();
    }
    
    /**Metodo usado por los hijos para despertar a los padres*/
    public void despertar(){
      synchronized(this){
        notify();
      }
    }
    
    /**Verifica que hilo termino de ejecutar y manta a cancelar los demas*/
    private void verificarHilos(){
       for(int i=0;i<arrEje.length;i++){
          if(arrEje[i].termine()){
             System.out.println("Termino servidor "+arrEje[i].servidor());
          }
       }
    }
    
    

    
}


/*****************************************************************************/
/*Clase que maneja la instancia de ejecucion (el hilo)*/
class Ejecucion implements Runnable{
    
    /*Servidor con servicio rmi*/
    private RMI.interfazServicioRmi rmiServ;
    
    /*Nombre de la clase que esta ejecutando*/
    private String nombreClass ;
        
    /**La ip del cliente*/
    private String ipCliente;
    
    /**Para saber si termino la ejecución*/
    private boolean termine;
    
    /**Referencia al manejador de ejecucion*/
    private ManejadorEjecucion me;
    
    public Ejecucion(RMI.interfazServicioRmi rmi,String nombreClass,
                     String ipCliente,ManejadorEjecucion me){
       this.nombreClass = nombreClass;
       this.ipCliente = ipCliente;
       this.rmiServ = rmi;
       termine = false;
       this.me = me;
    }
    
    
    public synchronized  void run() {
        ejecutar(rmiServ, nombreClass, ipCliente);
        termine = true;
        //System.out.println("Notifica al padre");
        me.despertar();
        
    }
   //

    
    
    /**
    Manda a ejecutar el archivo nombre en un servidor
    */
    private void ejecutar(interfazServicioRmi sr,String nombre,String ipCliente){
       //File f = new File(this.directorioDescarga+"/"+nombre);
       File f = new File("../"+nombre);
        if(f.exists() && f.isFile() ){
            
            byte[] buffer = new byte[(int)f.length()];
            try {
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));
                try {
                    input.read(buffer,0,buffer.length);
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    //SERVIDOR
                    byte[] arch = sr.ejecutar(buffer, nombre,ipCliente);
                    //Si hay una respuesta
                    if(arch != null){
                    //Crea el archivo de respuesta en el cliente
                        crearArchivoSolicitado(arch, nombreClase(nombre));
                    }
                    /*if(enviado){
                       System.out.println("Archivo "+nombre+" enviado.");
                    }
                    else{
                       System.out.println("Archivo "+nombre+" no pudo ser enviado.");
                    }
                    return  enviado;*/
                } catch (RemoteException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println("No se encontro archivo "+nombre);
        }
          // return false;
      }
    
    
    
    /*Retorna el nombre de la clase*/
    private String nombreClase(String nombre){
       if(nombre !=null){
            int tam = nombre.length();
            if(tam > 6){
               return nombre.substring(0, tam-6);
            }
       }
       return "";
    }
    
    
    
    /**
    Crea un archivo en el directorio de descarga
    */
    private boolean crearArchivoSolicitado(byte [] buffer, String nombre){
       if(buffer != null){
            //File file = new File(directorioDescarga+"/"+nombre);
            File file = new File("../"+nombre);
            if(!file.isDirectory()){
                    BufferedOutputStream output = null;
                try {
                    output = new BufferedOutputStream(new FileOutputStream(file));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.write(buffer,0,buffer.length);
                    output.flush();
                    output.close();
                    System.out.println("Archivo "+nombre+" Creado");
                    return true;
                } catch (IOException ex) {
                    Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             else{
             System.out.println("No se creo el archivo "+nombre);
            }

         }
         else{
             System.out.println("No se creo el archivo "+nombre);
             
         }
         return false;
    
    }
    
    /**Pregunta si termino la ejecución*/
    public boolean termine(){
     return this.termine;   
    }
    
    /**Retorna la ip del servidor asosicado a la ejecución*/
    public String servidor(){
       return ipCliente;
    }
    
    
}




