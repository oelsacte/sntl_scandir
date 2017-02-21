package com.bizpro.sntlops.main;

import watcher.FileChangeScan;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class SntlScanDir {
    
    private static final String PROP_FILE  = "./sntlops.properties";
    private final SNTLOperativeSystemProperties pep = SNTLOperativeSystemProperties.getInstance(PROP_FILE);    
    
    public void scannear()throws IOException{
        String[] directorios = pep.getFiles().split(",");
        ExecutorService executorService = Executors.newFixedThreadPool(directorios.length);
        for(int i=0;i < directorios.length; i++ ){
            FileChangeScan fileChangeWatcher = new FileChangeScan(directorios[i]);
            executorService.execute(fileChangeWatcher);
        }
    }
    
    public static void main(String[] args) throws IOException {
        SntlScanDir scan = new SntlScanDir();
        scan.scannear();
    }
 
}