package javagames.render;

import javagames.prototype.PolygonWrapper;
import javagames.util.Matrix3x3f;
import javagames.util.SimpleFramework;
import javagames.util.Utility;
import javagames.util.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameApp extends SimpleFramework {
    private Vector2f pos;
    private Vector2f[] poly;
    private ArrayList<Vector2f[]> renderList;
    private PolygonWrapper wrapper;

    public GameApp() {
        appBorderScale = 0.9f;
        appWidth = 640;
        appHeight = 640;
        appMaintainRatio = true;
        appTitle = "Screen wrap example";
        appBackground = Color.WHITE;
        appFPSColor = Color.BLACK;
    }

    @Override
    protected void initialize() {
        super.initialize();

        mouse.setRelative(true);
        renderList = new ArrayList<>();
        wrapper = new PolygonWrapper(appWorldWidth, appWorldHeight);

        poly = new Vector2f[] {
                new Vector2f(-0.125f, 0.125f),
                new Vector2f(0.125f, 0.125f),
                new Vector2f(0.125f, -0.125f),
                new Vector2f(-0.125f, -0.125f)
        };

        pos = new Vector2f();
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);

        if (mouse.isRelative()) {
            Vector2f v = getRelativeWorldMousePosition();
            pos = pos.add(v);
        } else {
            pos = getWorldMousePosition();
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            mouse.setRelative(!mouse.isRelative());
            if (mouse.isRelative()) {
                pos = new Vector2f();
            }
        }
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);

        renderList.clear();
        pos = wrapper.wrapPosition(pos);
        Vector2f[] world = transform(poly, Matrix3x3f.translate(pos));
        renderList.add(world);
        wrapper.wrapPolygon(world, renderList);
    }

    private Vector2f[] transform(Vector2f[] poly, Matrix3x3f mat) {
        Vector2f[] copy = new Vector2f[poly.length];
        for (int i = 0; i < poly.length; i++) {
            copy[i] = mat.mul(poly[i]);
        }
        return copy;
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);

        g.drawString("Press space to toggle mouse", 20,35);

        Matrix3x3f view = getViewportTransform();
        for (Vector2f[] toRender : renderList) {
            for (int i = 0; i < toRender.length; i++) {
                toRender[i] = view.mul(toRender[i]);
            }
            Utility.drawPolygon(g, toRender);
        }
    }

    public static void main(String[] args) {
        launchApp(new GameApp());
    }
}
