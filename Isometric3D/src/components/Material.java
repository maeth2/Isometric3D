package components;

public class Material {
	int textureID;
	float reflectance;
	
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
		return this.textureID;
	}
	
	public float getReflectance() {
		return this.reflectance;
	}
}
