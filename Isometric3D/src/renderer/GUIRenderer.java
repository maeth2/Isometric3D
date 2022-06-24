package renderer;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

import components.GUIComponent;
import util.AssetManager;
import util.ShaderLoader;
import util.TextureLoader;

public class GUIRenderer {
	private static GUIRenderer instance;
	private int GUIShaderID;

	public GUIRenderer() {
		this.GUIShaderID = AssetManager.getShader("assets/shaders/GUI");
	}
	
	public void render(List<GUIComponent> guis) {
		ShaderLoader.useShader(GUIShaderID);
		
		for(GUIComponent g : guis) {
			ShaderLoader.loadMatrix(GUIShaderID, "uTransformation", g.getTransformMatrix());
			renderQuad(GUIShaderID, g.getTexture());
		}
		

		ShaderLoader.unbindShader();
	}
	
	public void renderQuad(int shader, int texture) {
		glBindVertexArray(GUIComponent.getVaoID());
		glEnableVertexAttribArray(0); 
		
		TextureLoader.loadTextureToShader(shader, texture, 0);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, GUIComponent.vertices.length);
		TextureLoader.unbindTexture(shader, 0);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	public static GUIRenderer get() {
		if(instance == null) {
			instance = new GUIRenderer();
		}
		return instance;
	}
	
}
