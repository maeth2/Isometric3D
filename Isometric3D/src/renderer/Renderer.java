package renderer;

import java.util.ArrayList;
import java.util.List;

import components.TexturedModelRenderer;
import main.GameObject;

public class Renderer {
	private List<RenderBatch> batches; 
	
	/**
	 * Initializer
	 */
	public Renderer() {
		this.batches = new ArrayList<>();
	}
	
	/**
	 * Add a Game Object to the rendering list
	 * 
	 * @param gameObject 		GameObject to add
	 */
	public void add(GameObject gameObject) {
		TexturedModelRenderer model = gameObject.getComponent(TexturedModelRenderer.class);
		if(model != null) {
			add(model);
		}
	}
	
	/**
	 * Add a Game Object to the rendering list
	 * 
	 * @param gameObject 		GameObject to add
	 */
	public void add(TexturedModelRenderer gameObject) {
		boolean added = false;
		for(RenderBatch b : batches) {
			if(b.getBatchModel().getVaoID() == gameObject.getModel().getVaoID()) {
				b.add(gameObject);
				added = true;
				break;
			}
		}
		if(!added) {
			RenderBatch newBatch = new RenderBatch(gameObject.getModel());
			addBatch(newBatch);
			newBatch.add(gameObject);
		}
	}
	
	/**
	 * Add a Render Batch to render list
	 * 
	 * @param b			RenderBatch to add
	 */
	public void addBatch(RenderBatch b) {
		batches.add(b);
	}
	
	public void render() {
		for(RenderBatch b : batches) {
			b.render();
		}
	}
}
