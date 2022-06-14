package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;

public class Shaders {
	
	/**
	 * Find and build GLSL shader program from directory
	 * 
	 * @param path		Path to shader folder
	 * @return			Shader program ID
	 */
	public static int buildShader(String path) {
		int vertexID = 0, fragmentID = 0, shaderID;
		
		File file = new File(path);
		for(File f : file.listFiles()) {
			String p = f.getPath();
			String type = p.substring(p.length() - 4);
			if(type.equals("vert")) {
				vertexID = loadShader(p, GL_VERTEX_SHADER);
			}else if(type.equals("frag")) {
				fragmentID = loadShader(p, GL_FRAGMENT_SHADER);
			}
		}
		
		shaderID = glCreateProgram();
		glAttachShader(shaderID, vertexID);
		glAttachShader(shaderID, fragmentID);
		glLinkProgram(shaderID);
		
		if(glGetProgrami(shaderID, GL_LINK_STATUS) == GL_FALSE) {
			System.out.println("LINKING UNSUCCESSFUL!");
			int len = glGetProgrami(shaderID, GL_INFO_LOG_LENGTH);
			System.out.println(glGetProgramInfoLog(shaderID, len));
			glDeleteShader(shaderID);
			shaderID = -1;
		}else {
			System.out.println("LINKING SUCCESSFUL!");
		}
		
		return shaderID;
	}
	
	/**
	 * Find and load GLSL shader
	 * 
	 * @param file		File path of shader code
	 * @param type		OpenGL specified type of shader
	 * @return			Shader ID
	 */
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch (IOException e){
			System.err.println("Could not read file");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = glCreateShader(type); // Creates a shader object
		glShaderSource(shaderID, shaderSource); // Inputs shader code into shader object
		glCompileShader(shaderID); // Compiles shader code into shader object
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){ //Checks and prints any compilation errors
			System.out.println(glGetShaderInfoLog(shaderID,500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}
		return shaderID;
	}
	
	/**
	 * Binds the attribute number to shader variable
	 * 
	 * @param programID			Shader program ID
	 * @param attribute			Attribute number to bind to
	 * @param variableName		Variable name to bind to
	 */
	public static void bindAttribute(int programID, int attribute, String variableName){ 
		glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Uploads Matrix4f into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param matrix			Matrix4f to upload
	 */
	public static void loadMatrix(int shaderID, String name, Matrix4f matrix){
		int location = glGetUniformLocation(shaderID, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.get(buffer);
		glUniformMatrix4fv(location, false, buffer);
	}
	
	/**
	 * Uploads Integer into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param integer			Integer to upload	 */
	public static void loadInt(int shaderID, String name, int integer) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1i(location, integer);
	}
	
	/**
	 * Uploads Integer into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param array				Array to upload	 
	 */
	public static void loadIntArray(int shaderID, String name, int[] array) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1iv(location, array);
	}
	
	/**
	 * Uploads texture into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param texture			Texture to upload
	 */
	public static void loadTexture(int shaderID, String name, int texture) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1i(location, texture);
	}
	
	/**
	 * Binds and uses shader
	 * @param shaderID			Shader program ID
	 */
	public static void useShader(int shaderID) {
		glUseProgram(shaderID);
	}
	
	/**
	 * Detaches shader from program
	 */
	public static void unbindShader() {
		glUseProgram(0);
	}
}
