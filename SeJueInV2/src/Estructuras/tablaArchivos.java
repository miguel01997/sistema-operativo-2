package Estructuras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.sql.rowset.spi.SyncResolver;



/**
 ESTA CLASE DEBE DE SER USADA UNICAMENTE POR manejoArchivo
 */

 /*
 * @author necross
 */
public class tablaArchivos {

    
    /*tabla de hash que almacena la ip y la lista de archivos de los servidores*/
    private HashMap<String, List<String>> mapaArchivos;

    public HashMap<String, String> mapaServidores;
    
    /**Tabla cuya clave es el archivo y el valor son los servidores que lo tienen*/
    private HashMap<String, ArrayList<String>> mapaArcSer;
     
    //CONSTRUCTOR 
    public tablaArchivos() {
        this.mapaArchivos = new HashMap();
        this.mapaServidores = new HashMap();
        //agrega el servidor local
        mapaArchivos.put(infoRed.miIp(),new LinkedList<String>());
        
        mapaArcSer = new HashMap<String, ArrayList<String>>();
        
        //COMENTAR LINEA DE ABAJO CUANDO FUNCIONE PROTOCOLO ACTIVO
       // mapaServidores.put(infoRed.miIp(),infoRed.miHost());
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
    
    
    /**Retorna una lista de todos los archivos en los servidores*/
    public String[] todosArchivos(){
        retTablaSerArch();
       // System.out.println(retTablaSerArch());
        
//       / System.out.println("ARCHIVOS\n"+retTablaSerArch());
       Set<String> valores = mapaArcSer.keySet();
       String[] val = new String[valores.size()];
       valores.toArray(val);
       return val;  
    }
    
    
    
    
     /**Retorna la tabla que contiene la lista de archivos */
    private HashMap<String,ArrayList<String>> retTablaSerArch(){
       HashMap<String, ArrayList<String>> archServ ;
        archServ  = new HashMap<String, ArrayList<String>>();
        
        //Obtiene los valores
        Set val = mapaArchivos.keySet();
        //System.out.println("ARCHIVOS>>>\n"+mapaArchivos);
        Iterator valores = val.iterator();
        while(valores.hasNext()){
           //ip servidor 
           String serv = (String)valores.next();
           
           //Lista de archivos
           List l = mapaArchivos.get(serv);
           Iterator lis = l.iterator();
           while(lis.hasNext()){//itera sobre lista de arhcivos
               String arch = (String)lis.next();
              if(!archServ.containsKey(arch)){
                  ArrayList<String> laux = new ArrayList<String>();
                  laux.add(serv);
                  archServ.put(arch, laux);
              }
              else{//Si ya existe la lista solo lo agrega
                  archServ.get(arch).add(serv);
              }
           }
           
        }
        mapaArcSer = archServ;
        return archServ;
    }
    
    
    /**Busca el archivo y retorna la ip del servidor*/
    public String busArch(String nArchivo){
       this.retTablaSerArch();//crea la tabla de archivo - servidor
       ArrayList<String> serv = mapaArcSer.get(nArchivo);
       return serv.get(0);
    }
    
    
    
    /*
     Agrega un servidor a la tabla, si existe no lo remplaza
     */
    public synchronized  boolean agregarServidor(String ip,String nombre){
       if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada
          mapaArchivos.put(ip,new LinkedList<String>());
          mapaServidores.put(ip,nombre);
          return true;
       }
       return false;
    }
    
