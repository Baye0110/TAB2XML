package custom_model.rest;

public class HalfRest extends WholeRest{
	
	public HalfRest(double height) {
		super(height);
		this.setRotate(180);
		this.setTranslateY(height - this.height);
	}
}
