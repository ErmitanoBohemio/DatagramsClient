/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author OscarFabianHP
 */
//client side of connectionless client/server computing with datagrams

public class Client extends JFrame{
    
    private JTextField enterField; //for entering messages
    private JTextArea displayArea; //for displaying messages
    private DatagramSocket socket; //socket to connect to server
    
    //set up GUI and DatagramSocket
    public Client(){
        super("Client");
        
        enterField = new JTextField("Type message here");
        enterField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    //get message from textfield
                    String message = event.getActionCommand();
                 
                    displayArea.append("\nSending packet containing: " + message + "\n");
                    
                    byte[] data = message.getBytes(); //convert to bytes
                    
                    //create sendPacket
                    DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000);
                    
                    socket.send(sendPacket); //send packet
                    displayArea.append("Packet sent\n");
                    displayArea.setCaretPosition(displayArea.getText().length());
                } 
                catch (IOException ioException) {
                    displayMessage(ioException + "\n");
                    ioException.printStackTrace();
                }
                
            }
        });
        add(enterField, BorderLayout.NORTH);
        
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        setSize(400, 300); //set window size
        setVisible(true); //show window
        
        try{ //create DatagamSocket for sending and receiving packets
            socket = new DatagramSocket();
        }
        catch(SocketException socketException){
            socketException.printStackTrace();
            System.exit(1);
        }
    }
    
    //wait for packets to arrive from Server, display packet contents
    public void waitForPackets(){
        while(true){ //uses an infinite loop to wait for packets from the server, block untill a packet arrives; this does not prevent the user from sending a packet, because the GUI events are handle in the event-dispatch thread
            try{ //receive packet and display contents
                byte[] data = new byte[100]; //set up packet
                
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                
                socket.receive(receivePacket); //wait for packet
                
                //display packet contents
                displayMessage("\nPacket received:" +
                        "\nFrom host: " + receivePacket.getAddress() +
                        "\nHost port: " + receivePacket.getPort() + 
                        "\nLength: " + receivePacket.getLength() + 
                        "\nContaining:\n\t" + new String(receivePacket.getData(), 0, receivePacket.getLength()));
            }
            catch(IOException exception){
                displayMessage(exception + "\n");
                exception.printStackTrace();
            }
        }
    }
    
    //manipulates displayArea in the event-dispatch thread
    private void displayMessage(String messageToDisplay){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //updates displayArea
                displayArea.append(messageToDisplay);
            }
        });
    }
    
}
