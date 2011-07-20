/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RMI;

/**
 *
 * @author Luis Escorche
 */
import java.io.File;
import java.io.FileWriter;

public class seguridad {
    
    private static String pathSecurityDir = "../seguridad";
    private static String securitypolicy = "ppolicy";

    public static String getSecurityDir(){
        return pathSecurityDir;
    }

    public static String getPathPolicy(){
        return pathSecurityDir+"/"+securitypolicy;
    }

    public void createSecurityDir(){

        File dir = new File(pathSecurityDir);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    public void createPolicyFile() {
        String content =
                "grant codeBase \"file://../D/\" {\n"+
                "\tpermission java.io.FilePermission \"../D-\", \"read, write, delete, execute\";\n"+
                "};\"";

        try{
            FileWriter fw = new FileWriter(
                    new File(pathSecurityDir+"/"+securitypolicy));

            fw.write(content);
            fw.close();

        } catch(Exception e){
            System.out.println("Error generando el archivo con las politicas");
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        seguridad seg = new seguridad();
        seg.createSecurityDir();
        seg.createPolicyFile();
    }

}
