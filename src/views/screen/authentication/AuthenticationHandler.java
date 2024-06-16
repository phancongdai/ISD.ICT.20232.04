
package views.screen.authentication;

import controller.LoginController;
import entity.db.AIMSDB;
import entity.user.User;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.home.HomeScreenHandler;
import views.screen.home_admin.AdminHomeScreenHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class AuthenticationHandler implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password;

    @FXML
    private CheckBox login_selectshowPassword;

    @FXML
    private TextField login_showPassword;

    @FXML
    private TextField login_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button signup_btn;

    @FXML
    private PasswordField signup_cPassword;

    @FXML
    private TextField signup_email;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private Button signup_loginAccount;

    @FXML
    private PasswordField signup_password;

    @FXML
    private TextField signup_username;

    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private LoginController loginController;

    public void login() throws IOException {
        if (loginController == null) loginController = new LoginController();

        alertMessage alert = new alertMessage();
        if (login_username.getText().isEmpty() ||
                (login_selectshowPassword.isSelected() && login_showPassword.getText().isEmpty()) ||
                (!login_selectshowPassword.isSelected() && login_password.getText().isEmpty())) {
            alert.errorMessage("Username and Password are Required");

        } else {
            try {
                User user = loginController.loginWithUsernameAndPassword(login_username.getText(), login_password.getText());
                if (user == null) {
                    alert.errorMessage("Incorrect Username/Password");
                } else {
                    Stage stage = (Stage) login_btn.getScene().getWindow();
                    if (user.getId() != 0) {
                        HomeScreenHandler homeHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
                        homeHandler.setScreenTitle("Home Screen");
                        homeHandler.setImage();
                        homeHandler.show();
                    } else {
                        AdminHomeScreenHandler homeHandler = new AdminHomeScreenHandler(stage, Configs.HOME_ADMIN_PATH);
                        homeHandler.setScreenTitle("Home Screen");
                        homeHandler.setImage();
                        homeHandler.show();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void register() {

        alertMessage alert = new alertMessage();

        //Kiểm tra xem nếu để trường dữ liệu nào để trống
        if (signup_email.getText().isEmpty() || signup_username.getText().isEmpty()
                || signup_password.getText().isEmpty() || signup_cPassword.getText().isEmpty()) {
            alert.errorMessage("All fields are neccessary to be filled !");
        } //Kiểm tra mật khẩu đã khớp với mật khẩu confirm hay chưa
        else if (signup_cPassword.getText() == signup_password.getText()) {
            alert.errorMessage("Password does not match !");
        } //Kiểm tra độ dài mật khẩu hợp lệ hay chưa ?
        else if (signup_password.getText().length() < 8) {
            alert.errorMessage("Invalid Password, at least 8 characters or more !");
        } else {
            Connection connect = AIMSDB.getConnection();
            try {
                //Kiểm tra xem nếu tài khoản đã tồn tại rồi !
                String checkUsername = "SELECT * FROM User WHERE name = '"
                        + signup_username.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkUsername);
                if (result.next()) {
                    alert.errorMessage(signup_username.getText() + " is already existed !");
                } else {
                    String insertData = "INSERT INTO User (email, name, password) VALUES (?, ?, ?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, signup_email.getText());
                    prepare.setString(2, signup_username.getText());
                    prepare.setString(3, signup_password.getText());
                    prepare.executeUpdate();

                    alert.successMessage("Registered Successfully !!");
                    registerClearFields();

                    signup_form.setVisible(false);
                    login_form.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Xóa sạch form đăng kí đi
    public void registerClearFields() {
        signup_username.setText("");
        signup_email.setText("");
        signup_cPassword.setText("");
        signup_password.setText("");
    }

    //Chuyển màn hình code tại đây !!
    public void switchForm(ActionEvent e) {
        if (e.getSource() == signup_loginAccount) {
            //Chuyển màn từ đăng kí sang đăng nhập
            signup_form.setVisible(false);
            login_form.setVisible(true);
        } else if (e.getSource() == login_createAccount) {
            //Chuyển màn từ đăng nhập sang đăng kí
            signup_form.setVisible(true);
            login_form.setVisible(false);
        }
    }

    public void showPassword() {
        if (login_selectshowPassword.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    public void openApp() {
        login_form.setVisible(true);
        signup_form.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openApp();
    }

}

