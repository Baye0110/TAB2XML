package custom_model.rest;

public class HalfRest extends WholeRest{
	// Creates a Rest with the duration of a half note
	// Extends the whole rest, because this is simply just an up-side down version of WholeRest.
	
	public HalfRest(double height) {
		super(height);
		this.setRotate(180);
		this.setTranslateY(height - this.height);
	}
}
