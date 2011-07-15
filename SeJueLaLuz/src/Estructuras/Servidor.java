/*
 Clase creada para obtener información basica del equipo
 Ip, nombre del host.
 */
package Estructuras;

/**
 *
 * @author necross
 */

import RMI.*;
public class Servidor {

    public Servidor (){
        //Inicia el servidor rmi
        servidorServicioRmi s = new servidorServicioRmi();
        
        //Guarda una referencia a la tabla de Archivos
        manejoServArch m = s.retManejadorArchivos();
        //Se subscribe al multicast
        
        
       Multicast multi = new Multicast();
       //Se envia a multicas referencia al manejador de archivos       
         multi.setManjadorServArch(m);
       
        
        //Me quedo escuchando infinito
        multi.run();
        
    }
    
}
