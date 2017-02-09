package javagames.render;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javagames.util.FrameRate;

public class GameApp extends JFrame implements Runnable {

	private static final long serialVersionUID = -5612855817410359454L;
	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	private GraphicsDevice graphicsDevice;
	private DisplayMode currentDisplayMode;
	
	public GameApp() {
		frameRate = new FrameRate();
	}
	
	protected void createAndShowGUI() {
		setIgnoreRepaint(true);
		setUndecorated(true);
		setBackground(Color.BLACK);
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice = ge.getDefaultScreenDevice();
		currentDisplayMode = graphicsDevice.getDisplayMode();
		
		if (!graphicsDevice.isFullScreenSupported()) {
			System.err.println("ERROR: Fullscreen not supported.");
			System.exit(ERROR);
		}
		
		graphicsDevice.setFullScreenWindow(this);
		graphicsDevice.setDisplayMode(getDisplayMode());
		
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					shutDown();
				}
			}
		});
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private DisplayMode getDisplayMode() {
		return new DisplayMode(1920, 1080, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
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
		g.drawString("Press ESC to exit...", 30, 60);
	}
	
	protected void shutDown() {
		try {
			running = false;
			gameThread.join();
			System.out.println("Game loop stopped.");
			graphicsDevice.setDisplayMode(currentDisplayMode);
			graphicsDevice.setFullScreenWindow(null);
			System.out.println("Display restored.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		final GameApp app = new GameApp();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}
