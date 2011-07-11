
import Estructuras.manejoArchivo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author necross
 */
public class pManejoArchivos {
  
    public static void main(String argv[]){
       
    manejoArchivo mar = new manejoArchivo();
    mar.cargaArchivosLocales();
    String[] l = mar.listarFicheros();
    imp(l);
    //agregar archivos a la tabla de archivos de servidor compu1
    String [] l2 = {"d1","d3","d4","d5","d6"};
    mar.agregarArchivosServidor("compu1", l2);
    
    //Listar servidores de la tabla
    String[] aux = mar.listarServidores();
    System.out.println("Servidores:");
    imp(aux);
    
    
    System.out.println("Archivos de servidor compu1:");
    aux = mar.retorListArchServ("compu1");
    imp(aux);
    
    //limpiar tabla
    mar.limpiarTabla();
    aux = mar.listarServidores();
    System.out.println("Servidores:");
    imp(aux);
    
    
    
    }
    
    
    public static void imp(String[] ar){
    if(ar!=null){  
        for(int i=0;i<ar.length;i++){
            System.out.println(ar[i]);
        }
    }
    
    }
    
}
