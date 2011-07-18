/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructuras;

/**
 *
 * @author jose
 */
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
import sun.security.provider.PolicyParser;
import sun.security.provider.PolicyFile;
import java.security.*;
import java.net.URL;

import java.util.Date;

public class ActualizarF {
    
    String directorioDescarga = Config.dirDes;
    final int puerto = Config.puerto;
    
    interfazServicioRmi sr;

    public void actualizarArchivo(String ipserv,String nombreArch){

        try {
           sr = (interfazServicioRmi)
                Naming.lookup( "rmi://"+ipserv+":"+puerto+"/Servicio");

           byte[] content = sr.solicitarFichero(nombreArch);
           manejoServArch manejArch = new manejoServArch(directorioDescarga);
           manejArch.recibirFichero(content, nombreArch);

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

    public boolean verifArch(String nombreArch,Long timeArchServ){
        File flocal =  new File(directorioDescarga+"/"+nombreArch);
        if (flocal.exists()){
            if (flocal.lastModified()<timeArchServ){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void main(String[] args){
        /*PolicyParser p = new PolicyParser();
        try {
        URL url = new URL("file://home/jose/pjava/prueba/ppolicy2");
        PolicyFile p2 = new PolicyFile(url);
        ProtectionDomain pd = new ProtectionDomain(null, null);
        PermissionCollection permissions = p2.getPermissions(pd);
        System.out.println(permissions.toString());
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }*/

        try{
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        //Class c = cl.loadClass("");
        Class c2 = Class.forName("Prueba.p");
        Policy pol = Policy.getPolicy();
        ProtectionDomain pd = c2.getProtectionDomain();
        PermissionCollection permissions = pol.getPermissions(pd);
        java.util.Enumeration<Permission> e = permissions.elements();
        //Cambio de prueba


        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
