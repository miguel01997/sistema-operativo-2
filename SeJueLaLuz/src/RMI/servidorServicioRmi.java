
package RMI;

/**
 *
 * @author necross
 */

import Estructuras.Config;
import Estructuras.infoRed;
import Estructuras.manejoServArch;
import java.rmi.Naming;
public class servidorServicioRmi {

    
final int puerto = Config.puerto;
//final String ip = "192.168.1.123";
final String ip = infoRed.miIp();

//Directorio de descarga y subida de archivos del servidor
final String directorio = Config.dirDes;

/*Referencia a manejador de archivo*/
private manejoServArch ma;
    /*
     *Asocia el servicio a un puerto y a una ip
     */
    public servidorServicioRmi(){
      try {
      //Puerto asociado 4295
        java.rmi.registry.LocateRegistry.createRegistry(puerto);
        interfazServicioRmi sr = new serviciosRmi(directorio);
        
        //Se guarda referencia al manejador de archivo
        ma = ((serviciosRmi)sr).retManejadorArchivos();
        
        System.out.println("Iniciando servidor...");
        Naming.rebind("rmi://"+ip+":"+puerto+"/Servicio", sr);
      }
      catch (Exception e) {System.out.println ("Problema: " + e);
      }
  
    }
    
    /*Retorna la referencia al manejador de archivo*/
    public manejoServArch retManejadorArchivos(){
       return ma;
    }
    
      
}
