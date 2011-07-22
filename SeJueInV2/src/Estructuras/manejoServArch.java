/**
 * Clase que controla la lista de archivos en el servidor local
 * y almacena informacion de los demas archivos en los otros servidores
 */

package Estructuras;

import RMI.interfazServicioRmi;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author necross
 */
public class manejoServArch {

    //Directorio de descarga de los archivos;
    private String directorioDes = Config.dirDes;
    
    /**Tabla que maneja los archivos y sevidores*/
    public tablaArchivos ta;

    
    
    
    
    
    
    //CONSTRUCTOR
    //Recibe la url donde se almacenaran los archivos descargados
    public manejoServArch(String dirDescarga) {
        this.directorioDes = dirDescarga;
        //Crea el directorio donde se descargan los archivos
        this.crearDirDescarga();
        //crea tabla de archivos locales
        ta = new tablaArchivos();
        this.auxArchivosLocales();
       
    }
    
    public manejoServArch() {
       
        //Crea el directorio donde se descargan los archivos
        this.crearDirDescarga();
        //crea tabla de archivos locales
        ta = new tablaArchivos();
        //Carga los archivos locales
        this.auxArchivosLocales();

        //Crear el directorio de seguridad
        RMI.seguridad seg = new RMI.seguridad();
        seg.createSecurityDir();
        seg.createPolicyFile();
        
    }
    
    
    
    /**Busca un archivo y retorna la ip del servidor donde se encuentra*/
    public String busArch(String nArchivo){
       return ta.busArch(nArchivo);
    }
    
    /**Busca entre todos los servidores el archivo**/
    public void rastrearArchivos(String nClase){
       String ipServer = busArch(nClase);
       
       
       
    }
    
    
    /**Retorna una lista con todos los archivos*/
    public String[] todosArchivos(){
       return ta.todosArchivos();
    }
    
    
    
    /**
     * Retorna una lista con los archivos en el directorio de descarga
     */
    private String[] lFicheros(){
        File dir = new File(Config.dirDes);
        
        String[] ficheros = dir.list();
        if (ficheros == null)
            return null;
        else {
             return ficheros;
       }
    }
    
    
    /**
     * Retorna una lista con los archivos en el directorio de descarga
     */
    public String[] listarFicheros(){
        //carga los archivos en el servidor
        //auxArchivosLocales();
        //retorna la lista de archivos del servidor local
        //return ta.retorListArchServ(infoRed.miIp());
        return this.lFicheros();
    }
    
    
    
      /**
     * Retorna el arreglo de string con los archivos asociado a un servidor
     */
    public String[] retorListArchServ(String ip){
      return ta.retorListArchServ(ip);
    }
    
    
    /**
     * Carga en la tabla la lista de archivos locales
     */
    private void auxArchivosLocales(){
       String [] aLocales;
       aLocales = this.lFicheros();
       
       ta.remplazarArchivos(infoRed.miIp(),infoRed.miHost(), aLocales);
    }
    
    
    
    /**
     * Carga en la tabla la lista de archivos locales
     */
    public void cargaArchivosLocales(){
       auxArchivosLocales();
    }
    
    
    
