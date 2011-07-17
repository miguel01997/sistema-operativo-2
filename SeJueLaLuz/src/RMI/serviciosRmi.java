/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RMI;

import Estructuras.manejoServArch;
import Estructuras.Clock;
import Estructuras.Config;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class serviciosRmi extends java.rmi.server.UnicastRemoteObject
implements interfazServicioRmi {
   
    //Identificador
    static final long serialVersionUID = 1;
    
    
    //Donde descargara y subira los archivos 
   // String directorioDes =  Config.dirDes;
    
    //Para indicar si el servidor esta ocupado
    private boolean ocupado;
    //Manejador de procesos en ejecución
    controlEjecucion ce;
    
    //Para manejar los archivos existentes en el servidor
    private manejoServArch ma;

    // Referencia al reloj del servidor.
    public static Clock clock;
    
    /**Si la ejecucion fue interrumpida*/
    private boolean interumpido = false;
    
    //CONSTRUCTOR
    public serviciosRmi() throws java.rmi.RemoteException{
        super();

        ocupado = false;
        ce=new controlEjecucion();
        //Crea el directorio de descarga
        ma = new manejoServArch();
        clock = new Clock();
    }
    
    //CONSTRUCTOR
    public serviciosRmi(String directorio) throws java.rmi.RemoteException{
       super();
       ocupado = false;
       ce=new controlEjecucion();
       //Crea el directorio de descarga
       ma = new manejoServArch(directorio);
       clock = new Clock();
    }
    
    
    
    public byte[] solicitarFichero(String nombre) throws java.rmi.RemoteException{
        return ma.solicitarFichero(nombre);
    }

    
    //Retorna un arreglo de string con el nombre de los archivos en el
    //servidor en la carpeta de descarga
    public String[] listarFicheros()throws java.rmi.RemoteException{
      return ma.listarFicheros();
    }
    
    
    
    public boolean recibirFichero(byte [] buffer, String nombre){
        return ma.recibirFichero(buffer, nombre);
    }
    
    
    
    
    
    
    /**
     * Ejecuta archivo en el servidor
     */
    public byte[] ejecutar(byte[] fichero, String nombre,String ipCliente) 
            throws java.rmi.RemoteException {
        
        
        
        if(ocupado){//Encola ejecución y bloquea
           
            ce.agregarEjecucion(nombre,ipCliente);
           //recibe el fichero
           this.recibirFichero(fichero, nombre);
        }
        else{//Prepara el objeto para ejecutar
            
            Ocupar();
            //recibe el fichero
            this.recibirFichero(fichero, nombre);
            ce.cargarProceso(nombre,ipCliente);
           
        }
           
           ce.ejecutar();
           //System.out.println("Hola");
           if(!this.eliminarArchivo(nombre)){
             //Si no encontro el archivo para eliminarlo es porque hubo un error
             this.interrumpir();//Marca la ejecucion como si fuera sido interrumpida
           }
           ce.siguienteProceso();//Carga el siguiente proceso
        
        if(ce.vacio() && ce.sinProc()){
           //Desocupa
           Desocupar();
        }

           String nomArch = nombreClase(nombre);
           if(!interumpido){//Si no fue interrumpido replica
              //Replica los archivos
              System.out.println("Inicia replica");
              replicar(nomArch);
              System.out.println("Termina replica");
              return solicitarFichero(nomArch);
           }
           this.iniInterrumpir();
           return null;
    }
    
    
    //Elimina el archivo de nombre nombre en la carpeta de descarga
    public boolean eliminarArchivo(String nombre){
        return ma.eliminarArchivo(nombre);
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
    
    
    
    public boolean ocupado(){
       return this.ocupado;
    }
    
    public synchronized void Ocupar(){
        this.ocupado = true;
    }
    
    
    public synchronized void Desocupar(){
       this.ocupado = false;
    }
    
    public void tamanoEjecucion(){
       ce.infoTamano();
    }

    public void terminarEjecucion(String nombre,String ipCliente){
        this.interrumpir();
        //Elimina el archivo de respuesta referente a la ejecucion de nombre 
        ma.eliminarArchivo(nombreClase(nombre));//elimina archivo respuesta
        ce.eliminarEjecucion(nombre,ipCliente);
        
        
    }

   // public void agregarEjecucion(String nombre)  {
       
   // }

   
    
    /*Retorna una referencia al manejador de archivos*/
    public manejoServArch retManejadorArchivos(){
       return this.ma;
    }
    

    
    
    private synchronized void interrumpir(){
       this.interumpido = true;
    }
    
    private synchronized void iniInterrumpir(){
       this.interumpido = false;
    }

    
    
    
// PARA MANEJAR LAS REPLICAS ***********************************************/
   
    
    public boolean replicar(String nombreClass){
        boolean ejecutar = false;
         interfazServicioRmi [] rmiServer = null;
         String [] servidores = null;
        while(!ejecutar){
            //Numero Servidores conectados
            int numSerCon = ma.numServidores();
            if(numSerCon == 0 ) //No hay servidores para replicar
            return false; //no pudo replicar
            
            int numReplicas = (numSerCon/2)+1;
            // System.out.println("Solicita "+numReplicas + " replicas.");
            servidores=  ma.solicitarServidor(numReplicas);
            rmiServer = new interfazServicioRmi[servidores.length];
            String serError = "";
            serError = solicitarServidores(servidores, rmiServer);
            if(serError == null)//no hubo errores
                ejecutar = true;
            else{//No se pudo conectar con serError
            //Elimina servidor de la lista
                ma.eliminarServidor(serError);
                //Vuelve a solicitar los servidores
                //Si hay conflicto se podria mandar a actualizar los servidores
            }
        }
        //PUEDE REPLICAR
        return replicarArchivo(nombreClass, rmiServer,servidores);
    }
    
    
    /**sube a los servidores en si el archivo nombreClass*/
    private boolean replicarArchivo(String nombreClass, interfazServicioRmi[] si,
            String [] ipServ){
       if(nombreClass == null || si == null)
          return false;
        
        try {
            byte[] archivo = this.solicitarFichero(nombreClass);
            
            for(int i = 0;i<si.length;i++){
              si[i].recibirFichero(archivo, nombreClass);
              System.out.println("Replicando "+nombreClass +" en "+
                      ipServ[i]);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(serviciosRmi.class.getName()).log(Level.SEVERE, null, ex);
        }
         //System.out.println("Replicando respuesta de "+nombreClass);
       return true;
    }
    
    
    
    /**Establece comunicacion con los servidore si no se puede comunicar con
     * un servidor retorna en un string la ip del servidor que dio problemas
     */
    private String solicitarServidores(String [] servidores,interfazServicioRmi[] is)
    {
       if(servidores == null || is == null)
           return null;
       String ipAux = "";
       
       try{
       
           for(int i = 0;i<servidores.length;i++){
               ipAux = servidores[i];
               interfazServicioRmi sr;
               sr = (interfazServicioRmi)
               Naming.lookup( "rmi://"+ipAux+":"+Config.puerto+"/Servicio");    
               //Guarda en la lista de ejecucion la referencia al rmi
               is[i] = sr;
           }
           //salida ok
           return null;
       }
        catch (MalformedURLException murle ) {
        System.out.println ();
        System.out.println (
        "MalformedURLException");
        System.out.println ( murle ); }
        catch (RemoteException re) {
        //System.out.println ();
        //System.out.println ( "RemoteException");
        //System.out.println("Error al conectar con el servidor "+ipAux);
        //System.out.println (re);
        return ipAux; //retorna la ip del servidor que dio el problema
        }
        catch (NotBoundException nbe) {
        System.out.println ();
        System.out.println ("NotBoundException");
        System.out.println (nbe);}
        catch (java.lang.ArithmeticException ae) {
        System.out.println ();
        System.out.println ("java.lang.Arithmetic Exception");
        System.out.println (ae);} 
        return "";
    }
    
}
