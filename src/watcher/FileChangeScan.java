package watcher;

import com.bizpro.sntlops.main.SNTLGather;
import com.bizpro.sntlops.main.SNTLOperativeSystemProperties;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileChangeScan implements Runnable{

    private static final String PROP_FILE = "./sntlops.properties";
    private final SNTLOperativeSystemProperties pep = SNTLOperativeSystemProperties.getInstance(PROP_FILE);
    private String hostname;
    private String directorio;
    
    public FileChangeScan(String directorio){        
        this.directorio = directorio;
    }
    
    @Override
    public void run(){
        try {
            doWath();
        } catch (IOException ex) {
            Logger.getLogger(FileChangeScan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void doWath() throws IOException {
        InetAddress addr;
        
        try {
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SNTLGather.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Directorio Monitoreado " + directorio);
        System.out.println("HostName " + hostname);

        Path directoryToWatch = Paths.get(directorio);
        if (directoryToWatch == null) {
            throw new UnsupportedOperationException("No es un directorio o no existe");
        }

        
        WatchService watchService = directoryToWatch.getFileSystem().newWatchService();
        directoryToWatch.register(watchService, new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY});

        System.out.println("Inicia Servicio " + directorio);

        try {

            // Esperamos que algo suceda con el directorio
            WatchKey key = watchService.take();
            SNTLGather gather = new SNTLGather();

            // Algo ocurrio en el directorio para los eventos registrados
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    String eventKind = event.kind().toString();
                    String file = event.context().toString();                   
                    gather.crearOverFlowWatches(pep, hostname, directorio, file, eventKind);
                    System.out.println("Evento : " + eventKind + " archivo " + file);
                }

                key.reset();
                key = watchService.take();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
