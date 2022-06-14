package renderer;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

import components.Model;
import components.TexturedModelRenderer;
import main.Window;
import util.AssetLoader;
import util.Maths;
import util.Shaders;
import util.TextureLoader;

public class RenderBatch {
	private Model batchModel;
	private int shaderID;
	private HashMap<TexturedModelRenderer, Integer> models = new HashMap<>();
	private ArrayList<Integer> textures = new ArrayList<>();
	private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

	public RenderBatch(Model batchModel) {
		this.shaderID = AssetLoader.getShader("assets/shaders/default");
		this.batchModel = batchModel;
	}
	
	public void add(TexturedModelRenderer t) {
		int texID = 0;
		if(!hasTexture(t.getTexture().getID())) {
			textures.add(t.getTexture().getID());
			texID = textures.size();
		}else {
			for(int i = 0; i < textures.size(); i++) {
				if(textures.get(i) == t.getTexture().getID()) {
					texID = i + 1;
				}
			}
		}
		models.put(t, texID);
	}
	
	public boolean hasRoom() {
		return textures.size() < 8;
	}
	public boolean hasTexture(int texture) {
		return textures.contains(texture);
	}
	
	public void render() {
		Shaders.useShader(shaderID);
		Shaders.loadMatrix(shaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		Shaders.loadMatrix(shaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		Shaders.loadIntArray(shaderID, "uTextures", texSlots);
		
		for(int i = 0; i < textures.size(); i++) {
			TextureLoader.loadTextureToShader(shaderID, textures.get(i), i + 1);
		}
		
		// Bind the VAO
		glBindVertexArray(batchModel.getVaoID());
		// Enable the vertex attribute pointers
		glEnableVertexAttribArray(0); 
		glEnableVertexAttribArray(1);

		for(TexturedModelRenderer m : models.keySet()) {
			Shaders.loadMatrix(shaderID, "uTransformation", Maths.createTransformationalMatrix(m.gameObject.transform));
			Shaders.loadInt(shaderID, "texID", models.get(m));
			glDrawElements(GL_TRIANGLES, batchModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		}
		
		//Unbind
		Shaders.unbindShader();
		for(int i = 0; i < textures.size(); i++) {
			TextureLoader.unbindTexture(shaderID, i + 1);
		}
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}
	
	public Model getBatchModel() {
		return this.batchModel;
	}
}
