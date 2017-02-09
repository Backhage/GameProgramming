package javagames.render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.glass.events.WindowEvent;

import javagames.util.FrameRate;

public class HelloWorldApp extends JFrame implements Runnable {

	private static final long serialVersionUID = -5612855817410359454L;
	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	
	public HelloWorldApp() {
		frameRate = new FrameRate();
	}
	
	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(320, 240);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Active rendering");
		setIgnoreRepaint(true);
		pack();
		
		setVisible(true);
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		running = true;
		frameRate.initialize();
		while (running) {
			gameLoop();
		}
	}
	
	private void gameLoop() {
		do {
			do {
				Graphics g = null;
				try {
					g = bufferStrategy.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}
	
	private void render(Graphics g) {
		frameRate.calculate();
		g.setColor(Color.GREEN);
		g.drawString(frameRate.getFrameRate(), 30, 30);
	}
	
	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final HelloWorldApp app = new HelloWorldApp();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}
