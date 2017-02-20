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

import javagames.util.*;

public class GameApp extends JFrame implements Runnable {

	private static final int SCREEN_W = 640;
	private static final int SCREEN_H = 480;

	private Canvas canvas;
	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;

	private Vector2f[] triangle;
	private Vector2f[] triangleWorld;

    private Vector2f[] rectangle;
    private Vector2f[] rectangleWorld;

	private GameApp() {
	}
	
	private void createAndShowGUI() {
		canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		
		setTitle("Game Application");
		setIgnoreRepaint(true);
		pack();
		
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		
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

	private void initialize() {
		frameRate = new FrameRate();
		frameRate.initialize();

		triangle = new Vector2f[] {
				new Vector2f(0.0f, 0.5f),
				new Vector2f(-0.5f, -0.5f),
				new Vector2f(0.5f, -0.5f)
		};
		triangleWorld = new Vector2f[triangle.length];

		rectangle = new Vector2f[] {
				new Vector2f(-1.0f, 1.0f),
				new Vector2f(1.0f, 1.0f),
				new Vector2f(1.0f, -1.0f),
				new Vector2f(-1.0f, -1.0f)
		};
		rectangleWorld = new Vector2f[rectangle.length];
	}

	private void gameLoop(double timeDelta) {
		processInput(timeDelta);
		updateObjects(timeDelta);
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
	
	private void processInput(double timeDelta) {
		keyboard.poll();
		mouse.poll();
	}
	
	private void updateObjects(double timeDelta) {
	}

	private void render(Graphics g) {
		frameRate.calculate();
		
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		g.setColor(Color.BLACK);
		g.drawString(frameRate.getFrameRate(), 20, 20);

		float worldWidth = 2.0f;
		float worldHeight = 2.0f;
		float screenWidth = canvas.getWidth() - 1;
		float screenHeight = canvas.getHeight() - 1;
		float scaleHorizontal = screenWidth / worldWidth;
		float scaleVertical = screenHeight / worldHeight;

		Matrix3x3f viewPort = Matrix3x3f.scale(scaleHorizontal, -scaleVertical);
		viewPort = viewPort.mul(Matrix3x3f.translate(screenWidth / 2.0f, screenHeight / 2.0f));

		for (int i = 0; i < triangle.length; i++) {
			triangleWorld[i] = viewPort.mul(triangle[i]);
		}
		drawPolygon(g, triangleWorld);

		for (int i = 0; i < rectangle.length; i++) {
			rectangleWorld[i] = viewPort.mul(rectangle[i]);
		}
		drawPolygon(g, rectangleWorld);
	}

	private void drawPolygon(Graphics g, Vector2f[] polygon) {
		Vector2f P;
		Vector2f S = polygon[polygon.length - 1];
        for (Vector2f aPolygon : polygon) {
            P = aPolygon;
            g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
            S = P;
        }
	}
	
	private void onWindowClosing() {
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
		SwingUtilities.invokeLater(() -> app.createAndShowGUI());
	}
}
