/*
 * SeJueLaLuzInterfazView.java
 */

package sejuelaluzinterfaz;

import Estructuras.Cliente;
import Estructuras.Mensajes;
import Estructuras.Multicast;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

// importadas para la parte de browse
import javax.swing.JFileChooser; //Permite elegir un archivo por dialogo
import javax.swing.filechooser.FileNameExtensionFilter; // Filtro de archivos
import java.io.File;

/**
 * The application's main frame.
 */
public class SeJueLaLuzInterfazView extends FrameView {

    //Para los archivos en ejecucion
    DefaultListModel modelo = new DefaultListModel();
    
    //Para los archivos en los servidores
    static DefaultListModel modeArchivos = new DefaultListModel();
   
    //Para el log
    static DefaultListModel modeLog = new DefaultListModel();
    
    //javax.swing.JList listaEjecucion2 = new javax.swing.JList();
    
    private Cliente cliente;
    
    
    public SeJueLaLuzInterfazView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        //Inicia el cliente 
        
        cliente = new Cliente();
        cliente.iniciar();
        
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SeJueLaLuzInterfazApp.getApplication().getMainFrame();
            aboutBox = new SeJueLaLuzInterfazAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SeJueLaLuzInterfazApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaEjecucion = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaDescarga = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        descargar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        urlArchivo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        browse = new javax.swing.JButton();
        textoEscondido = new javax.swing.JTextPane();
        textoEscondido.setVisible(false);

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sejuelaluzinterfaz.SeJueLaLuzInterfazApp.class).getContext().getResourceMap(SeJueLaLuzInterfazView.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        //static DefaultListModel modelo = new DefaultListModel();
        listaEjecucion.setModel(modelo);
        //listaEjecucion2 = listaEjecucion;

        //modelo.addElement("un item");   // para añadir un item a la lista

        /*
        listaEjecucion.setModel(new javax.swing.AbstractListModel() {

            private ArrayList<String> datos = new ArrayList<String>();

            //@Override
            public Object getElementAt(int index) {
                if(index >= 0 & index < datos.size())
                return datos.get(index);
                return null;
            }

            //@Override
            public int getSize() {
                return datos.size();
            }

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        */
        listaEjecucion.setName("listaEjecucion"); // NOI18N
        jScrollPane1.setViewportView(listaEjecucion);

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        /*
        listaDescarga.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        */
        listaDescarga.setModel(modeArchivos);
        listaDescarga.setName("archivosRemotos"); // NOI18N
        jScrollPane2.setViewportView(listaDescarga);

        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        descargar.setText(resourceMap.getString("descargar.text")); // NOI18N
        descargar.setName("descargar"); // NOI18N
        descargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descargarActionPerformed(evt);
            }
        });

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        /*
        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        */
        //modeLog
        jList2.setModel(modeLog);
        jList2.setName("log"); // NOI18N
        jScrollPane3.setViewportView(jList2);

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(descargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jLabel1)
                        .addGap(100, 100, 100)
                        .addComponent(jLabel2)
                        .addGap(121, 121, 121)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                            .addComponent(descargar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sejuelaluzinterfaz.SeJueLaLuzInterfazApp.class).getContext().getActionMap(SeJueLaLuzInterfazView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        urlArchivo.setText(resourceMap.getString("urlArchivo.text")); // NOI18N
        urlArchivo.setName("urlArchivo"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        browse.setText(resourceMap.getString("browse.text")); // NOI18N
        browse.setName("browse"); // NOI18N
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        textoEscondido.setBorder(null);
        textoEscondido.setFont(resourceMap.getFont("textoEscondido.font")); // NOI18N
        textoEscondido.setForeground(resourceMap.getColor("textoEscondido.foreground")); // NOI18N
        textoEscondido.setText(resourceMap.getString("textoEscondido.text")); // NOI18N
        textoEscondido.setName("textoEscondido"); // NOI18N
        textoEscondido.setOpaque(false);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statusMessageLabel)
                        .addGap(180, 180, 180))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(30, 30, 30)))
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textoEscondido, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(urlArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(browse, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusAnimationLabel)
                        .addContainerGap())))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(urlArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(browse, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(statusMessageLabel)
                        .addComponent(statusAnimationLabel)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textoEscondido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
 
    static int p = 0;
    //
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String urlArch = urlArchivo.getText();
        String[] re = buscarUrlClass(urlArch);
        String url = re[0];
        String clase = re[1];
        int transa = cliente.retTransa();
        agregarColaEjecucion(clase, transa); 
//        agregarColaEjecucion(clase, transa); 
        
        
        //System.out.println("Agregado "+clase+transa);
        ejecucionHilo eje = new ejecucionHilo(cliente, url, clase,this,transa);
        Thread th = new Thread(eje);
        th.start();
       // cliente.ejecutarEnServidores(url, clase);
       
        listaEjecucion.repaint();
        listaEjecucion.revalidate();
        
    }//GEN-LAST:event_jButton2ActionPerformed
//Elimina de la lista de ejecucion
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            int posicion = ejeSelecListaEje();
            
           if(posicion != -1){
              String ejecucion = (String)modelo.get(posicion);
              String[] eje = ejecucion.split("        ");
              //System.out.println(eje[0]+" >>> "+eje[1]);
              cliente.detenerEjecucion(eje[0], Integer.valueOf(eje[1]));
              modelo.remove(posicion);
           }
           
    }//GEN-LAST:event_jButton1ActionPerformed

    private void descargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descargarActionPerformed
      int indi = ejeSelecDescarga();
      if(indi!=-1){
          String nArchivo = (String)modeArchivos.get(indi);
          //System.out.println("Rastrear "+nArchivo);
          cliente.rastrearArchivos(nArchivo);
          
      }
    }//GEN-LAST:event_descargarActionPerformed

    /** Permite hacer "Browse" del .class que se quiere mandar
     * a los servidores
     */
    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos class de java","class");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Selecciona un .class ...");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){

            File file = fileChooser.getSelectedFile();
            if (String.valueOf(file).endsWith(".class")){
                textoEscondido.setVisible(false);
                urlArchivo.setText(String.valueOf(file));
            }else{
                textoEscondido.setVisible(true);
                urlArchivo.setText("");
            }
        }
    }//GEN-LAST:event_browseActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        Multicast m = new  Multicast(false);
        m.enviarMensaje("activo?");
        
    }//GEN-LAST:event_jButton3ActionPerformed

    
    
    private  void actualizarListaArchivosDeDescarga(){
       modeArchivos.clear();
         String[] archivos = cliente.listarTodosArchivos();
        if(archivos!=null){
           for(int i = 0;i<archivos.length;i++){
               agregarArchivo(archivos[i]);
           }
        }
    }
    
    public static void  p(String nArchivo){
      agregarArchivo(nArchivo);
    }
    
    public static void limpiarArchivosDescarga(){
       modeArchivos.clear();
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browse;
    private javax.swing.JButton descargar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listaDescarga;
    private javax.swing.JList listaEjecucion;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextPane textoEscondido;
    private javax.swing.JTextField urlArchivo;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    
        
    
    /*************************************************************************?
     * PARA AGREGAR LOS METODOS QUE MODIFICAN LOS WIDGETS EN PANTALLA
     */
    
    /**Para agregar una ejecucion a la lista de ejecucion**/
    public void agregarColaEjecucion(String nClass, int numTransa){
        
        modelo.addElement(nClass+"        "+numTransa);
        
        listaEjecucion.revalidate();
        listaEjecucion.repaint();
        //mainPanel.revalidate();
        //mainPanel.repaint();
        
        
    }
    
    /**Para agregar una ejecucion a la lista de ejecucion**/
     public static void agregarArchivo(String nArch){
       modeArchivos.addElement(nArch);
       
    }
     /*
      * Elimina toda la lista de archivos de los servidores en el log
      */
     public static void limpiarListaArchivos(){
         modeArchivos.removeAllElements();
     }
     
     
     
     //public void  agregarNombreArchivo(){
      //   mode
    // }
    
        
    /**Para agregar un mensaje al log  **/
     public static void agregarLog(String msj){
       modeLog.addElement(msj);
    }
    
    
    /**Para ubicar la posicion de la ejecucion seleccionada*/
    MouseListener mouseListener = new MouseAdapter() 
{
        @Override
    public void mouseClicked(MouseEvent e) 
    {
        if (e.getClickCount() == 2) // Se mira si es doble click
        {
            int posicion = listaEjecucion.locationToIndex(e.getPoint());
           // System.out.println("La posicion es " + posicion);
         }
    }
};
  

/**Retorna el indice de la posicion de la clase en ejecucion seleciconada
 * retorna -1 si no consigue seleccion
 **/
 public int ejeSelecListaEje(){
  return listaEjecucion.getSelectedIndex();

 }
 
 public int ejeSelecArchivo(){
  return listaEjecucion.getSelectedIndex();

 }
 
 
 
 public int ejeSelecDescarga(){
  return  listaDescarga.getSelectedIndex();
 }
 
 
/**Retorna la url y el nombre de la clase que va a ejecutar*/
 public String [] buscarUrlClass(String url ){
     String [] re = new String[2];
     int i = url.lastIndexOf('/');
     re[0] = url.substring(0,i+1);
     //System.out.println("URL >"+re[0]);
     re[1] = url.substring(i+1,url.length());
     //System.out.println("CLASS >"+re[1]);
     return re;
 }
 
 /**Busca en la ejecucion la ejecucion de nombre nombre y num de transa y
  retorna el indice donde se encuentra
  */
 public int buscarIndiEjec(String nClass,int numTransa){
     int indi = modelo.indexOf(nClass+"        "+numTransa);
     return indi;
 }
 
 
 
 /**Busca el indice del archivo dentro de la lista de descarga**/
 public int buscarIndiDescarga(String nArchivo){
     int indi = modelo.indexOf(nArchivo);
     return indi;
 }
 
 
 
 
 /**Elimina de la lista de ejecucion */
 public void elimListaEje(String nClase,int numTransa){
  //System.out.println(nClase+" >>> "+numTransa);
     int indi = buscarIndiEjec(nClase, numTransa);
    if(indi!=-1){
       modelo.remove(indi);
    }
 }
    
 

//.addMouseListener(mouseListener);
    
    

 class ejecucionHilo implements Runnable{
      Cliente cli;
      
      String nClase;
      
      String url;
      
      SeJueLaLuzInterfazView ss;
      
      int numTransa;
     
      DefaultListModel ejecu;
      public ejecucionHilo(Cliente cli,String url ,String nClase,SeJueLaLuzInterfazView s,
                            int nt){
         this.cli = cli;
         this.nClase = nClase;
         this.url = url;
         this.ss = s;
         this.numTransa = nt;
      }
      
     
      
      
        public void run() {
            cliente.ejecutarEnServidores(this.url,this.nClase);
            //cuando termina elimina la instancia de los archivos
            ss.elimListaEje(nClase, numTransa);
            
        }
 
     
 
 }
    
}
