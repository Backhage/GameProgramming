package javagames.render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
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
	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	private Canvas canvas;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	private Point point = new Point(0,0);
	private boolean disableCursor = false;
	
	public GameApp() {
		frameRate = new FrameRate();
	}
	
	protected void createAndShowGUI() {
		canvas = new Canvas();
		canvas.setSize(640, 480);
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
		frameRate.initialize();
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
	
	private void processInput() {
		keyboard.poll();
		mouse.poll();
		
		Point p = mouse.getPosition();
		if (mouse.isRelative()) {
			point.x += p.x;
			point.y += p.y;
		} else {
			point.x = p.x;
			point.y = p.y;
		}
		
		// Wrap rectangle around the screen
		if (point.x + 25 < 0) {
			point.x = canvas.getWidth() - 1;
		} else if (point.x > canvas.getWidth() - 1) {
			point.x = -25;
		}
		
		if (point.y + 25 < 0) {
			point.y = canvas.getHeight() - 1;
		} else if (point.y > canvas.getHeight() - 1) {
			point.y = -25;
		}
		
		// Toggle relative
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			mouse.setRelative(!mouse.isRelative());
		}
		
		// Toggle cursor
		if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
			disableCursor = !disableCursor;
			if (disableCursor) {
				disableCursor();
			} else {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	
	private void render(Graphics g) {
		frameRate.calculate();
		g.setColor(Color.GREEN);
		g.drawString(mouse.getPosition().toString(), 20, 20);
		g.drawString("Relative: " + mouse.isRelative(), 20, 35);
		g.drawString("Press space to switch mouse modes", 20, 50);
		g.drawString("Press C to toggle cursor", 20, 65);
		g.drawString(frameRate.getFrameRate(), 20, 80);

		g.setColor(Color.WHITE);
		g.drawRect(point.x, point.y, 25, 25);
	}
	
	private void disableCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("");
		Point p = new Point(0,0);
		String name = "Can be anything";
		Cursor cursor = tk.createCustomCursor(image, p, name);
		setCursor(cursor);
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
