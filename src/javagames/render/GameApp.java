package javagames.render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javagames.util.FrameRate;
import javagames.util.KeyboardInput;
import javagames.util.RelativeMouseInput;

public class GameApp extends JFrame implements Runnable {

	private static final long serialVersionUID = -5612855817410359454L;
	
	private static final int SCREEN_W = 480;
	private static final int SCREEN_H = 480;
	
	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	private Canvas canvas;
	
	private float angle;
	private float step;
	private long sleep;
	
	public GameApp() {
	}
	
	protected void createAndShowGUI() {
		canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		
		setTitle("Game Application");
		setIgnoreRepaint(true);
		pack();
		
		// Add key listeners
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		
		// Add mouse listeners
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		
		setVisible(true);
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		running = true;
		initialize();
		long currentTime = System.nanoTime();
		long lastTime = currentTime;
		double nsPerFrame;
		while (running) {
			currentTime = System.nanoTime();
			nsPerFrame = currentTime - lastTime;
			gameLoop(nsPerFrame / 1.0E9);
			lastTime = currentTime;
		}
	}
	
	private void gameLoop(double timeDelta) {
		processInput(timeDelta);
		updateObjects(timeDelta);
		renderFrame();
		sleep(sleep);
	}
	
	private void renderFrame() {
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
	
	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// Do nothing.
		}
	}
	
	private void initialize() {
		frameRate = new FrameRate();
		frameRate.initialize();
		
		angle = 0.0f;
		step = (float)Math.PI / 2.0f;
	}
	
	private void processInput(double timeDelta) {
		keyboard.poll();
		mouse.poll();
		
		if (keyboard.keyDownOnce(KeyEvent.VK_UP)) {
			sleep += 10;
		}
		
		if (keyboard.keyDownOnce(KeyEvent.VK_DOWN)) {
			sleep -= 10;
		}
		
		if (sleep > 1000) {
			sleep = 1000;
		}
		
		if (sleep < 0) {
			sleep = 0;
		}
	}
	
	private void updateObjects(double timeDelta) {
		angle += step * timeDelta;
		if (angle > 2 * Math.PI) {
			angle -= 2 * Math.PI;
		}
	}

	private void render(Graphics g) {
		frameRate.calculate();
		
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		g.setColor(Color.BLACK);
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString("Up arrow increases sleep time", 20, 35);
		g.drawString("Down arrow decreases sleep time", 20, 50);
		g.drawString("Sleep time (ms): " + sleep, 20, 65);
		
		int x = canvas.getWidth() / 4;
		int y = canvas.getHeight() / 4;
		int w = canvas.getWidth() / 2;
		int h = canvas.getHeight() / 2;
		g.drawOval(x, y, w, h);
		
		float radiusWidth = w / 2;
		float radiusHeight = h / 2;
		int rx = (int)(radiusWidth * Math.cos(angle));
		int ry = (int)(radiusHeight * Math.sin(angle));
		
		int cx = (int)(rx + w);
		int cy = (int)(ry + h);
		
		// Draw clock hand
		g.drawLine(w, h, cx, cy);
		// and the dot at the and of the hand
		g.drawRect(cx - 2, cy - 2, 4, 4);
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
		final GameApp app = new GameApp();
		app.addWindowListener(new WindowAdapter() {
			@Override
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
