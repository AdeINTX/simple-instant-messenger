/*
Intermediate Java Tutorial
Project Tutorial : Instant Messenger
Testing Server
*/

import javax.swing.JFrame;

public class ServerTest{
	public static void main(String[] args){
		Server aeon = new Server();
		aeon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aeon.startRunning();
	}
}