/*
Intermediate Java Tutorial
Project Tutorial: Instant Messenger
Testing Client app
*/

import javax.swing.JFrame;

public class ClientTest{
	public static void main(String[] args){
		Client aeon;
		aeon = new Client("127.0.0.1");
		aeon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aeon.startRunning();
	}
}