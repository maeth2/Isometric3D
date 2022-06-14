package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
public class Helper {
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	/**
	 * Creates a OpenGL VAO
	 * 
	 * @return 		VAO ID
	 */
	public static int generateVAO() {
		int id = GL30.glGenVertexArrays();
		vaos.add(id);
		return id;
	}
	
	/**
	 * Creates a OpenGL VBO
	 * 
	 * @return 		VBO ID
	 */
	private static int generateVBO() {
		int id = GL30.glGenBuffers();
		vbos.add(id);
		return id;
	}
	
	/**
	 * Disposes of all VAO and VBO from video memory
	 */
	public static void dispose() {
		System.out.println("CLEANING UP!");
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos) {
			GL30.glDeleteVertexArrays(vbo);
		}
	}
	
	/**
	 * Creates a OpenGL VBO and stores indices as an element buffer
	 * 
	 * @param vaoID		VAO ID
	 * @param array		Integer array to be stored into VBO
	 * @return 			VBO ID
	 */
	public static int generateIndicesBuffer(int vaoID, int[] array) {
		GL30.glBindVertexArray(vaoID);
		int id = generateVBO();

		IntBuffer buffer = BufferUtils.createIntBuffer(array.length);
		buffer.put(array).flip();
		
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, id);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		
		GL30.glBindVertexArray(0);
		return id;
	}
	
	/**
	 * Stores Data into attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param size			Size of each group of data
	 * @param attribute		Attribute number to store data
	 * @param array			Float array to be stored into VBO
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int size, int attribute, float[] array) {
		GL30.glBindVertexArray(vaoID);
		int id = generateVBO();
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, id);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(attribute, size, GL30.GL_FLOAT, false, 0, 0);
		GL30.glBindVertexArray(0);
		
		return id;
	}
	
	/**
	 * Stores Data into attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param vaoID			VBO ID
	 * @param size			Size of each set of data
	 * @param groups		Attribute number and size of each sub-group in each group of data; 
	 * 						i[0] = attribute to store
	 * 						i[1] = size of sub-group
	 * @param array			Float array to be stored into VBO
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int vboID, int size, int[][] groups, float[] array) {
		GL30.glBindVertexArray(vaoID);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		GL30.glBindVertexArray(0);
		
		return vboID;
	}
	
	/**
	 * Stores Data into attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param size			Size of each set of data
	 * @param groups		Attribute number and size of each sub-group in each group of data; 
	 * 						i[0] = attribute to store
	 * 						i[1] = size of sub-group
	 * @param array			Float array to be stored into VBO
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int size, int[][] groups, float[] array) {
		GL30.glBindVertexArray(vaoID);
		int id = generateVBO();
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, id);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		GL30.glBindVertexArray(0);
		
		return id;
	}
	
	/**
	 * Allocates memory in attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param size			Size of each set of data
	 * @param groups		Attribute number and size of each sub-group in each group of data; 
	 * 						i[0] = attribute to store
	 * 						i[1] = size of sub-group
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int totalSize, int size, int[][] groups) {
		GL30.glBindVertexArray(vaoID);
		int id = generateVBO();
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, id);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, totalSize * Float.BYTES, GL30.GL_DYNAMIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		GL30.glBindVertexArray(0);
		
		return id;
	}
	
	/**
	 * Sets pointer for attribute list
	 * 
	 * @param vaoID				VAO ID
	 * @param attribute			Attribute number to store VBO
	 * @param size				Number of data put into each index of VBO
	 * @param skip				Number of data to skip in array
	 * @param offset			Number of data to skip at the start of reading
	 */
	public static void setAttributePointers(int vaoID, int attribute, int size, int skip, int offset) {
		GL30.glVertexAttribPointer(attribute, size, GL30.GL_FLOAT, false, (size + skip) * Float.BYTES, offset * Float.BYTES);
		GL30.glEnableVertexAttribArray(attribute);
	}
}
