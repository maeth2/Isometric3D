package util;

import org.joml.Matrix4f;

import main.Transform;

public class Maths {
	public static Matrix4f createTransformationalMatrix(Transform transform){
		Matrix4f matrix = new Matrix4f();
		matrix.identity().translate(transform.position).
        	rotateX((float)Math.toRadians(transform.rotation.x)).
        	rotateY((float)Math.toRadians(transform.rotation.y)).
        	rotateZ((float)Math.toRadians(transform.rotation.z)).
        	scale(transform.scale);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Transform transform) {
		Matrix4f matrix = new Matrix4f();
		matrix = matrix.rotationX((float)Math.toRadians(transform.rotation.x))
		         .rotateY((float)Math.toRadians(transform.rotation.y))
		         .translate(-transform.position.x, -transform.position.y, -transform.position.z);
		return matrix;
	}
}
