package javagames.util;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Utility {
    public static Matrix3x3f createViewPort(float worldWidth, float worldHeight, float screenWidth, float screenHeight) {
        float scaleX = (screenWidth - 1) / worldWidth;
        float scaleY = (screenHeight - 1) / worldHeight;
        float translateX = (screenWidth - 1) / 2.0f;
        float translateY = (screenHeight - 1) / 2.0f;

        Matrix3x3f viewport = Matrix3x3f.scale(scaleX, -scaleY);
        viewport = viewport.mul(Matrix3x3f.translate(translateX, translateY));
        return viewport;
    }

    public static Matrix3x3f createReverseViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight) {
        float scaleX = worldWidth /(screenWidth - 1);
        float scaleY = worldHeight / (screenHeight - 1);
        float translateX = (screenWidth - 1) / 2.0f;
        float translateY = (screenHeight - 1) / 2.0f;

        Matrix3x3f viewport = Matrix3x3f.translate(-translateX, -translateY);
        viewport = viewport.mul(Matrix3x3f.scale(scaleX, -scaleY));
        return viewport;
    }

    public static void drawPolygon(Graphics g, Vector2f[] polygon) {
        Vector2f P;
        Vector2f S = polygon[polygon.length - 1];
        for (int i = 0; i < polygon.length; i++) {
            P = polygon[i];
            g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
            S = P;
        }
    }

    public static void fillPolygon(Graphics2D g, Vector2f[] polygon) {
        Polygon p = new Polygon();
        for (Vector2f v : polygon) {
            p.addPoint((int)v.x, (int)v.y);
        }
        g.fillPolygon(p);
    }

    public static void fillPolygon(Graphics2D g, List<Vector2f> polygon) {
        Polygon p = new Polygon();
        for (Vector2f v : polygon) {
            p.addPoint((int)v.x, (int)v.y);
        }
        g.fillPolygon(p);
    }
}

