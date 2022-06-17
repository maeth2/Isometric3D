package components;

public class TexturedModelRendererComponent extends Component{
	private Model model;
	private Material material;
	
	/**
	 * Constructs component
	 * 
	 * @param model			Model vertex data
	 */
	public TexturedModelRendererComponent(Model model) {
		init(model, null);
	}
	
	/**
	 * Constructs component
	 * 
	 * @param model			Model vertex data
	 * @param material		Model material data
	 */
	public TexturedModelRendererComponent(Model model, Material material) {
		init(model, material);
	}
	
	/**
	 * Initializes component
	 * 
	 * @param model			Model vertex data
	 * @param material		Model material data
	 */
	private void init(Model model, Material material) {
		this.model = model;
		this.material = material;
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
	
	public Material getMaterial() {
		return this.material;
	}
}
