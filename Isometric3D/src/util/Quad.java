package util;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;

public class Quad {
	
	public static float vertices[] = {
			-1, 1,
			-1, -1,
			1, 1,
			1, -1
	};
	
	private static int vaoID;
	
	private static int shaderID;
	
	public static void renderQuad(int texture) {
		ShaderLoader.useShader(getShaderID());
		renderQuad(getShaderID(), texture, null);
		ShaderLoader.unbindShader();
	}
	
	public static void renderQuad(int shader, int texture) {
		renderQuad(shader, texture, null);
	}
	
	public static void renderQuad(int texture, Matrix4f transformation) {
		ShaderLoader.useShader(getShaderID());
		renderQuad(getShaderID(), texture, transformation);
		ShaderLoader.unbindShader();
	}
	
	public static void renderQuad(int shader, int texture, Matrix4f transformation) {
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0); 
		
		if(transformation != null) {
			ShaderLoader.loadMatrix(shader, "uTransformation", transformation);
		}

		TextureLoader.loadTextureToShader(shader, texture, 0);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.length);
		TextureLoader.unbindTexture(shader, 0);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	public static void renderQuad(int shader, int[] texture) {
		renderQuad(shader, texture, null);
	}
	
	public static void renderQuad(int shader, int[] texture, Matrix4f transformation) {
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0); 
		
		if(transformation != null) {
			ShaderLoader.loadMatrix(shader, "uTransformation", transformation);
		}

		for(int i = 0; i < texture.length; i++) {
			TextureLoader.loadTextureToShader(shader, texture[i], i);
		}
		glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.length);
		TextureLoader.unbindTexture(shader, 0);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	public static int getVaoID() {
		if(vaoID == 0) {
			vaoID = Helper.generateVAO();
			Helper.storeDataInAttributeList(vaoID, 2, 0, vertices);
		}
		return vaoID;
	}
	
	public static int getShaderID() {
		if(shaderID == 0) {
			shaderID = AssetManager.getShader("assets/shaders/quad");
		}
		return shaderID;
	}
}
