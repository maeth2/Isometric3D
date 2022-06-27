package renderer;

import java.util.List;

import components.GUIComponent;
import util.Quad;

public class GUIRenderer {
	private static GUIRenderer instance;
	
	public void render(List<GUIComponent> guis) {		
		for(GUIComponent g : guis) {
			Quad.renderQuad(g.getTexture(), g.getTransformMatrix());
		}
	}
	
	public static GUIRenderer get() {
		if(instance == null) {
			instance = new GUIRenderer();
		}
		return instance;
	}
	
}
