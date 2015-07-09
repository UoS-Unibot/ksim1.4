package org.evors.vision;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

public class ShowImg  extends JFrame
{
	Image img;
	public ShowImg( Image img ){ this.img = img; this.setSize(752, 480);}
	public void paint( Graphics g )
	{
		g.drawImage( img, 0, 0, 752, 480, null );
	}
}
