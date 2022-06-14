package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
	public static Map<String, Integer> textures = new HashMap<>();
	public static Map<String, Integer> shaders = new HashMap<>();
	
	/**
	 * Loads texture from assets
	 * 
	 * @param resourceName			Image file path
	 * @return						Texture ID
	 */
	public static int getTexture(String resourceName) {
		File file = new File(resourceName);
		String path = file.getAbsolutePath();
		if(textures.containsKey(path)) {
			return textures.get(path);
		}else {
			int textureID = TextureLoader.loadTexture(resourceName);
			if(textureID > -1) {
				textures.put(path, textureID);
			}
			return textureID;
		}
	}
	
	/**
	 * Loads shader from assets
	 * 
	 * @param resourceName			Shader file path
	 * @return						Shader ID
	 */
	public static int getShader(String resourceName) {
		File file = new File(resourceName);
		String path = file.getAbsolutePath();
		if(shaders.containsKey(path)) {
			return shaders.get(path);
		}else {
			int shaderID = Shaders.buildShader(resourceName);
			if(shaderID > -1) {
				shaders.put(path, shaderID);
			}
			return shaderID;
		}
	}
}
