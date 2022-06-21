package components;

public class Material {
	int textureID;
	float reflectance;
	
	public Material() {
		init(0, 1);
	}
	
	public Material(int textureID) {
		init(textureID, 1);
	}
	
	public Material(float reflectance) {
		init(0, reflectance);
	}
	
	public Material(int textureID, float reflectance) {
		init(textureID, reflectance);
	}
	
	public void init(int textureID, float reflectance) {
		this.textureID = textureID;
		this.reflectance = reflectance;
	}

	public int getTextureID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}
	
}
