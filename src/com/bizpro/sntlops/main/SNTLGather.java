package com.bizpro.sntlops.main;

import com.axway.trkapiua.TrkApiUA;
import com.axway.trkapiua.TrkMessageUAEvent;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SNTLGather extends Thread implements Serializable{
    
    /**
     * Logger de Aplicación
     */
    public static final Logger LOGGER = Logger.getLogger("sntlscandir");
    
    /**
     * String que contiene el nombre del archivo de propiedades
     */
    private static String propFile  = "./sntlops.properties";
    
    /**
     * Clase que carga al archivo de configuración o properties
     */
    private SNTLOperativeSystemProperties pep = SNTLOperativeSystemProperties.getInstance(propFile);
    
    /**
     * Variable String del nombre de host
     */
    private String hostname;
    
    /**
     * Constructor 
     * @param pep Carga configuración del programa
     * 
     */
    public SNTLGather(SNTLOperativeSystemProperties pep){
        this.pep = pep;        
    }
    
    /**
     * Constructor por defecto
     */
    public SNTLGather(){    }
    
    /**
     * Método que recibe el evento del FileChangeScan y lo reporta a Sentinel
     * @param pep Carga archivo de propiedades
     * @param hostname Nombre de Host
     * @param directory Directorio a monitorear
     * @param file Archivo que se va a reportar
     * @param event Tipo de evento, EVENT_MODIFY, EVENT_DELETED, EVENT_CREATED
     */
    public void crearOverFlowWatches(SNTLOperativeSystemProperties pep, String hostname, String directory, String file, String event){
        LOGGER.log(Level.INFO, "FILE::.Creando Over Flow ...");
        TrkApiUA trkapiua = new TrkApiUA( pep.getSntl_server(), Integer.toString(pep.getSntl_port()) );
        //trkapiua.setUATrace( 4, pep.getSntl_overflowFile() );
        trkapiua.setOverflowFile( "file_" + pep.getSntl_overflowFile(), 10 );
        //trkapiua.sendOverflowFile( pep.getSntl_overflowFile() );
        trkapiua.setLocalAddr(pep.getSntl_server());
        trkapiua.setProductName("TRKAPI_JAVA");
        TrkMessageUAEvent trkmsgevent = new TrkMessageUAEvent( trkapiua,"FILEWATCHES", "1.0" );
        trkmsgevent.setAttribute( "CycleId", "OSFI" + pep.getSntl_server() + directory + file);
        trkmsgevent.setAttribute( "APPLICATION", "TRKAPIUA" );
        trkmsgevent.setAttribute( "AgentIPAddr", trkapiua.getLocalAdress() );
        trkmsgevent.setAttribute( "hostName", hostname );
        System.out.println("OVERFLOW hostName " + hostname );
        trkmsgevent.setAttribute( "fileName", file );
        trkmsgevent.setAttribute( "filePath", directory );
        if (event.equals("ENTRY_DELETE")){
            trkmsgevent.setAttribute( "InternalState", 1001 );
        }else {
            trkmsgevent.setAttribute( "InternalState", 1002 );
        }
        
        trkmsgevent.setDateAttribute( "fileDateChanged", new java.util.Date(  ) );
        trkmsgevent.setTimeAttribute("fileTimeChanged", new java.util.Date(  )); 
        int returnCode = trkapiua.sendMessage( trkmsgevent );
        boolean overFlowSended = trkapiua.sendOverflowFile("file_" + pep.getSntl_overflowFile() );
        LOGGER.log(Level.INFO, "FILE::.Over Flow Sended {0}", overFlowSended);
        LOGGER.log(Level.INFO, "FILE::.Return Code {0}", returnCode);
        
        trkapiua.stopUA();
        
        if( returnCode == 0 ){
            java.io.File fileFlow = new File("file_" + pep.getSntl_overflowFile());
            if(fileFlow.delete()){
                LOGGER.log(Level.INFO, "FILE::.Borrado overflow forzado.");
            }else{
                LOGGER.log(Level.INFO, "FILE::.No se pudo forzar borrado overflow.");
            }
        }
        LOGGER.log(Level.INFO, "FILE::.#Fin Over Flow.");
    }
    
}