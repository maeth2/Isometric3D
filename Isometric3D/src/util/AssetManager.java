package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import components.Model;

import static org.lwjgl.opengl.GL30.*;

public class AssetManager {
	public static Map<String, Integer> textures = new HashMap<>();
	public static Map<String, Integer> shaders = new HashMap<>();
	public static Map<String, Model> models = new HashMap<>();
	
	/**
	 * Loads texture from system
	 * 
	 * @param filePath				Image file path
	 * @return						Texture ID
	 */
	public static int getTexture(String filePath) {
		String file = new File(filePath).getAbsolutePath();
		if(textures.containsKey(file)) {
			return textures.get(file);
		}else {
			int textureID = TextureLoader.loadTexture(file);
			if(textureID > -1) {
				textures.put(file, textureID);
			}
			return textureID;
		}
	}
	
	/**
	 * Generates blank texture
	 * 
	 * @param name				Name of texture
	 * @param width				Width of texture
	 * @param height			Height of texture
	 * @param frameBuffer		Frame Buffer ID
	 * @return					Texture ID
	 */
	public static int generateTexture(String name, int width, int height, int frameBuffer) {
		int id = TextureLoader.generateTexture(width, height, frameBuffer);
		textures.put(name, id);
		return id;
	}

	/**
	 * Generates blank depth texture
	 * 
	 * @param name				Name of texture
	 * @param width				Width of texture
	 * @param height			Height of texture
	 * @param frameBuffer		Frame Buffer ID
	 * @return					Texture ID
	 */
	public static int generateDepthTexture(String name, int width, int height, int frameBuffer) {
		int id = TextureLoader.generateDepthTexture(width, height, frameBuffer);
		textures.put(name, id);
		return id;
	}
	
	/**
	 * Loads shader from assets
	 * 
	 * @param filePath				Shader file path
	 * @return						Shader ID
	 */
	public static int getShader(String filePath) {
		String file = new File(filePath).getAbsolutePath();
		if(shaders.containsKey(file)) {
			return shaders.get(file);
		}else {
			int shaderID = ShaderLoader.buildShader(file);
			if(shaderID > -1) {
				shaders.put(file, shaderID);
			}
			return shaderID;
		}
	}
	
	/**
	 * Loads shader from assets
	 * 
	 * @param filePath				OBJ model file path
	 * @return						Raw model
	 */
	public static Model getModel(String filePath) {
		String file = new File(filePath).getAbsolutePath();
		if(models.containsKey(file)) {
			return models.get(file);
		}else {
			Model model = OBJLoader.loadOBJModel(file);
			models.put(file, model);
			return model;
		}
	}

	/**
	 * Cleans up all textures and shaders from memory
	 */
	public static void dispose() {
		for(String s : shaders.keySet()) {
			glDeleteShader(shaders.get(s));
		}
		for(String t : textures.keySet()) {
			glDeleteTextures(textures.get(t));
		}
	}
}
