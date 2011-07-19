/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RMI;

/**
 *
 * @author necross
 */
public interface interfazServicioRmi extends java.rmi.Remote{
    
    /**
     * Envia el fichero de nombre nombre al cliente que solicita el serivicio
     */
    public byte [] solicitarFichero(String nombre) 
            throws java.rmi.RemoteException;
    
    /**
     * Orden para guardar un fichero de nombre nombre en el sevidor 
     */
    public boolean recibirFichero(byte [] fichero, String nombre) 
            throws java.rmi.RemoteException;
    
    /**Lista los ficheros almacenados en el servidor*/
    public String[] listarFicheros() 
            throws java.rmi.RemoteException;
    
    /**Ejecuta el fichero en el servidor
     recibe el fichero, el nombre del fichero y la ip asociada al cliente
     */
    public byte[] ejecutar(byte[] fichero, String nombre,String ipCliente,
                           int numTransaccion)
            throws java.rmi.RemoteException;
    
    
    /**Retorna true si esta ocupado y false en caso contrario*/
    public boolean ocupado()
            throws java.rmi.RemoteException;
    
    
    /*Imprime en el servidor cuantos elementos hay en la cola de ejecucion**/
    public void tamanoEjecucion()
            throws java.rmi.RemoteException;
    
    //Termina ejecucion de nombre nombre  asociado a la ip del cliente
    public void terminarEjecucion(String nombre,String ipCliente,int numTransa)
            throws java.rmi.RemoteException;
    
    public String ip()
            throws java.rmi.RemoteException;
    
    
   // public void agregarEjecucion(String nombre)
   //                  throws java.rmi.RemoteException;
    
    
    
   

}
