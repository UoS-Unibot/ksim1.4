package org.evors.vision;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

public class ShowImg  extends JFrame
{
	public Image img;
	public ShowImg( Image img ){ this.img = img; this.setSize(752, 510);}
	public void paint( Graphics g )
	{
		g.drawImage( img, 0, 30, 752, 480, null );
	}
	
	public void setImage( Image img )
	{
		this.img = img;
		repaint();
	}
}
