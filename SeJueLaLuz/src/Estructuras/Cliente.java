

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
        private String ip = "192.168.1.193";
        
        
        //Para manejo de los servidores
        private manejoServArch ma;
        
        
        String directorioDescarga = Config.dirDes;
        final int puerto = Config.puerto;
       
        //Instancia a servidor
        interfazServicioRmi sr;
    /**
     * @param args the command line arguments
     */
    public Cliente() {
        //Crea directorio de descarga
        this.crearDirDescarga();
    }
    
    
    
    public void iniciar(){
        try {
                //Se afilia al multicas
                Multicast mul = new Multicast();
                mul.setManjadorServArch(ma);
                
            
                //Pregunta por servidores vivos.

               
                sr = (interfazServicioRmi)
                Naming.lookup( "rmi://"+ip+":"+puerto+"/Servicio");                
                //lista de ficheros en el servidor
                this.listarArchivos(sr);

                //para preguntar cuáles servidores están activos
                this.servidoresActivos();
                
                //Detener proceso en servidor
                //sr.terminarEjecucion("p.class");
                
                //ejecutar archivo en el servidor
               // this.ejecutar(sr,"p2.class");
                
                //Si esta ocupado se agrega ejecución
                
                //Subir archivo
                //this.enviarArchivo("manifest.mf");

                //descargar archivo
                //this.solicitarArchivo("pServidor.class");

                
                
                
                //sr.tamanoEjecucion();
                 
                
                
                 
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
    }
    

    
    
    
    
    
    /**
     * Metodo para solicitar archivo al servidor desde el cliente
    */
    public void solicitarArchivo(String nombre ) {
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
    public boolean enviarArchivo(String nombre){
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
    public void ejecutar(interfazServicioRmi sr,String nombre){
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
                    byte[] arch = sr.ejecutar(buffer, nombre);
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
                 multi.enviarMensaje("Activo?");
                 multi.run();
    }
}
