package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL30.*;

public class Helper {
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	/**
	 * Creates an OpenGL VAO
	 * 
	 * @return 		VAO ID
	 */
	public static int generateVAO() {
		int id = glGenVertexArrays();
		vaos.add(id);
		return id;
	}
	
	/**
	 * Creates an OpenGL VBO
	 * 
	 * @return 		VBO ID
	 */
	private static int generateVBO() {
		int id = glGenBuffers();
		vbos.add(id);
		return id;
	}
	
	/**
	 * Creates a OpenGL VBO and stores indices as an element buffer
	 * 
	 * @param vaoID		VAO ID
	 * @param array		Integer array to be stored into VBO
	 * @return 			VBO ID
	 */
	public static int generateIndicesBuffer(int vaoID, int[] array) {
		glBindVertexArray(vaoID);
		int id = generateVBO();

		IntBuffer buffer = BufferUtils.createIntBuffer(array.length);
		buffer.put(array).flip();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		glBindVertexArray(0);
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
		glBindVertexArray(vaoID);
		int id = generateVBO();
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		glBindVertexArray(0);
		
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
		glBindVertexArray(vaoID);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		glBindVertexArray(0);
		
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
		glBindVertexArray(vaoID);
		int id = generateVBO();
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		glBindVertexArray(0);
		
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
		glBindVertexArray(vaoID);
		int id = generateVBO();
		
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, totalSize * Float.BYTES, GL_DYNAMIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		glBindVertexArray(0);
		
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
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, (size + skip) * Float.BYTES, offset * Float.BYTES);
		glEnableVertexAttribArray(attribute);
	}
	
	
	/**
	 * Cleans up all VAOs and VBOs from memory
	 */
	public static void dispose() {
		System.out.println("CLEANING UP!");
		for(int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos) {
			glDeleteVertexArrays(vbo);
		}
	}
}
