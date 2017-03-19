package javagames.prototype;

import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

import java.util.Random;

public class PrototypeAsteroidFactory {
    private static final Vector2f[][] LARGE = {
            { // Large 0
                new Vector2f(0.09546161f, 0.1173709f),
                new Vector2f(0.029733896f, 0.08920187f),
                new Vector2f(-0.020344317f, 0.114241004f),
                new Vector2f(-0.107981205f, 0.08920187f),
                new Vector2f(-0.12989044f, 0.029733956f),
                new Vector2f(-0.1205008f, -0.042253494f),
                new Vector2f(-0.07042253f, -0.07042253f),
                new Vector2f(0.06416273f, -0.07042253f),
                new Vector2f(0.051643133f, 0.029733956f),
                new Vector2f(0.08920181f, 0.08294213f)
            }, { // Large 1
                new Vector2f(-0.051643193f, 0.13928014f),
                new Vector2f(-0.12989044f, 0.07668233f),
                new Vector2f(-0.1236307f, 0.035993755f),
                new Vector2f(-0.17370892f, -0.039123654f),
                new Vector2f(-0.12989044f, -0.104851365f),
                new Vector2f(-0.039123654f, -0.07355237f),
                new Vector2f(0.014084458f, -0.13302028f),
                new Vector2f(0.14241004f, -0.017214417f),
                new Vector2f(0.06729269f, 0.023474216f),
                new Vector2f(0.07981217f, 0.111111104f),
                new Vector2f(0.0015649796f, 0.1205008f)
            }, { // Large2
                new Vector2f(0.12989044f, 0.111111104f),
                new Vector2f(0.06416273f, 0.15805948f),
                new Vector2f(-0.0046948195f, 0.13615024f),
                new Vector2f(-0.1236307f, 0.14866978f),
                new Vector2f(-0.19561815f, 0.026604056f),
                new Vector2f(-0.16431928f, -0.111111045f),
                new Vector2f(-0.08607197f, -0.1205008f),
                new Vector2f(-0.023474216f, -0.039123654f),
                new Vector2f(0.042253494f, -0.1267606f),
                new Vector2f(0.17683876f, -0.032863855f),
            }
    };

    private static final Vector2f[][] MEDIUM = {
            { // Medium0
                new Vector2f(0.054773092f, 0.08294213f),
                new Vector2f(0.007824659f, 0.1173709f),
                new Vector2f(-0.054773092f, 0.05790299f),
                new Vector2f(-0.107981205f, 0.054773092f),
                new Vector2f(-0.09233177f, -0.039123654f),
                new Vector2f(-0.032863855f, -0.06416273f),
                new Vector2f(0.039123654f, -0.042253494f),
                new Vector2f(0.057902932f, 0.054773092f),
            },{ // Medium1
                new Vector2f(0.048513293f, 0.13615024f),
                new Vector2f(0.051643133f, 0.07981223f),
                new Vector2f(0.101721406f, 0.045383394f),
                new Vector2f(0.107981205f, -0.029733896f),
                new Vector2f(0.045383453f, -0.06416273f),
                new Vector2f(-0.06416279f, -0.12989044f),
                new Vector2f(-0.08920187f, -0.051643133f),
                new Vector2f(-0.06729263f, -0.0046948195f),
                new Vector2f(-0.114241004f, 0.06729263f),
                new Vector2f(-0.039123654f, 0.098591566f),
                new Vector2f(-0.010954618f, 0.06729263f)
            }, { // Medium2
                new Vector2f(0.098591566f, -0.06729269f),
                new Vector2f(0.014084458f, -0.12363064f),
                new Vector2f(-0.08607197f, -0.117370844f),
                new Vector2f(-0.1205008f, -0.057902932f),
                new Vector2f(-0.098591566f, 0.048513293f),
                new Vector2f(-0.051643193f, 0.09233177f),
                new Vector2f(0.045383453f, 0.1173709f),
                new Vector2f(0.054773092f, 0.035993755f)
            }
    };

    private static final Vector2f[][] SMALL = {
            { // Small0
                new Vector2f(0.020344257f, 0.051643193f),
                new Vector2f(-0.029733956f, 0.032863855f),
                new Vector2f(-0.051643193f, -0.017214417f),
                new Vector2f(-0.029733956f, -0.07981217f),
                new Vector2f(0.020344257f, -0.045383453f),
                new Vector2f(0.039123654f, 0.007824719f)
            },
            { // Small1
                new Vector2f(0.020344257f, -0.026604056f),
                new Vector2f(-0.017214417f, -0.042253494f),
                new Vector2f(-0.048513293f, -0.023474216f),
                new Vector2f(-0.042253554f, -0.0015649796f),
                new Vector2f(-0.042253554f, 0.035993755f),
                new Vector2f(0.007824659f, 0.05790299f),
                new Vector2f(0.029733896f, 0.035993755f),
                new Vector2f(0.029733896f, 0.0015649796f)
            },
            { // Small2
                new Vector2f(0.035993695f, 0.06729263f),
                new Vector2f(0.029733896f, 0.039123654f),
                new Vector2f(0.048513293f, 0.010954618f),
                new Vector2f(0.039123654f, -0.039123654f),
                new Vector2f(0.0015649796f, -0.054773092f),
                new Vector2f(-0.026604056f, -0.035993695f),
                new Vector2f(-0.039123654f, 0.039123654f),
                new Vector2f(0.0015649796f, 0.06729263f)
            }
    };

    private PolygonWrapper wrapper;
    private Random rand;

    public PrototypeAsteroidFactory(PolygonWrapper wrapper) {
        this.wrapper = wrapper;
        this.rand = new Random();
    }

    public PrototypeAsteroid createLargeAsteroid(Vector2f position) {
        PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
        asteroid.setPosition(position);
        asteroid.setPolygon(getRandomAsteroid(LARGE));
        asteroid.setSize(PrototypeAsteroid.Size.Large);
        return asteroid;
    }

    public PrototypeAsteroid createMediumAsteroid(Vector2f position) {
        PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
        asteroid.setPosition(position);
        asteroid.setPolygon(getRandomAsteroid(MEDIUM));
        asteroid.setSize(PrototypeAsteroid.Size.Medium);
        return asteroid;
    }

    public PrototypeAsteroid createSmallAsteroid(Vector2f position) {
        PrototypeAsteroid asteroid = new PrototypeAsteroid(wrapper);
        asteroid.setPosition(position);
        asteroid.setPolygon(getRandomAsteroid(SMALL));
        asteroid.setSize(PrototypeAsteroid.Size.Small);
        return asteroid;
    }

    private Vector2f[] getRandomAsteroid(Vector2f[][] asteroids) {
        return mirror(asteroids[rand.nextInt(asteroids.length)]);
    }

    private Vector2f[] mirror(Vector2f[] polygon) {
        Vector2f[] mirror = new Vector2f[polygon.length];
        float x = rand.nextBoolean() ? 1.0f : -1.0f;
        float y = rand.nextBoolean() ? 1.0f : -1.0f;
        Matrix3x3f mat = Matrix3x3f.scale(x, y);
        for (int i = 0; i < polygon.length; i++) {
            mirror[i] = mat.mul(polygon[i]);
        }
        return mirror;
    }
}
