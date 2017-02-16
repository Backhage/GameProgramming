package javagames.render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javagames.util.FrameRate;
import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.RelativeMouseInput;
import javagames.util.Vector2f;

public class GameApp extends JFrame implements Runnable {

	private static final long serialVersionUID = -5612855817410359454L;
	
	private static final int SCREEN_W = 640;
	private static final int SCREEN_H = 480;
	
	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	
	private float earthRot, earthDelta;
	private float moonRot, moonDelta;
	
	private boolean showStars;
	private int[] stars;
	private Random rand = new Random();
	
	public GameApp() {
	}
	
	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.BLACK);
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
		while (running) {
			gameLoop();
		}
	}
	
	private void gameLoop() {
		processInput();
		renderFrame();
		sleep(10L);
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
		
		earthDelta = (float)Math.toRadians(0.5);
		moonDelta = (float)Math.toRadians(2.5);
		
		showStars = true;
		stars = new int[1000];
		
		for (int i = 0; i < stars.length - 1; i += 2) {
			stars[i] = rand.nextInt(SCREEN_W);
			stars[i+1] = rand.nextInt(SCREEN_H);
		}
	}
	
	private void processInput() {
		keyboard.poll();
		mouse.poll();
		
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			showStars = !showStars;
		}
	}
	private void render(Graphics g) {
		frameRate.calculate();
		
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		g.setColor(Color.GREEN);
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString("Press [SPACE] to toggle stars", 20, 35);
		
		if (showStars) {
			g.setColor(Color.WHITE);
			for (int i = 0; i < stars.length - 1; i++) {
				g.fillRect(stars[i], stars[i+1], 1, 1);
			}
		}
		
		// Draw the sun
		Matrix3x3f sunMatrix = Matrix3x3f.identity();
		sunMatrix = sunMatrix.mul(Matrix3x3f.translate(SCREEN_W / 2, SCREEN_H / 2));
		
		Vector2f sun = sunMatrix.mul(new Vector2f());
		g.setColor(Color.YELLOW);
		g.fillOval((int)sun.x - 50, (int)sun.y - 50, 100, 100);
		
		// Draw earth's orbit
		g.setColor(Color.WHITE);
		g.drawOval((int)sun.x - SCREEN_W / 4, (int)sun.y - SCREEN_W / 4, SCREEN_W / 2, SCREEN_W / 2);
		
		// Draw the earth
		Matrix3x3f earthMatrix = Matrix3x3f.translate(SCREEN_W / 4, 0);
		earthMatrix = earthMatrix.mul(Matrix3x3f.rotate(earthRot));
		earthMatrix = earthMatrix.mul(sunMatrix);
		
		earthRot += earthDelta;
		
		Vector2f earth = earthMatrix.mul(new Vector2f());
		g.setColor(Color.BLUE);
		g.fillOval((int)earth.x - 10, (int)earth.y - 10, 20, 20);
		
		// Draw the moon
		Matrix3x3f moonMatrix = Matrix3x3f.translate(30, 0);
		moonMatrix = moonMatrix.mul(Matrix3x3f.rotate(moonRot));
		moonMatrix = moonMatrix.mul(earthMatrix);
		moonRot += moonDelta;
		
		Vector2f moon = moonMatrix.mul(new Vector2f());
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int)moon.x - 5, (int)moon.y - 5, 10, 10);
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
