package com.bizpro.sntlops.main;

import java.io.FileInputStream;
import java.util.Properties;

public class SNTLOperativeSystemProperties {
    private static SNTLOperativeSystemProperties sntl_properties = null;
    private String sntl_server;
    private int sntl_port;
    private int discovery;
    private String sntl_overflowFile;
    private int logSize;
    private int logFileNum;
    private boolean file;
    private String files;    
    
    private SNTLOperativeSystemProperties(String propFile) throws Exception{
        StringBuffer sb = null;
        if( propFile != null ) {
            sb = new StringBuffer(propFile);
        } else
            sb = new StringBuffer("./").append("sntlops.properties");
        
        System.out.println("Leyendo Properties");
        FileInputStream fis = new FileInputStream(sb.toString());
        Properties props = new Properties();
        props.load(fis);
        this.setSntl_server(props.getProperty("sntl_server"));
        this.setSntl_port(Integer.parseInt(props.getProperty("sntl_port")));
        this.setDiscovery(Integer.parseInt(props.getProperty("discovery")));
        this.setSntl_overflowFile(props.getProperty("overflowFile"));
        this.setLogSize(Integer.parseInt(props.getProperty("logSize")));
        this.setLogFileNum(Integer.parseInt(props.getProperty("logFileNum")));
        this.setFile(Boolean.valueOf(props.getProperty("file-info")));
        this.setFiles(props.getProperty("files"));
        
    }
    
    public static SNTLOperativeSystemProperties getInstance(String propFile) 
    {
        if(sntl_properties == null)
        {
            try
            {
                sntl_properties = new SNTLOperativeSystemProperties(propFile);
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        return sntl_properties;
    }

    public static SNTLOperativeSystemProperties getSntl_properties() {
        return sntl_properties;
    }

    public static void setSntl_properties(SNTLOperativeSystemProperties sntl_properties) {
        SNTLOperativeSystemProperties.sntl_properties = sntl_properties;
    }

    public String getSntl_server() {
        return sntl_server;
    }

    public void setSntl_server(String sntl_server) {
        this.sntl_server = sntl_server;
    }

    public int getSntl_port() {
        return sntl_port;
    }

    public void setSntl_port(int sntl_port) {
        this.sntl_port = sntl_port;
    }

    public int getDiscovery() {
        return discovery;
    }

    public void setDiscovery(int discovery) {
        this.discovery = discovery;
    }

    public String getSntl_overflowFile() {
        return sntl_overflowFile;
    }

    public void setSntl_overflowFile(String sntl_overflowFile) {
        this.sntl_overflowFile = sntl_overflowFile;
    }

    public int getLogSize() {
        return logSize;
    }

    public void setLogSize(int logSize) {
        this.logSize = logSize;
    }

    public int getLogFileNum() {
        return logFileNum;
    }

    public void setLogFileNum(int logFileNum) {
        this.logFileNum = logFileNum;
    }
    
    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
}