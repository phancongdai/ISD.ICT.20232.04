package views.screen.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;

import java.io.IOException;

public class ResultScreenHandler extends BaseScreenHandler {

	private String result;
	private String message;

	//Data Coupling
	public ResultScreenHandler(Stage stage, String screenPath, String result, String message) throws IOException {
		super(stage, screenPath);
		resultLabel.setText(result);
		messageLabel.setText(message);
	}

	@FXML
	private Label pageTitle;

	@FXML
	private Label resultLabel;

	@FXML
	private Button okButton;
	
	@FXML
	private Label messageLabel;



	@FXML
	void confirmPayment(MouseEvent event) throws IOException {
		setHomeScreenHandler(new HomeScreenHandler(this.stage, Configs.HOME_PATH));
		homeScreenHandler.show();

	}

}
