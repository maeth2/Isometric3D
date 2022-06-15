package main;

import org.joml.Vector3f;

public class Transform {
	public Vector3f position;
	public float scale;	
	public Vector3f rotation;	
	
	public Transform() {
		init(new Vector3f(), new Vector3f(), 1);
	}
	
	public Transform(float scale) {
		init(new Vector3f(), new Vector3f(), scale);
	}
	
	public Transform(Vector3f position) {
		init(position, new Vector3f(), 1);
	}
	
	public Transform(Vector3f position, Vector3f rotation) {
		init(position, rotation, 1);
	}
	
	public Transform(Vector3f position, float scale) {
		init(position, new Vector3f(), scale);
	}
	
	public Transform(Vector3f position, Vector3f rotation, float scale) {
		init(position, rotation, scale);
	}
	
	public void init(Vector3f position, Vector3f rotation, float scale) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public Transform copy() {
		Transform t = new Transform(new Vector3f(this.position), this.scale);
		return t;
	}
	
	public void copy(Transform to) {
		to.position.set(this.position);
		to.scale = this.scale;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof Transform)) return false;
		
		Transform t = (Transform) o;
		return t.position.equals(this.position) && t.scale == this.scale;
	}
}