    /**Elimina un servidor y la lista de archivos asociado al mismo*/
    public synchronized  void eliminarServidor(String ip){
       //elimina la tabla de archivos asociada al servidor
        if(mapaArchivos.containsKey(ip)){
           //elimina la entrada
          mapaArchivos.remove(ip);
          
       }//Elimina la entrada en el mapa deservidores
       if(mapaServidores.containsKey(ip)){
           //elimina la entrada
          mapaServidores.remove(ip);
       }
        
    }
    
    
    /**
     * Agrega archivo a la lista de de archivos de un servidor
     */
    public synchronized  boolean agregarArchivo(String ip,String nombre,
            String archivo){
        if(ip==null)
               return false;
        
        if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada al servidor si no existe
          mapaArchivos.put(ip,new LinkedList<String>());
          mapaServidores.put(ip,nombre);
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
    public synchronized  boolean agregarArchivos(String ip,String nombre,
            String[] archivos){
        
        if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada al servidor si no existe
          mapaArchivos.put(ip,new LinkedList<String>());
          //agrega el elemento
          mapaServidores.put(ip,nombre);
          
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
    public synchronized  boolean remplazarArchivos(String ip, String nombre,
            String[] archivos){
       if(ip!=null ){
            System.out.println();           
            if(!mapaArchivos.containsKey(ip)){
           //crea la nueva entrada al servidor si no existe
                //System.out.println("Agregado servidore "+ip);
               LinkedList<String> l =new LinkedList<String>();
               if(archivos != null){
                 l.addAll(Arrays.asList(archivos));
               }
               mapaArchivos.put(ip,l);
               mapaServidores.put(ip,nombre);
          
            }
            else{ //Solo crea una nueva lista y la remplaza
               LinkedList<String> l =new LinkedList<String>();
               if(archivos != null){
                 l.addAll(Arrays.asList(archivos));
               }
               mapaArchivos.put(ip,l);
               mapaServidores.put(ip,nombre);
            }
       }
       else{
          return false;
       }
       //System.out.println("Agrega servidor "+ip);  
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
     * Retorna el arreglo de string de archivos asociado a un servidor
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
    
    
    
    
    /**Retorna una lista de string con las ip de los servidores registrados*/
    public String[] listarServidores(){
       if(mapaArchivos!=null){
           Set conjunto = mapaServidores.keySet();
           String [] re = new String[conjunto.size()];
           Iterator t = conjunto.iterator();
           for(int i=0;t.hasNext();i++){
              re[i]=(String)t.next();
           }
           return re;
       }
       return null;
       
    }
    
    /**Retorna una lista con los nombres de los servidores registrados*/
    public String[] nombresServidores(){
       if(mapaArchivos!=null){
           Collection nombres =   mapaServidores.values();
           String [] re = new String[nombres.size()];
                   nombres.toArray(re);
           return re;
       }
       return null;
       
    }
    
    
    
    
    
    
    
    /**Elimina las todas las entradas de la tabla*/
    public boolean limpiarTabla(){
        mapaServidores.clear();
        mapaArchivos.clear();
        return true;
    }
    
    
    
    /**Usa un criterio de seleccion del servidor
       retorna i servidores con ese criterio
     */
    public String[] solicitarServidor(int i ){
         //Criterio 
        //return serviSeguidos(i);
        
        //Criterio
        return criRandom(i);
        //return re;
    }
    
    
    /**Selecciona un servidor random*/
    private String[] criRandom(int numEje){
       int i;
       i = mapaServidores.size();
       
       Set servidores = mapaServidores.keySet();
       String[] serviString  = new String[i];
       servidores.toArray(serviString);
       if(numEje>i)
           return null;
       
       ArrayList<Integer> arr = new ArrayList<Integer>();
       String[] servidos = new String[numEje];
       
       
       //System.out.println("Tam "+i);
       int r;
       int numSer = 0;
       while(numSer<numEje){
         r =   (int) (Math.random()*(i));
         //System.out.println(numSer+" "+numEje+"  "+"Num >>"+r+ "   "+i);
         if(!arr.contains(r)){
             arr.add(r); 
             servidos[numSer] = serviString[numSer];
             numSer++;
         }
         
       }
       return servidos;
       
       
    }
    
    /**Busca servidores seguidos*/
    private String[] serviSeguidos(int numServi){
       int i;
         Set servidores = mapaServidores.keySet();
         Iterator iter = servidores.iterator();
         
         i = mapaServidores.size();
         //System.out.println(i+" MAPASERVER>>" +mapaServidores);
         if(numServi >i)
             return null;
         String[] servi = new String[numServi];
         int indi = 0;
         while(iter.hasNext()){
            String aux = (String)iter.next();
             servi[indi] = aux;
             //System.out.println("AAAA>>>" + aux);
             indi++;
             if(indi >= numServi)
                 break;
         }
         
       return servi;
    }
    
    
    
    
    /**Retorna el numero de servidores registrados*/
    public int numServidores(){
       // System.out.println(mapaServidores);
        return mapaServidores.size();
    }
    
    
}
