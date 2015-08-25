package org.evors.vision;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

public class ShowImg  extends JFrame
{
	protected Dimension imgDim = new Dimension( 752, 480 );
	
	public Image img;
	
	public ShowImg( Image img, int scaleDown )
	{
		this.img = img;
		imgDim = new Dimension( imgDim.width / scaleDown, imgDim.height / scaleDown );
		this.setSize( imgDim.width, imgDim.height + 30 );
	}
	
	public ShowImg( Image img ){ this( img, 1 ); }
	
	public void paint( Graphics g )
	{
		g.drawImage( img, 0, 30, imgDim.width, imgDim.height, null );
	}
	
	public void setImage( Image img )
	{
		this.img = img;
		repaint();
	}
}
