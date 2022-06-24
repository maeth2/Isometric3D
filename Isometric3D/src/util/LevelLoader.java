package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import scenes.Scene;

public class LevelLoader {
	
	/**
	 * Loads level txt file into game
	 * 
	 * @param path			Directory to the file
	 * @param scene			Scene to load into
	 */
	public static void loadLevel(String path, Scene scene){
		File file = new File(path);
		try {
			int width;
			int height;
			String name;
	        Scanner sc;
			sc = new Scanner(file);
			name = sc.nextLine();
	        System.out.println(name);
	        width = sc.nextInt();
	        height = sc.nextInt();
	        for(int r = height - 1; r >= 0; r--) {
	        	for(int c = 0; c < width; c++) {
	        		int i = sc.nextInt();
	        		if(i == 0) continue;
	        		
	        		//TODO
	        		//Implement adding object
	        		
	        		//scene.addGameObjectToScene(t);
	        	}
	        }
	        for(int r = height - 1; r >= 0; r--) {
	        	for(int c = 0; c < width; c++) {
	        		int i = sc.nextInt();
	        		if(i == 0) continue;
	        		//TODO
	        		//Implement adding object
	        		
	        		//scene.addGameObjectToScene(t);
	        	}
	        }
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
