 package main;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import components.Controller;
import util.Maths;

public class Camera extends GameObject{
	private static final float FOV = 15;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 1000;
	private Matrix4f projectionMatrix;
	private GameObject target;
	
	public Camera(Vector3f position) {
		super("Camera");
		target = this;
		this.transform.position = position;
		this.projectionMatrix = new Matrix4f();
		this.addComponent(new Controller());
		adjustProjection();
	}
	
	@Override
	public void update(float dt) {
		if(target.getComponent(Controller.class) != null) {
			target.getComponent(Controller.class).updateController(dt);
		}
		this.setPosition(target.transform.position);
		//System.out.println(this.transform.position.x + ", " + this.transform.position.y + ", " + this.transform.position.z);
	}
	
	public void adjustProjection() {
		projectionMatrix = new Matrix4f().perspective(FOV, 920f/900f, NEAR_PLANE, FAR_PLANE);
	}
	
	public Matrix4f getViewMatrix() {
		return Maths.createViewMatrix(this.transform);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setPosition(Vector3f position) {
		this.transform.position.x = position.x;
		this.transform.position.y = position.y;
		this.transform.position.z = position.z;
	}
}