     /**
     Crea el directorio donde se descargaran los archivos
     */
    private void crearDirDescarga(){
        File f = new File(this.directorioDes);
        if(f.exists() ){
            if(!f.isDirectory()){
               f.mkdir();
               System.out.println("Directorio de descarga "+
                       this.directorioDes+" creado.");
            }
        }
        else{
           f.mkdir();
            System.out.println("Directorio de descarga "+
                       this.directorioDes+" creado.");
        }
    }
    
    
    /*Retorna si existe un arreglo de byte del archivo
      Busca el archivo en el directorio de descarga
     */
    public byte[] solicitarFichero(String nombre) throws java.rmi.RemoteException{
        File f = new File(directorioDes+"/"+nombre);
        if(f.exists() && f.isFile() ){// si existe el fichero
            byte[] buffer = new byte[(int)f.length()];
            
                BufferedInputStream input;
            try {
                input = new BufferedInputStream(new FileInputStream(f));
                try {
                    input.read(buffer,0,buffer.length);
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(manejoServArch.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            } catch (FileNotFoundException ex) {
                Logger.getLogger(manejoServArch.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                return buffer;//retorna el buffer del fichero
        }
        return null;
    }
    

    
    public boolean recibirFichero(byte [] buffer, String nombre){
         if(buffer != null){
            File file = new File(this.directorioDes+"/"+nombre);

                BufferedOutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(manejoServArch.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                System.out.println("Creando archivo "+nombre);
                output.write(buffer,0,buffer.length);
                output.flush();
                output.close();
                System.out.println("Creado archivo "+nombre);
                //Agrega a la lista de archivos el archivo creado
                ta.agregarArchivo(infoRed.miIp(),infoRed.miHost(), nombre);
                return true;
            } catch (IOException ex) {
                Logger.getLogger(manejoServArch.class.getName()).log(Level.SEVERE, null, ex);
                
            }

         }
         else{
             System.out.println("No se creo el archivo "+nombre+ ". buffer vacio");
             
         }
         return false;
    
    }
    
    
    
    
    
    //Elimina el archivo de nombre nombre en la carpeta de descarga
    public boolean eliminarArchivo(String nombre){
     File f = new File(this.directorioDes+"/"+nombre);
        if(f.exists() && f.isFile() ){
           boolean elim = f.delete();
           if(elim){
              System.out.println("Archivo "+ nombre+" eliminado.");
              ta.eliminarArchivo(infoRed.miIp(), nombre);
              return true;
           }
           else{
               System.out.println("Archivo "+ nombre+" no eliminado.");
               return false;
           }
        }
        else{
            System.out.println("Archivo "+ nombre+" no existe.");
        }
       return false;
    }
    
    
    
    /**
     * Agrega a la tabla de archivos del servidor ip el archivo archivo
     */
    public boolean agregarArchivoServidor(String ip,String nombre,String archivo){
       return ta.agregarArchivo(ip,nombre, archivo);
    }
    
    
    /**
     * Remplaza la lista de archivos de un servidor (ip) por archivos
     */
    public boolean agregarArchivosServidor(String ip,String nombre,String[] archivos){
       return ta.remplazarArchivos(ip, nombre,archivos);
    }
    
    
    
    
    /**Retorna una lista de string con las ip de los servidores registrados*/
    public String[] listarServidores(){
        return ta.listarServidores();
    }
    
    
    /*Elimina las todas las entradas de la tabla*/
    public boolean limpiarTabla(){
        ta.limpiarTabla();
        this.cargaArchivosLocales();
        return true;
        
    }
    
    
    /**
     * Retorna una lista con los nombres de los servidores registrados
     */
    public String[] nombresServidores(){
       return ta.nombresServidores();
    }
    
    
     /**Usa un criterio de seleccion del servidor*/
    public String[] solicitarServidor(int i){
      return ta.solicitarServidor(i)  ;
    }
    
    
    public void eliminarServidor(String ip){
       ta.eliminarServidor(ip);
    }
    
     
    /**Retorna el numero de servidores registrados*/
    public int numServidores(){
       return ta.numServidores();
    }

    /*
     * Sustituye el contenido del archivo por el mensaje
     */
    public static void reempContent(File arch, String mens){
        try{

            FileWriter fw = new FileWriter(arch);
            fw.write(mens);
            fw.close();
        } catch (Exception e){
            System.out.println("Error reemplazando el contenido del "+
                    "archivo "+arch.getName());
        }

    }

    public boolean verifErrorArch(String nArch){
        File tempFile = new File(this.directorioDes+"/"+nArch);
        if (!tempFile.exists()){
            System.out.println("verifErrorArch: El archivo no existe");
            return false;
        } else {
            try {
            FileReader fr = new FileReader(tempFile);
            BufferedReader br = new BufferedReader(fr);
            String primeraLinea = br.readLine();
            fr.close();
            if (primeraLinea.equals("Error")){
                System.out.println("Ocurrio un error durante la ejecucion"
                        + " del archivo "+nArch+".class");
                return false;
            }

            return true;
            } catch (Exception e){

                return false;
            }
        }
    }
    
    
    
    
    /**Solicita al servidor rmi la lista de directorios**/
    public String[] solicitarArchivosAServidor(String ip){
            interfazServicioRmi sr;
            try{
            sr = (interfazServicioRmi)
            Naming.lookup( "rmi://"+ip+":"+Config.puerto+"/Servicio");
            return sr.listarFicheros();
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
            return null;
    
    }
    
    
    

    public String[] mostrarTodosArchivos(){
        return this.todosArchivos();

        //int tam = todos.length;

       // sejuelaluzinterfaz.SeJueLaLuzInterfazView.limpiarListaArchivos();
        //for(int i=0;i<tam;i++){
           // String arch = todos[i];
            //sejuelaluzinterfaz.SeJueLaLuzInterfazView.agregarArchivo(arch);
        //}
    }
    
    
}
