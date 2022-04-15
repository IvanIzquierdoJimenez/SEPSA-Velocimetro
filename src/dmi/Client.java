package dmi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import dmi.*;

public class Client {
	Socket s;
	DataOutputStream out;
	BufferedReader in;
	Archivo arc = new Archivo();
	public Client()
	{
		String[] Conf = arc.ReadConfig("../SEPSA-Velocimetro/config.txt");
		while(s==null)
		{
			try
			{
				s = new Socket(Conf[2], 5090); //Cambia el puerto
			}
			catch (IOException e)
			{
			}
		}
		while(!s.isConnected()) 
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void sendData(String s)
	{
		s = s+'\n';
		try {
			out.writeBytes(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	String readData()
	{
		try {
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
