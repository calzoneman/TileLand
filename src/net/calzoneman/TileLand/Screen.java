package net.calzoneman.TileLand;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Screen extends Canvas {
	//private BufferedImage image;
	
	public Screen() {
		this.setMinimumSize(new Dimension(320,240));
		this.setPreferredSize(new Dimension(1024,768));
		this.setMaximumSize(new Dimension(2560,1600));
		this.setBackground(Color.BLACK);
		//System.out.println(this.getWidth() + ", " + this.getHeight());
		//this.image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	//@Override
	//public Graphics getGraphics() {
		//return image.getGraphics();
	//}
	
	//@Override
	//public void paint(Graphics g) {

	//}
	
	//@Override
	//public void update(Graphics g) {
		//Graphics2D g2d = (Graphics2D)g;
		//g2d.clearRect(0,  0, this.getWidth(), this.getHeight());
		//g2d.drawImage(image, null, 0, 0);
	//}
}
