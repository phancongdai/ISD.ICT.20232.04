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
	//Data Coupling
	public ResultScreenHandler(Stage stage, String screenPath, String result, String message, String id, String bkcode, String transid, String am, String transstatus, String transdate) throws IOException {
		super(stage, screenPath);
		resultLabel.setText(result);
		messageLabel.setText(message);
		invoiceid.setText(id);
		bankcode.setText(bkcode);
		transactionid.setText(transid);
		amount.setText(String.valueOf(String.format("%.3f",Double.parseDouble(am)) + " đ"));
		transactionstatus.setText(transstatus);
		String date =transdate.substring(6,8) +"/"+transdate.substring(4,6)+"/"+transdate.substring(0,4)+ " at " + transdate.substring(8,10)+":"+transdate.substring(10,12)+":"+transdate.substring(12,14);
		transactiondate.setText(date);
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
	private Label invoiceid;

	@FXML
	private Label bankcode;
	@FXML
	private Label transactionid;
	@FXML
	private Label amount;

	@FXML
	private Label transactionstatus;

	@FXML
	private Label transactiondate;

	@FXML
	void confirmPayment(MouseEvent event) throws IOException {
		HomeScreenHandler homeHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
		homeHandler.setScreenTitle("Home Screen");
		homeHandler.setImage();
		homeHandler.show();
	}

}
