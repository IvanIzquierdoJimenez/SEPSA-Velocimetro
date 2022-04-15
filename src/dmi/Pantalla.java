package dmi;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

import java.awt.*;

public class Pantalla extends JFrame{

	int AngIni = -29;
	int Ang = 238;
	int div = 1/*1.025f*/;
	float v_pref = 0;
	static float v_act = 0;
	static int v_max = 160;
	int centx = 225;
	int centy = 175+40;
	Velocidad vel;
	JLabel spd;
	JLabel modo;
	float scale = /*1.3f*/ 1f;
	public static Archivo arc = new Archivo();
	int getScale(double val)
	{
		return (int)Math.round(val*scale);
	}
	public Pantalla() {
		 setTitle("VELOCIMETRO");
		 setSize(getScale(450), getScale(350));
		 setVisible(true);
		 setResizable(false);
		 //setExtendedState(JFrame.MAXIMIZED_BOTH);
		 getContentPane().setBackground(Color.BLACK);
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		 setLayout(null);
		 vel = new Velocidad(this);
		 add(vel);
		 vel.setBounds(0, 0, getScale(450), getScale(350));
		 for(int i=0; i<=v_max; i+=10)
		 {
			 JLabel j = new JLabel(Integer.toString(i));
			 j.setFont(new Font("Arial", Font.PLAIN, getScale(20)));
			 j.setForeground(Color.YELLOW);
			 j.setHorizontalAlignment(SwingConstants.CENTER);
			 int r = 125;
			 if(i<100) r = 130;
			 if(i==0) r = 135;
			 double cx = centx - r*Math.cos(Math.toRadians(AngIni+i/(float)v_max*Ang));
			 double cy = centy - r*Math.sin(Math.toRadians(AngIni+i/(float)v_max*Ang));
			 add(j);
			 j.setBounds(getScale(cx-25), getScale(cy-10), getScale(50), getScale(20));
		 }
		 spd = new JLabel(Integer.toString(Math.round(v_act)));
		 spd.setFont(new Font("Arial", Font.PLAIN, getScale(26)));
		 spd.setForeground(Color.GREEN);
		 spd.setHorizontalAlignment(SwingConstants.RIGHT);
		 Border b = BorderFactory.createLineBorder(Color.RED);
		 spd.setBorder(b);
		 add(spd);
		 spd.setBounds(getScale(centx-30), getScale(centy-70), getScale(60), getScale(30));
		 
		 JLabel unit = new JLabel("km/h");
		 unit.setFont(new Font("Arial", Font.PLAIN, getScale(20)));
		 unit.setForeground(Color.BLUE);
		 unit.setHorizontalAlignment(SwingConstants.CENTER);
		 add(unit);
		 unit.setBounds(getScale(centx-40), getScale(centy-40), getScale(80), getScale(30));
		 
		 modo = new JLabel();
		 modo.setFont(new Font("Arial", Font.PLAIN, getScale(14)));
		 modo.setForeground(Color.YELLOW);
		 modo.setHorizontalAlignment(SwingConstants.CENTER);
		 modo.setBorder(b);
		 updateModo(1);
		 add(modo);
		 modo.setBounds(getScale(centx-60), getScale(centy), getScale(120), getScale(30));
		 
		 JLabel vigil = new JLabel();
		 vigil.setIcon(new ImageIcon(getClass().getResource("/dmi/vigilancia.JPG")));
		 add(vigil);
		 vigil.setBounds(getScale(10), getScale(10), getScale(50), getScale(44));
	}
	public void updateReal(float curr)
	{
		v_act = curr;
		spd.setText(Integer.toString(Math.round(v_act)));
		repaint();
	}
	public void updatePrefijada(float curr)
	{
		v_pref = curr;
		revalidate();
		repaint();
	}
	String modos[] = {"MANUAL","VEL. PREFIJADA", "TALLER", "ENGANCHE"};
	public void updateModo(int curr)
	{
		modo.setText(modos[curr]);
	}
	public boolean TestInit(Pantalla p) throws InterruptedException
	{
		for(int i = 0; i <= v_max; i++)
		{
			p.updateReal(i);
			p.updatePrefijada(i);
			Thread.sleep(6);
		}
		for(int i = v_max; i >= 0; i--)
		{
			p.updateReal(i);
			p.updatePrefijada(i);
			Thread.sleep(6);
		}
		for(int y = 0; y <= modos.length - 1; y++)
		{
			p.updateModo(y);
			Thread.sleep(350);
		}
		return true;
	}
	public static void main(String[] args) throws InterruptedException
	{
		String[] Conf = arc.ReadConfig("../SEPSA-Velocimetro/config.txt");
		v_max = Integer.parseInt(Conf[0]);
		Pantalla p = new Pantalla();
		Client c = new Client();
		p.updateModo(0);
		c.sendData("register(speed)");
		c.sendData("register(cruise_speed)");
		c.sendData("register(mode)");
		if(p.TestInit(p) == true);
		else return;
		Thread.sleep(100);
		p.updateModo(Integer.parseInt(Conf[1]));
		while(true)
		{
			String s = c.readData();
			if(s==null) return;
			if(s.startsWith("speed="))
			{
				p.updateReal(Float.parseFloat(s.substring(6).replace(',', '.')));
			}
			else if(s.startsWith("cruise_speed="))
			{
				p.updatePrefijada(Float.parseFloat(s.substring(13).replace(',', '.')));
			}
			else if(s.startsWith("mode="))
			{
				p.updateModo(Integer.parseInt(s.substring(5)));
			}
		}
	}
}
