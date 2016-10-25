/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;
import org.opencv.core.Core;

/**
 *
 * @author Eivind Fugledal
 */
public class Main {
    
    protected static DataHandler dh;
    private static GUIData gd;
    private static Thread controller;
    private static Thread server;
    private static Semaphore semaphore;
    private static Thread tracker; // Testing
    static SendEventState enumStateEvent;
    protected static String ipAdress;
    private static CameraCapture camera;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        semaphore = new Semaphore(1, true);
        
        dh = new DataHandler();
        dh.setThreadStatus(true);
        
        gd = new GUIData(dh, semaphore);
        controller = new Thread(new Controller(dh, semaphore));
        server = new Thread(new UDPServer(gd));
        camera = new CameraCapture();
        tracker = new Thread(new ObjectTracker(dh,semaphore,camera));
        
        
        
        gd.start();
        controller.start();
        server.start();
        tracker.start();
        
        
        
        SerialComArduino sca = new SerialComArduino(dh);
        try {
            sca.connect("COM6", semaphore);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
