package javagames.prototype;

import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

import java.util.Random;

public class PrototypeAsteroidFactory {
    private static final Vector2f[][] LARGE = {
            { // Large 0
                new Vector2f(0.054773092f, 0.74960876f),
                new Vector2f(-0.16118938f, 0.7308294f),
                new Vector2f(-0.41158062f, 0.43035996f),
                new Vector2f(-0.7652582f, 0.29264474f),
                new Vector2f(-0.6025039f, -0.017214417f),
                new Vector2f(-0.4460094f, -0.37715173f),
                new Vector2f(-0.7089202f, -0.40532076f),
                new Vector2f(-0.48982787f, -0.80281687f),
                new Vector2f(-0.08607197f, -0.57433486f),
                new Vector2f(0.17370892f, -0.6744914f),
                new Vector2f(0.6870109f, -0.2832551f),
                new Vector2f(0.17996871f, -0.1267606f),
                new Vector2f(0.36463225f, 0.20187795f),
                new Vector2f(0.3865415f, 0.49608767f),
                new Vector2f(0.1267606f, 0.62441313f)
            }, { // Large 1
                new Vector2f(0.13302028f, 0.68701094f),
                new Vector2f(-0.29890454f, 0.6087637f),
                new Vector2f(-0.53990614f, 0.3802817f),
                new Vector2f(-0.6087637f, 0.16744918f),
                new Vector2f(-0.6995305f, -0.14241004f),
                new Vector2f(-0.53051645f, -0.5586854f),
                new Vector2f(-0.3489828f, -0.7151799f),
                new Vector2f(-0.032863855f, -0.7777778f),
                new Vector2f(0.39280128f, -0.76838803f),
                new Vector2f(0.68388104f, -0.342723f),
                new Vector2f(0.69014084f, 0.020344317f),
                new Vector2f(0.6682316f, 0.23630673f),
                new Vector2f(0.114241004f, -0.029733896f),
                new Vector2f(-0.035993755f, 0.22378719f),
                new Vector2f(0.51486695f, 0.48982787f),
                new Vector2f(0.25508606f, 0.57433486f),
                new Vector2f(-0.098591566f, 0.43035996f),
                new Vector2f(0.08920181f, 0.6025039f)
            }, { // Large2
                new Vector2f(0.010954618f, 0.75586855f),
                new Vector2f(-0.24256653f, 0.65884197f),
                new Vector2f(-0.51486695f, 0.24569643f),
                new Vector2f(-0.81846637f, 0.36776215f),
                new Vector2f(-0.7339593f, -0.029733896f),
                new Vector2f(-0.39593112f, -0.017214417f),
                new Vector2f(-0.3395931f, -0.5774648f),
                new Vector2f(0.14866984f, -0.81533647f),
                new Vector2f(0.7527386f, -0.3552426f),
                new Vector2f(0.48356807f, 0.41471046f),
                new Vector2f(0.06103289f, 0.6025039f)
            }
    };

    private static final Vector2f[][] MEDIUM = {
            { // Medium0
                new Vector2f(0.13302028f, 0.26134586f),
                new Vector2f(-0.07042253f, 0.31455398f),
                new Vector2f(-0.23630673f, 0.2832551f),
                new Vector2f(-0.31455398f, 0.07981223f),
                new Vector2f(-0.3364632f, -0.14241004f),
                new Vector2f(-0.15805948f, -0.26134586f),
                new Vector2f(0.07981217f, -0.30516434f),
                new Vector2f(0.29264474f, -0.14553988f),
                new Vector2f(0.30829418f, -0.042253494f),
                new Vector2f(0.30516434f, 0.1236307f)
            },{ // Medium1
                new Vector2f(0.20813775f, 0.19874805f),
                new Vector2f(0.12989044f, 0.2707355f),
                new Vector2f(0.06416273f, 0.23630673f),
                new Vector2f(-0.08607197f, 0.29264474f),
                new Vector2f(-0.19561815f, 0.21126759f),
                new Vector2f(-0.15805948f, 0.1205008f),
                new Vector2f(-0.3364632f, -0.0046948195f),
                new Vector2f(-0.15492958f, -0.22691703f),
                new Vector2f(-0.09233177f, -0.4397496f),
                new Vector2f(0.08607197f, -0.45539904f),
                new Vector2f(0.17370892f, -0.29890454f),
                new Vector2f(0.17370892f, -0.15179968f),
                new Vector2f(0.3802817f, -0.07981217f),
                new Vector2f(0.33959305f, 0.07355243f)
            }, { // Medium2
                new Vector2f(0.16744912f, 0.2769953f),
                new Vector2f(0.31768382f, 0.13928014f),
                new Vector2f(0.25195622f, -0.020344257f),
                new Vector2f(0.2832551f, -0.22065723f),
                new Vector2f(0.035993695f, -0.30203438f),
                new Vector2f(-0.24256653f, -0.18309855f),
                new Vector2f(-0.32081378f, 0.08607197f),
                new Vector2f(-0.101721466f, 0.20500785f)
            }
    };

    private static final Vector2f[][] SMALL = {
            { // Small0
                new Vector2f(0.104851365f, 0.17996871f),
                new Vector2f(-0.06416279f, 0.17057902f),
                new Vector2f(-0.22691709f, 0.035993755f),
                new Vector2f(-0.16431928f, -0.114241004f),
                new Vector2f(0.054773092f, -0.15492952f),
                new Vector2f(0.117370844f, -0.014084458f)
            },
            { // Small1
                new Vector2f(0.06416273f, 0.1236307f),
                new Vector2f(0.0015649796f, 0.14241004f),
                new Vector2f(-0.08920187f, 0.09233177f),
                new Vector2f(-0.111111104f, -0.026604056f),
                new Vector2f(-0.026604056f, -0.09233177f),
                new Vector2f(0.06103289f, -0.101721406f),
                new Vector2f(0.114241004f, -0.014084458f),
                new Vector2f(0.1205008f, 0.06416279f)
            },
            { // Small2
                new Vector2f(0.07981217f, 0.101721466f),
                new Vector2f(0.029733896f, 0.12989044f),
                new Vector2f(-0.045383394f, 0.111111104f),
                new Vector2f(-0.15179968f, -0.057902932f),
                new Vector2f(-0.032863855f, -0.1267606f),
                new Vector2f(0.1205008f, -0.104851365f),
                new Vector2f(0.15805948f, 0.048513293f)
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
