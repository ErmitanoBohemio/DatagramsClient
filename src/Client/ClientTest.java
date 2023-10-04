/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import javax.swing.JFrame;

/**
 *
 * @author OscarFabianHP
 */
public class ClientTest {
    public static void main(String[] args) {
        Client application = new Client(); //create client
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //
        application.waitForPackets(); //run client application
    }
}
