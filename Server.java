/*
Intermediate Java Tutorial
Project Tutorial : Instant Messenger
Server class
*/

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	
	private JTextField userText; //area where the users input their texts
	private JTextArea chatWindow; //window to display conversations
	private ObjectOutputStream output; //output stream goes of of the sender computer to the recipient
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//the constructor
	public Server(){
		//building the GUI
		super("AEON Instant Messenger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		chatWindow.setBackground(Color.GREEN);
		add(new JScrollPane(chatWindow));
		setSize(300, 500);
		setVisible(true);
	}
	
	public void startRunning(){
		try{
			server = new ServerSocket(6789, 100);
			while(true){
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\n Server ended the connection!");
				}finally{
					closeProgram();
				}
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		showMessage(" Waiting for someone to connect... \n");
		connection = server.accept(); //once detect a connection, accept
		showMessage(" Now connected to "+ connection.getInetAddress().getHostName());
	}
	
	//set up stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush(); //to clean the data pipe (sort of)
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup! \n");
	}
	
	//to handle things during the chatting
	private void whileChatting() throws IOException{
		String message = "You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n"+ message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n ???");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	//close everything (streams and sockets) after chatting is done
	private void closeProgram(){
		showMessage("\n Closing connection... \n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//send a message to the client
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}catch(IOException ioException){
			chatWindow.append("\n ERROR: message is not sent");
		}
	}
	
	private void showMessage(final String text){
		// to update the GUI (chat window) instead rebuilding many times
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}
			}
		);
	}
	
	//let the user type text into the chat box
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(tof);
				}
			}
		);
	}
}