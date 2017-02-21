package javagames.render;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javagames.util.*;

public class GameApp extends JFrame implements Runnable {

	private static final int SCREEN_W = 640;
	private static final int SCREEN_H = 640;

	private FrameRate frameRate;
	private BufferStrategy bufferStrategy;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;

    private Canvas canvas;

	private Vector2f[] cannon;
	private Vector2f[] cannonCopy;
	private float cannonRotation, cannonDelta;

	private Vector2f bullet;
	private Vector2f bulletCopy;
	private Vector2f velocity;

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

		velocity = new Vector2f();
		cannonRotation = 0.0f;
		cannonDelta = (float)Math.toRadians(90.0);
		cannon = new Vector2f[] {
		        new Vector2f(-0.5f, 0.125f),
                new Vector2f(0.5f, 0.125f),
                new Vector2f(0.5f, -0.125f),
                new Vector2f(-0.5f, -0.125f)
        };
		cannonCopy = new Vector2f[cannon.length];

		Matrix3x3f scale = Matrix3x3f.scale(0.75f, 0.75f);
		for (int i = 0; i < cannon.length; i++) {
		    cannon[i] = scale.mul(cannon[i]);
        }
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

		if (keyboard.keyDown(KeyEvent.VK_A)) {
		    cannonRotation += cannonDelta * timeDelta;
        }

        if (keyboard.keyDown(KeyEvent.VK_D)) {
		    cannonRotation -= cannonDelta * timeDelta;
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
		    // New velocity
		    Matrix3x3f matrix = Matrix3x3f.translate(7.0f, 0.0f);
		    matrix = matrix.mul(Matrix3x3f.rotate(cannonRotation));
		    velocity = matrix.mul(new Vector2f());

		    // Place bullet at cannon end
            matrix = Matrix3x3f.translate(0.375f, 0.0f);
            matrix = matrix.mul(Matrix3x3f.rotate(cannonRotation));
            matrix = matrix.mul(Matrix3x3f.translate(-2.0f, -2.0f));
            bullet = matrix.mul(new Vector2f());
        }
	}
	
	private void updateObjects(double timeDelta) {
	    Matrix3x3f matrix = Matrix3x3f.identity();
	    matrix = matrix.mul(Matrix3x3f.rotate(cannonRotation));
	    matrix = matrix.mul(Matrix3x3f.translate(-2.0f, -2.0f));

	    for (int i = 0; i < cannon.length; i++) {
	        cannonCopy[i] = matrix.mul(cannon[i]);
        }

        if (bullet != null) {
	        velocity.y += -9.8f * timeDelta;
	        bullet.x += velocity.x * timeDelta;
	        bullet.y += velocity.y * timeDelta;
	        bulletCopy = new Vector2f(bullet);

	        if (bullet.y < -2.5f) {
	            bullet = null;
            }
        }
	}

	private void render(Graphics g) {
		frameRate.calculate();
		
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		g.setColor(Color.BLACK);
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString("(A) to raise, (D) to lower", 20, 35);
		g.drawString("Press space to fire cannon", 20, 50);

		String vel = String.format("Velocity: (%.2f,%.2f)", velocity.x, velocity.y);
		g.drawString(vel, 20, 65);

		float worldWidth = 5.0f;
		float worldHeight = 5.0f;
		float screenWidth = canvas.getWidth() - 1;
		float screenHeight = canvas.getHeight() - 1;

		float scaleHorizontal = screenWidth / worldWidth;
		float scaleVertical = -screenHeight / worldHeight;
		Matrix3x3f viewPort = Matrix3x3f.scale(scaleHorizontal, scaleVertical);

		float translateX = screenWidth / 2.0f;
		float translateY = screenHeight / 2.0f;

		viewPort = viewPort.mul(Matrix3x3f.translate(translateX, translateY));

		for (int i = 0; i < cannon.length; i++) {
			cannonCopy[i] = viewPort.mul(cannonCopy[i]);
		}
		drawPolygon(g, cannonCopy);

		if (bullet != null) {
		    bulletCopy = viewPort.mul(bulletCopy);
		    g.drawRect((int)bulletCopy.x-2, (int)bulletCopy.y-2, 4, 4);
        }
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
