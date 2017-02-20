package javagames.render;

import java.awt.*;
import java.awt.event.*;
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

    private float worldWidth;
    private float worldHeight;

	private GameApp() {
	}
	
	private void createAndShowGUI() {
		canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLayout(null);
        setTitle("Game Application");
		setSize(SCREEN_W, SCREEN_H);
		getContentPane().add(canvas);
		
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);

		getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onComponentResized(e);
            }
        });
		setVisible(true);
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void onComponentResized(ComponentEvent e) {
	    Dimension size = getContentPane().getSize();
	    int viewWidth = size.width * 3 / 4;
	    int viewHeight = size.height * 3 / 4;
	    int viewXPosition = (size.width - viewWidth) / 2;
	    int viewYPosition = (size.height - viewHeight) / 2;

	    int newWidth = viewWidth;
	    int newHeight = (int)(viewWidth * worldHeight / worldWidth);
	    if (newHeight > viewHeight) {
	        newWidth = (int)(viewHeight * worldWidth / worldHeight);
	        newHeight = viewHeight;
        }

        viewXPosition += (viewWidth - newWidth) / 2;
	    viewYPosition += (viewHeight - newHeight) / 2;
	    canvas.setLocation(viewXPosition, viewYPosition);
	    canvas.setSize(newWidth, newHeight);
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
				new Vector2f(0.0f, 2.25f),
				new Vector2f(-4.0f, -2.25f),
				new Vector2f(4.0f, -2.25f)
		};
		triangleWorld = new Vector2f[triangle.length];

		worldWidth = 16.0f;
		worldHeight = 9.0f;
	}

	private void gameLoop(double timeDelta) {
		processInput(timeDelta);
		updateObjects(timeDelta);
		renderFrame();
		sleep();
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
	
	private void sleep() {
		try {
			Thread.sleep(10L);
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

		float scaleHorizontal = (canvas.getWidth() - 1) / worldWidth;
		float scaleVertical = (canvas.getHeight() - 1) / worldHeight;
		float translateX = (canvas.getWidth() - 1) / 2.0f;
		float translateY = (canvas.getHeight() - 1) / 2.0f;

		Matrix3x3f viewPort = Matrix3x3f.identity();
		viewPort = viewPort.mul(Matrix3x3f.scale(scaleHorizontal, -scaleVertical));
		viewPort = viewPort.mul(Matrix3x3f.translate(translateX, translateY));

		for (int i = 0; i < triangle.length; i++) {
			triangleWorld[i] = viewPort.mul(triangle[i]);
		}
		drawPolygon(g, triangleWorld);
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
