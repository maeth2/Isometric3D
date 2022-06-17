package renderer;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

import components.Model;
import components.TexturedModelRendererComponent;
import util.Maths;
import util.Shaders;
import util.TextureLoader;

public class RenderBatch {
	private Model batchModel;
	private int shaderID;
	private ArrayList<TexturedModelRendererComponent> models = new ArrayList<TexturedModelRendererComponent>();

	public RenderBatch(int shaderID, Model batchModel) {
		this.shaderID = shaderID;
		this.batchModel = batchModel;
	}
	
	public void add(TexturedModelRendererComponent t) {
		models.add(t);
	}

	public void render() {				
		glBindVertexArray(batchModel.getVaoID());
		glEnableVertexAttribArray(0); 
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);


		for(TexturedModelRendererComponent m : models) {
			TextureLoader.loadTextureToShader(shaderID, m.getMaterial().getTextureID(), 0);
			Shaders.loadMatrix(shaderID, "uTransformation", Maths.createTransformationalMatrix(m.gameObject.transform));
			Shaders.loadMaterial(shaderID, "uMaterial", m.getMaterial());
			glDrawElements(GL_TRIANGLES, batchModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		}
		
		TextureLoader.unbindTexture(shaderID, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	public Model getBatchModel() {
		return this.batchModel;
	}
}
