package dmi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;

public class Velocidad extends JLabel {
	Pantalla p;
	int r1 = 180;
	int r2 = 163;
	int r3 = 146;
	public Velocidad(Pantalla p)
	{
		this.p = p;
		scale = p.scale;
		setOpaque(false);
	}
	float scale;
	int getScale(int val)
	{
		return (int)Math.round(val*scale);
	}
	void drawArc(Graphics g, float cx, float cy, float rad, int a0, int fin)
	{
		int x = (int) (cx-rad);
		int y = (int) (cy-rad);
		int w = (int) (2*rad);
		int h = w;
		g.drawArc(x, y, w, h, 180-a0, -fin);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r1), p.AngIni, p.Ang);
		drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r2), p.AngIni, p.Ang);
		drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r3), p.AngIni, p.Ang);
		drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r1+1), p.AngIni, p.Ang);
		drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r2+1), p.AngIni, p.Ang);
		drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r3+1), p.AngIni, p.Ang);
		//drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r1+21), p.AngIni, p.Ang);
		//drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r1+22), p.AngIni, p.Ang);
		//drawArc(g, getScale(p.centx), getScale(p.centy), getScale(r1+23), p.AngIni, p.Ang);
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(getScale(p.centx), getScale(p.centy));
		g2d.rotate(Math.toRadians(p.AngIni-90));
		AffineTransform transf = g2d.getTransform();
		
		g2d.setTransform(transf);
		g.setColor(Color.YELLOW);
		Rectangle pref = new Rectangle(getScale(1), getScale(2-r1), getScale(6), getScale(12));
		for(int i=0; i+1<p.v_pref; i+=p.div)
		{
			g2d.fill(pref);
			g2d.rotate(Math.toRadians(p.Ang/(float)p.v_max*p.div));
		}
		
		g2d.setTransform(transf);
		g.setColor(Color.GREEN);
		Rectangle spd = new Rectangle(getScale(1), getScale(2-r2), getScale(6), getScale(12));
		for(int i=0; i+1<p.v_act; i+=p.div)
		{
			g2d.fill(spd);
			g2d.rotate(Math.toRadians(p.Ang/(float)p.v_max*p.div));
		}
		
		g2d.setTransform(transf);
		g.setColor(Color.WHITE);
		Rectangle line = new Rectangle(getScale(-1), getScale(-r1-2), getScale(2), getScale(r1-r3+4));
		for(int i=0; i<=p.v_max; i+=10)
		{
			g2d.fill(line);
			g2d.rotate(Math.toRadians(p.Ang/(float)p.v_max*10));
		}
	}
}
