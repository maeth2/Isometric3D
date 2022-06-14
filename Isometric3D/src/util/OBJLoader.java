package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import components.Model;


public class OBJLoader {
	private static HashMap<String, Model> cachedModels = new HashMap<>();
	/**
	 * Loads an .OBJ file from system
	 * 
	 * @param file			file path
	 * @return				Raw model
	 */
	public static Model loadOBJModel(String file){//Loads obj file into textured model and raw model
		if(cachedModels.containsKey(file)) {
			return cachedModels.get(file);
		}
		
		FileReader fr = null;
		try {
			fr = new FileReader(new File(file));//Gets OBJ file
		} catch (FileNotFoundException e) {
			System.err.println("couldn't find file");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);//Puts file into buffered reader
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>(); //creates List for all the vertices ordered
		List<Vector2f> textures = new ArrayList<Vector2f>(); //creates List for all the texture coords unordered
		List<Integer> indices = new ArrayList<Integer>(); //creates List for all the indices ordered
		
		float[] verticesArray = null; //creates float array for all the vertices ordered
		float[] texturesArray = null; //creates float array for all the texture coords ordered
		int[] indicesArray = null; //creates float array for all the indices ordered
		
		try{
			while(true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");//splits line into sections based on spaces
				if(line.startsWith("v ")){ //checks if line is a vertice element holder
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3])); //converts text to float
					vertices.add(vertex); //places vertex into unordered vertice list
				}else if(line.startsWith("vt ")){ //checks if line is a texture coord element holder
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2])); //converts text into float
					textures.add(texture); //adds texture coord into unordered texture coord list;
				}else if(line.startsWith("f ")){ //checks if line is a face element holder
					texturesArray = new float[vertices.size()*2]; //sets size of ordered texture coord array
					break;
				}
			}		
			while(line != null){
				if(!line.startsWith("f ")){ //checks if line is not a face element holder
					line = reader.readLine(); // if line is not a indices holder continue
					continue;
				}
				String[] currentLine = line.split(" "); //splits face data into 3 vertices
				String[] vertex1 = currentLine[1].split("/"); //splits the data into vertices data
				String[] vertex2 = currentLine[2].split("/"); //splits the data into texture coord data
				String[] vertex3 = currentLine[3].split("/"); //splits the data into normals data

				processVertexData(vertex1, indices, textures, texturesArray); //connects vertex with corresponding texture coord and normal based on indice data
				processVertexData(vertex2, indices, textures, texturesArray); //connects vertex with corresponding texture coord and normal based on indice data
				processVertexData(vertex3, indices, textures, texturesArray); //connects vertex with corresponding texture coord and normal based on indice data
				line = reader.readLine(); //continue to next line
			}
			
			verticesArray = new float[vertices.size()*3];
			indicesArray = new int[indices.size()];
			
			int verticesNum = 0;
			for(Vector3f vertice : vertices){
				verticesArray[verticesNum++] = vertice.x;
				verticesArray[verticesNum++] = vertice.y;
				verticesArray[verticesNum++] = vertice.z;
			}
			
			for(int i = 0; i<indices.size(); i++){
				indicesArray[i] = indices.get(i);
			}
			reader.close(); //close reader
		}catch(Exception e){
			e.printStackTrace();
		}
		int vaoID = Helper.generateVAO();
		System.out.println(Arrays.toString(verticesArray));
		System.out.println(Arrays.toString(indicesArray));
		Helper.storeDataInAttributeList(vaoID, 3, 0, verticesArray);
		Helper.storeDataInAttributeList(vaoID, 2, 1, texturesArray);
		Helper.generateIndicesBuffer(vaoID, indicesArray);
		Model model = new Model(vaoID, indicesArray.length);
		cachedModels.put(file, model);
		return model; //Loads data into vao
	}
	
	private static void processVertexData(String[] vertexData, List<Integer> indices, List<Vector2f> textures, float[] texturesArray ){
		int vertexPointer = Integer.parseInt(vertexData[0])-1;
		indices.add(vertexPointer);
		
		Vector2f textureCoords = textures.get(Integer.parseInt(vertexData[1])-1);
		texturesArray[vertexPointer*2] = textureCoords.x;
		texturesArray[vertexPointer*2+1] = textureCoords.y;


	}
}
