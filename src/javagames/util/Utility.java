package javagames.util;

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
}

