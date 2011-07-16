/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RMI;

import Estructuras.manejoServArch;
import Estructuras.Clock;


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
        //recibe el fichero
        this.recibirFichero(fichero, nombre);
        
        
        if(ocupado){//Encola ejecución y bloquea
           ce.agregarEjecucion(nombre,ipCliente);   
        }
        else{//Prepara el objeto para ejecutar
            Ocupar();
            ce.cargarProceso(nombre,ipCliente);
        }
           ce.ejecutar();
           //System.out.println("Hola");
           this.eliminarArchivo(nombre);
           ce.siguienteProceso();//Carga el siguiente proceso
        
       
        
        
        if(ce.vacio() && ce.sinProc()){
           //Desocupa
           Desocupar();
        }
        
        return solicitarFichero(nombreClase(nombre));
        
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
        ce.eliminarEjecucion(nombre,ipCliente);
        
    }

   // public void agregarEjecucion(String nombre)  {
       
   // }

   
    
    /*Retorna una referencia al manejador de archivos*/
    public manejoServArch retManejadorArchivos(){
       return this.ma;
    }
    

   

    
}
