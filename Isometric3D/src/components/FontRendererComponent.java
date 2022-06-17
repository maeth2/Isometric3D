package components;

public class FontRendererComponent extends Component {
	
	private boolean firstTime = true;

	@Override
	public void start() {
		System.out.println(TexturedModelRendererComponent.class + " ATTACHED TO " + gameObject.getName());
		if(gameObject.getComponent(TexturedModelRendererComponent.class) != null) {
			System.out.println("FOUND " + TexturedModelRendererComponent.class + " IN " + gameObject.getName());
			System.out.println("CREATING " + this.getClass() + "...");
		}
	}
	
	@Override
	public void update(float dt) {
		if(firstTime) {
			System.out.println("UPDATING " + this.getClass() + "!");
			firstTime = false;
		}
	}

}
