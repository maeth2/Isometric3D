package components;

public class TexturedModelRenderer extends Component{
	private Model model;
	private ModelTexture texture;
	
	/**
	 * Constructs component
	 * 
	 * @param sprite		Sprite object
	 */
	public TexturedModelRenderer(Model model) {
		init(model, null);
	}
	
	/**
	 * Constructs component
	 * 
	 * @param color			Vector4f color
	 * @param sprite		Sprite object
	 */
	public TexturedModelRenderer(Model model, ModelTexture texture) {
		init(model, texture);
	}
	
	/**
	 * Initializes component
	 * 
	 * @param color			
	 * @param sprite
	 */
	private void init(Model model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	@Override
	public void start() {
	}
	
	@Override
	public void update(float dt) {

	}
	
	public Model getModel() {
		return this.model;
	}
	
	public ModelTexture getTexture() {
		return this.texture;
	}
}
