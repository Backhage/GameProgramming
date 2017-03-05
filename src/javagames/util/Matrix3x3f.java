package javagames.util;

public class Matrix3x3f {

	private float[][] m = new float[3][3];
	
	public Matrix3x3f() {
		
	}
	
	public Matrix3x3f(float[][] m) {
		setMatrix(m);
	}
	
	public Matrix3x3f add(Matrix3x3f other) {
		return new Matrix3x3f(new float[][] {
			{ m[0][0] + other.m[0][0], m[0][1] + other.m[0][1], m[0][2] + other.m[0][2]	},
			{ m[1][0] + other.m[1][0], m[1][1] + other.m[1][1], m[1][2] + other.m[1][2]	},
			{ m[2][0] + other.m[2][0], m[2][1] + other.m[2][1], m[2][2] + other.m[2][2]	},
		});
	}

	public Matrix3x3f sub(Matrix3x3f other) {
		return new Matrix3x3f(new float[][] {
			{ m[0][0] - other.m[0][0], m[0][1] - other.m[0][1], m[0][2] - other.m[0][2]	},
			{ m[1][0] - other.m[1][0], m[1][1] - other.m[1][1], m[1][2] - other.m[1][2]	},
			{ m[2][0] - other.m[2][0], m[2][1] - other.m[2][1], m[2][2] - other.m[2][2]	},
		});
	}
	
	public Matrix3x3f mul(Matrix3x3f other) {
		return new Matrix3x3f(new float[][] {
			{ m[0][0]*other.m[0][0] + m[0][1]*other.m[1][0] + m[0][2]*other.m[2][0],
			  m[0][0]*other.m[0][1] + m[0][1]*other.m[1][1] + m[0][2]*other.m[2][1],
			  m[0][0]*other.m[0][2] + m[0][1]*other.m[1][2] + m[0][2]*other.m[2][2], },
			{ m[1][0]*other.m[0][0] + m[1][1]*other.m[1][0] + m[1][2]*other.m[2][0],
			  m[1][0]*other.m[0][1] + m[1][1]*other.m[1][1] + m[1][2]*other.m[2][1],
			  m[1][0]*other.m[0][2] + m[1][1]*other.m[1][2] + m[1][2]*other.m[2][2], },
			{ m[2][0]*other.m[0][0] + m[2][1]*other.m[1][0] + m[2][2]*other.m[2][0],
			  m[2][0]*other.m[0][1] + m[2][1]*other.m[1][1] + m[2][2]*other.m[2][1],
			  m[2][0]*other.m[0][2] + m[2][1]*other.m[1][2] + m[2][2]*other.m[2][2], },
			
		});
	}
	
	public void setMatrix(float[][] m) {
		this.m = m;
	}
	
	public static Matrix3x3f zero() {
		return new Matrix3x3f(new float[][] {
			{ 0.0f, 0.0f, 0.0f },
			{ 0.0f, 0.0f, 0.0f },
			{ 0.0f, 0.0f, 0.0f }
		});
	}
	
	public static Matrix3x3f identity() {
		return new Matrix3x3f(new float[][] {
			{ 1.0f, 0.0f, 0.0f },
			{ 0.0f, 1.0f, 0.0f },
			{ 0.0f, 0.0f, 1.0f },
		});
	}
	
	public static Matrix3x3f translate(Vector2f v) {
		return translate(v.x, v.y);
	}
	
	public static Matrix3x3f translate(float x, float y) {
		return new Matrix3x3f(new float[][] {
			{ 1.0f, 0.0f, 0.0f },
			{ 0.0f, 1.0f, 0.0f },
			{   x,    y,  1.0f }
		});
	}
	
	public static Matrix3x3f scale(Vector2f v) {
		return scale(v.x, v.y);
	}
	
	public static Matrix3x3f scale(float x, float y) {
		return new Matrix3x3f(new float[][] {
			{    x, 0.0f, 0.0f },
			{ 0.0f,    y, 0.0f },
			{ 0.0f, 0.0f, 1.0f }
		});
	}
	
	public static Matrix3x3f shear(Vector2f v) {
		return shear(v.x, v.y);
	}
	
	public static Matrix3x3f shear(float x, float y) {
		return new Matrix3x3f(new float[][] {
			{ 1.0f,    y, 0.0f },
			{    x, 1.0f, 0.0f },
			{ 0.0f, 0.0f, 1.0f }
		});
	}
	
	public static Matrix3x3f rotate(float rad) {
		return new Matrix3x3f(new float[][] {
			{ (float) Math.cos(rad), (float)Math.sin(rad), 0.0f },
			{ (float)-Math.sin(rad), (float)Math.cos(rad), 0.0f	},
			{                  0.0f,                 0.0f, 1.0f }
		});
	}
	
	public Vector2f mul(Vector2f vec) {
		return new Vector2f(
				vec.x * m[0][0] + vec.y * m[1][0] + vec.w * m[2][0],
				vec.x * m[0][1] + vec.y * m[1][1] + vec.w * m[2][1],
				vec.x * m[0][2] + vec.y * m[1][2] + vec.w * m[2][2]);
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			buf.append("[");
			buf.append(m[i][0]);
			buf.append(",\t");
			buf.append(m[i][1]);
			buf.append(",\t");
			buf.append(m[i][2]);
			buf.append("]\n");
		}
		return buf.toString();
	}
}
