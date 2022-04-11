package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class NameChoosingController{

	private PreviewController pc;
    
    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private Button cancelButton;
    @FXML private Button applyButton; 
    
    
    public void setPreviewController(PreviewController pcInput) {
		 this.pc = pcInput;
	}
    
    @FXML
    private void applyButtonClicked() {
    	if (!this.titleField.getText().isBlank()) {
    		this.pc.sheet.setTitle(this.titleField.getText());
    	}
    	if (!this.artistField.getText().isBlank()) {
    		this.pc.sheet.setAuthor(this.artistField.getText());
    	}
    	
    	this.cancelButtonClicked();
    }
    
    
    @FXML
    private void cancelButtonClicked() {
    	pc.displayWindow.hide();
    }
}
