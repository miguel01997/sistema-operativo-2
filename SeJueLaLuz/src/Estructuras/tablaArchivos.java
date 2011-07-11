package Estructuras;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;



/**
 ESTA CLASE DEBE DE SER USADA UNICAMENTE POR manejoArchivo
 */

 /*
 * @author necross
 */
public class tablaArchivos {

    
    /*tabla de hash que almacena la ip y la lista de archivos de los servidores*/
    private HashMap<String, List<String>> mapaArchivos;

     
     
    //CONSTRUCTOR 
    public tablaArchivos() {
        this.mapaArchivos = new HashMap();
        //agrega el servidor local
        mapaArchivos.put(infoRed.miIp(),new LinkedList<String>());
    }
    
    /**
     * CONSTRUCTOR
     */
    public tablaArchivos(String []archivosLocales) {
        LinkedList<String> l = new LinkedList<String>();
        this.mapaArchivos = new HashMap();
        //agrega el servidor local
        if(archivosLocales != null){
           l.addAll(Arrays.asList(archivosLocales));
        }
        
        mapaArchivos.put(infoRed.miIp(),l);
    }
    
    
    
    /*
     Agrega un servidor a la tabla, si existe no lo remplaza
     */
    public synchronized  boolean agregarServidor(String ip){
       if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada
          mapaArchivos.put(infoRed.miIp(),new LinkedList<String>());
          return true;
       }
       return false;
    }
    
    
    
    /**
     * Agrega archivo a la lista de de archivos de un servidor
     */
    public synchronized  boolean agregarArchivo(String ip,String archivo){
        if(ip==null)
               return false;
        
        if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada al servidor si no existe
          mapaArchivos.put(ip,new LinkedList<String>());
          //agrega el elemento
       }
       LinkedList<String> l = (LinkedList<String>) mapaArchivos.get(ip);
       if(l!=null){
           if(!l.contains(archivo)){//Si no contiene el archivo
               l.add(archivo);//lo agrega
           }
       }
       else{//si la lista es nula
          l = (LinkedList<String>) mapaArchivos.get(ip);
          l.add(archivo);//lo agrega
          mapaArchivos.put(ip,l);
       }
       return true;
    }
    
    
    
    /**
     * Agrega archivo a la lista de de archivos de un servidor
     */
    public synchronized  boolean agregarArchivos(String ip,String[] archivos){
        
        if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada al servidor si no existe
          mapaArchivos.put(ip,new LinkedList<String>());
          //agrega el elemento
       }
       LinkedList<String> l = (LinkedList<String>) mapaArchivos.get(ip);
       if(l!=null){
           if(archivos != null){
              for(int i=0;i<archivos.length;i++){
                 if(!l.contains(archivos[i])){//Si no contiene el archivo
                    l.add(archivos[i]);//lo agrega
                 }       
              }
           }
       }
       else{//si la lista es nula
          l = (LinkedList<String>) mapaArchivos.get(ip);
          if(archivos != null){
                l.addAll(Arrays.asList(archivos));
           }
          mapaArchivos.put(ip,l);
       }
       return true;
    }
    
    
    
    /**
     * Remplaza a lista de archivos de un servidor por archivos
     */
    public synchronized boolean remplazarArchivos(String ip, String[] archivos){
       if(ip!=null && archivos !=null){
            if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada al servidor si no existe
               LinkedList<String> l =new LinkedList<String>();
               l.addAll(Arrays.asList(archivos));
               mapaArchivos.put(ip,l);
          
            }
            else{ //Solo crea una nueva lista y la remplaza
               LinkedList<String> l =new LinkedList<String>();
               l.addAll(Arrays.asList(archivos));
               mapaArchivos.put(ip,l);
            }
       }
       else{
          return false;
       }
       return true;
    }
     
    
    /**
     * Elimina un archivo de la lista de archivos asociado a un servidor
     */
    public synchronized  boolean eliminarArchivo(String ip, String archivo){
        if(ip==null)
               return false;
        
        if(!mapaArchivos.containsKey(ip)){//no consigue el servidor
           return false;
        }
        LinkedList<String> l = (LinkedList<String>) mapaArchivos.get(ip);
        if(l!=null){
           if(l.contains(archivo)){//Si  contiene el archivo
               l.remove(archivo);
               return true;
           }
           else{//No consigue el archivo
              return false;
           }
       }//No existe la lista
       return false;
    }
    
    /**
     * Retorna el arreglo de string asociado a un servidor
     */
    public String[] retorListArchServ(String ip){
       if(ip !=null){
          if(mapaArchivos.containsKey(ip)){
              LinkedList<String> l = (LinkedList<String>) mapaArchivos.get(ip);
              if(l!=null){
                  String[] r= new  String[l.size()];
                  l.toArray(r);
                  return r;
              }
              return null;
          }
          else
              return null;
       }
       else 
           return null;
    }
    
    
    
    /*Retorna una lista de string con las ip de los servidores registrados*/
    public String[] listarServidores(){
       if(mapaArchivos!=null){
           Set conjunto = mapaArchivos.keySet();
           String [] re = new String[conjunto.size()];
           Iterator t = conjunto.iterator();
           for(int i=0;t.hasNext();i++){
              re[i]=(String)t.next();
           }
           return re;
       }
       return null;
       
    }
    
    
    /*Elimina las todas las entradas de la tabla*/
    public boolean limpiarTabla(){
        mapaArchivos.clear();
        
        return true;
    }
    
    
    
    
}
