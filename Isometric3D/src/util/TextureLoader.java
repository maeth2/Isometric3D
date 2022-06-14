package util;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;


public class TextureLoader {
	private static Map<Integer, Vector2f> dimensions = new HashMap<>();
	
	/**
	 * Load texture from file directory
	 * 
	 * @param filePath		Image directory
	 * @return				OpenGL texture ID
	 */
	public static int loadTexture(String filePath) {
		int texID = glGenTextures();
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
		
		
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		if(image != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			dimensions.put(texID, new Vector2f(width.get(0), height.get(0)));
		}else {
			glDeleteTextures(texID);
			texID = -1;
			System.out.println("Unable to load image: " + filePath);
		}
		
		stbi_image_free(image);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return texID;
	}
	
	/**
	 * Returns texture dimensions
	 * @param texture			Texture ID
	 * @return					Texture Dimensions
	 */
	public static Vector2f getDimensions(int texture) {
		if(!dimensions.containsKey(texture)) {
			return null;
		}
		return dimensions.get(texture);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param uniformName			Sampler2D uniform variable name
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void loadTextureToShader(int shaderID, String uniformName, int textureFile, int slot) {
		Shaders.loadTexture(shaderID, uniformName, slot);
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, textureFile);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void loadTextureToShader(int shaderID, int textureFile, int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, textureFile);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void unbindTexture(int shaderID, int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
