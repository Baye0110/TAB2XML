package custom_model.rest;

public class HalfRest extends WholeRest{
	// Restore this class
	
	public HalfRest(double height) {
		super(height);
		this.setRotate(180);
		this.setTranslateY(height - this.height);
	}
}
