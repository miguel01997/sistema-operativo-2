/**
 * Clase que controla la lista de archivos en el servidor local
 * y almacena informacion de los demas archivos en los otros servidores
 */

package Estructuras;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author necross
 */
public class manejoServArch {

    //Directorio de descarga de los archivos;
    private String directorioDes = Config.dirDes;
    
    /**Tabla que maneja los archivos y sevidores*/
    private tablaArchivos ta;

    
    
    
    
    
    
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
       
       //ta.remplazarArchivos(infoRed.miIp(),infoRed.miHost(), aLocales);
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
                output.write(buffer,0,buffer.length);
                output.flush();
                output.close();
                System.out.println("Archivo "+nombre+" Creado");
                //Agrega a la lista de archivos el archivo creado
                ta.agregarArchivo(infoRed.miIp(),infoRed.miHost(), nombre);
                return true;
            } catch (IOException ex) {
                Logger.getLogger(manejoServArch.class.getName()).log(Level.SEVERE, null, ex);
            }

         }
         else{
             System.out.println("No se creo el archivo "+nombre);
             
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
    
    
    
    
}
