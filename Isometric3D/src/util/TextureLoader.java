package util;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;


public class TextureLoader {
	private static HashMap<Integer, Vector2f> dimensions = new HashMap<>();

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
	 * Generate blank texture
	 * 
	 * @param width				Width of textures
	 * @param frameBuffer		Frame Buffer ID
	 * @param height			Height of texture
	 * @param attachement		OpenGL color attachement
	 * @return					Texture ID
	 */
	public static int generateTexture(int frameBuffer, int width, int height, int attachment) {
		int texture = glGenTextures();
		dimensions.put(texture, new Vector2f(width, height));	
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, attachment, texture, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return texture;
	}
	
	/**
	 * Generate blank depth texture
	 * 
	 * @param width				Width of texture
	 * @param frameBuffer		Frame Buffer ID
	 * @param height			Height of texture
	 * @return					Texture ID
	 */
	public static int generateDepthTexture(int frameBuffer, int width, int height) {
		int texture = glGenTextures();
		dimensions.put(texture, new Vector2f(width, height));	
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return texture;
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
		ShaderLoader.loadTexture(shaderID, uniformName, slot);
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
	public static void bindTextureToShader(int shaderID, String name, int slot) {
		ShaderLoader.loadInt(shaderID, name, slot);
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
