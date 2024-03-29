

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author necross
 */
public class Cliente {
        //ip del servidor

        //private String ip = "127.0.0.1";
        private String ip = infoRed.miIp();
        
        //Para manejo de los servidores
        private manejoServArch ma;
        
        
        String directorioDescarga = Config.dirDes;
        final int puerto = Config.puerto;
       
        
        /**Manejador de ejecucion*/
        private HashMap<Integer,ManejadorEjecucion> mapaEjecucion;
        
        /**Si la ejecucion fue interrumpida*/
        private boolean interumpido = false;
        
        //Numero para indicar la transaccion
        private static Integer indiTransa;
       
        private static Multicast mul;
    /**
     * @param args the command line arguments
     */
    public Cliente() {
        //Crea directorio de descarga
        this.crearDirDescarga();
        ma = new manejoServArch();
        mapaEjecucion = new HashMap<Integer, ManejadorEjecucion>();
        if(indiTransa == null){
          indiTransa = 0;
        }
       
    }
    
    
    
    public void iniciar(){

                //Se afilia al multicast
                mul = new Multicast(false);
                Thread tMul = new Thread(mul);
                
                mul.setManjadorServArch(ma);
                
                tMul.start();
                servidoresActivos();
                //prueba();
              
    }
    

    
    
    
    public void prueba(){
       
            //try{ 
            //Pregunta por servidores vivos al multicast.
            /*servidoresActivos();
            String process =
           
           /* String process =
                    java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
            long pid = Long.parseLong(process.split("@")[0]);
            System.out.println("My pid is "+ pid);

            //Solicita la lista de ip a los servidores vivos
            /*interfazServicioRmi sr;
            
            try{
            sr = (interfazServicioRmi)
            Naming.lookup( "rmi://"+ip+":"+puerto+"/Servicio");
            sr.terminarEjecucion("p.class", "127.0.0.1",1);
            
              System.out.println("Ocupado "+sr.ocupado() );
            
            }
            catch (MalformedURLException murle ) {
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
            System.out.println (ae);}   
            
            //lista de ficheros en el servidor
            //this.listarArchivos(sr);

            //para preguntar cuáles servidores están activos
            //this.servidoresActivos();

            //Detener proceso en servidor
            //sr.terminarEjecucion("p.class",ip);*/
            
         try {    
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        //ejecutar archivo en el servidor, envia ip del cliente
         this.ejecutarEnServidores("../","p2.class");
         System.exit(0); 
         
        //this.ejecutarEnServidores("p.class");
        //this.ejecutarEnServidores("p.class");
        
        
        
        
        
        
        //Si esta ocupado se agrega ejecución

        //Subir archivo
        //this.enviarArchivo("manifest.mf");

        //descargar archivo
        //this.solicitarArchivo("pServidor.class");

        //sr.tamanoEjecucion();
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
    private void ejecutar(interfazServicioRmi sr,String nombre,String ipCliente,
                          int numTransac){
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
                    byte[] arch = sr.ejecutar(buffer, nombre,ipCliente,numTransac);
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
         mul.enviarMensaje("activo?");
    }
    
    
    
    /*Busca servidores activos y envia la solicitud de ejecución de la clase 
     nombre y le asocia la ip ipCliente
     */
    public void ejecutarEnServidores(String url,String nombre){
        
        int numTransa = this.indiTransa;
        this.indiTransa = this.indiTransa +1;
        ManejadorEjecucion me = new ManejadorEjecucion(ma);
        mapaEjecucion.put(new Integer(numTransa), me); 
        System.out.println("Cliente num transa "+indiTransa);
        
        me.ejecutarEnServidores(url,nombre, ip,numTransa);
        
    }
    
    
    /**Detiene la ejecucion de la clase nomClass*/
    public void detenerEjecucion(String nomClass,int indiTran ){
        //System.out.println(">>>"+mapaEjecucion);
        ManejadorEjecucion me = mapaEjecucion.remove(indiTran);
       
       if(me==null){
          System.out.println("No hay transaccion asociada a " +nomClass);
          return;
       }
       me.detenerEjecucion();
    }
    
    /**Retorna el numero de la ultima transaccion*/
    public int retTransa(){
       return Cliente.indiTransa;
    }
    
    
}

/*****************************************************************************/
//*Clase creada para manejar los hilos de ejecución*/
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
    public  void ejecutarEnServidores(String url,String nombreClass,String ipCliente,
            int numTransaccion){
       
       boolean ejecutar = false;
       while(!ejecutar){
           String [] ipServidores = ma.listarServidores();
            //System.out.println("TAMMMM >> "+ipServidores.length);
           if(ipServidores !=null){//Si hay suficientes servidores para ejecutar
               //System.out.println("TAMMMM >> "+ipServidores.length);
               if(ipServidores.length>=numServi){//Ejecución segura
                   //Se asigna el numero de servidores a comunicarse
                   numServi = Config.numServidores;
                   //Se crea arreglo de ejecucion
                   arrEje = new Ejecucion[numServi];
                  //selecciona los serviodores que ejecutan las clases
                  String [] aux = ma.solicitarServidor(numServi);
                  String res;
                  for(int y=0;y<aux.length;y++){
               //System.out.println(">>>>>>"+aux[y]);
            }
                  res = crearEjecucion(url,nombreClass, ipCliente, aux,numTransaccion);
                  if(res!=null){//Si no se pudo conectar a un servidor se sale
                     //Elimina el servidor al que no se pudo conectar
                     //de la lista de servidores
                      ma.eliminarServidor(res);
                      //Vuelve a solicitar los servidores
                      if(res.equals("")){
                         System.out.println("No hay servidores para ejecutar");
                         return ;
                      }
                      
                      continue;
                      
                  }
                  
                  //Puede ejecutar
                  ejecutar =  true;
                  
               }else{//Ejecucion insegura
                   if(Config.inseguro){
                       System.out.println("Ejecutando en modo inseguro");
                      /*Si se va a ejecutar aunque no haya el minimo de servidores
                       solicitados*/
                       //Se crea un nuevo arreglo de ejecucion
                       numServi = ipServidores.length;
                       arrEje = new Ejecucion[numServi];
                       String [] aux = ma.solicitarServidor(numServi);
                       String res;
                      res = crearEjecucion(url,nombreClass, ipCliente, aux,numTransaccion);
                      if(res!=null){//Si no se pudo conectar a un servidor se sale
                         //Elimina el servidor al que no se pudo conectar
                         //de la lista de servidores
                          ma.eliminarServidor(res);
                          //Vuelve a solicitar los servidores
                         /*if(res.equals("")){
                            System.out.println("No hay servidores para ejecutar");
                            return ;
                         }*/
                          continue;
                      }
                      //Puede ejecutar
                      ejecutar =  true;
                   }else{
                     System.out.println("Numero de servidores conectados menor al numero minimo requerido");
                     System.out.println("No ejecuta "+nombreClass);
                     return; 
                   }
                   
               }
           } 
           else{//No hay servidores
              System.out.println("No hay Servidores disponibles.");
              return;
           }
       }
       //EJECUTA LA APLICACION
       synchronized(this){
          ejeTodosHilos();
          //System.out.println("Ejecuta todos los hilos "+aux.length);
            try {
                wait();//Espera a que algun hijo termine
            } catch (InterruptedException ex) {
                Logger.getLogger(ManejadorEjecucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          //System.out.println("Sale del wait");
          //verifica el servidor que termino y manda a matar a los otros
          this.verificarHilos();
    }
   
    
    /**
     * Crea en arrEje un arrego de apuntadores a los servidores rmi
     * Retorna una lista con los servidores que no logro conectarse*/
    private String crearEjecucion(String url,String nombreClass,String ipCliente,
                                   String[] ipServidor,int numTransaccion){
        
        String ipAux = "";
      try{
        //Buscamos las ip de los servidores   
         // System.out.println("Ips "+ipServidor.length + " "+arrEje.length);
        for(int i=0;i<ipServidor.length;i++){
           //ipAux = ipServidor[i];
            interfazServicioRmi sr;
           sr = (interfazServicioRmi)
           Naming.lookup( "rmi://"+ipServidor[i]+":"+Config.puerto+"/Servicio");    
           //Guarda en la lista de ejecucion la referencia al rmi
           arrEje[i] = new Ejecucion(sr,url, nombreClass, ipCliente,this,numTransaccion);
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
        
        //System.out.println (re);
        return ipAux; 
        }
        catch (NotBoundException nbe) {
        System.out.println ();
        System.out.println ("NotBoundException");
        System.out.println (nbe);}
        catch (java.lang.ArithmeticException ae) {
        System.out.println ();
        System.out.println ("java.lang.Arithmetic Exception");
        System.out.println (ae);} 
      return null;
    
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
       boolean replicado = false;
       for(int i=0;i<arrEje.length;i++){
          if(arrEje[i].termine()){
             //Lo manda a replicar
             if(!replicado){
             System.out.println("Termino servidor "+arrEje[i].servidor());    
             arrEje[i].replicar();
             replicado = true;
             }
          }
       }
    }
    
    
    public void detenerEjecucion(){
       for(int i=0;i<arrEje.length;i++){
          arrEje[i].detenerEjecucion();
       }
        
    }
    

    
}


/*****************************************************************************/
/*Clase que maneja la instancia de ejecucion (el hilo)*/
class Ejecucion implements Runnable{
    
    /*Servidor con servicio rmi*/
    private RMI.interfazServicioRmi rmiServ;
    
    /*Nombre de la clase que esta ejecutando p.class*/
    private String nombreClass ;
        
    /**La ip del cliente*/
    private String ipCliente;
    
    /**Para saber si termino la ejecución*/
    private boolean termine;
    
    private String url = "";
    
    
    
    private int numTransaccion;
    
    /**Referencia al manejador de ejecucion*/
    private ManejadorEjecucion me;
    
    public Ejecucion(RMI.interfazServicioRmi rmi,String url,String nombreClass,
                     String ipCliente,ManejadorEjecucion me,int numTransac){
       this.nombreClass = nombreClass;
       this.ipCliente = ipCliente;
       this.rmiServ = rmi;
       termine = false;
       this.me = me;
       this.numTransaccion = numTransac;
       this.url = url;
       
    }
    
    
    public synchronized  void run() {
        System.out.println("Transaccion "+numTransaccion);
        ejecutar(rmiServ, url ,nombreClass, ipCliente,numTransaccion);
        termine = true;
        //System.out.println("Notifica al padre");
        me.despertar();
        
    }
   //

    
    
    /**
    Manda a ejecutar el archivo nombre en un servidor
    */
    private void ejecutar(interfazServicioRmi sr,String url,String nombre,String ipCliente,
                          int numTransac  ){
       //File f = new File(this.directorioDescarga+"/"+nombre);
       File f = new File(url+nombre);
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
                    String ipServer = sr.ip();
                    System.out.println("Ejecutando clase "+nombre + " en " + ipServer );
                    //SERVIDOR
                    byte[] arch = sr.ejecutar(buffer, nombre,ipCliente,numTransac);
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
        try {
            return rmiServ.ip();
        } catch (RemoteException ex) {
            Logger.getLogger(Ejecucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public void detenerEjecucion(){
        try {
            this.rmiServ.terminarEjecucion(nombreClass, ipCliente,numTransaccion);
        } catch (RemoteException ex) {
            Logger.getLogger(Ejecucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        termine = true;
    
    }
    
    /**Le indica al rmi que replique el archivo*/
    public boolean replicar( ){
        try {
            return  rmiServ.replicar(nombreClase(nombreClass));
        } catch (RemoteException ex) {
            Logger.getLogger(Ejecucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}




