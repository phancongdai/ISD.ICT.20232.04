package views.screen;

import java.io.IOException;
import java.util.Hashtable;

import controller.BaseController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.screen.home_admin.AdminHomeScreenHandler;

public class BaseScreenAdminHandler extends FXMLScreenHandler {

    private Scene scene;
    private BaseScreenAdminHandler prev;
    protected final Stage stage;
    public AdminHomeScreenHandler adminHomeScreenHandler;
    protected Hashtable<String, String> messages;
    private BaseController bController;

    private BaseScreenAdminHandler(String screenPath) throws IOException {
        super(screenPath);
        this.stage = new Stage();
    }

    public void setPreviousScreen(BaseScreenAdminHandler prev) {
        this.prev = prev;
    }

    public BaseScreenAdminHandler getPreviousScreen() {
        return this.prev;
    }

    public BaseScreenAdminHandler(Stage stage, String screenPath) throws IOException {
        super(screenPath);
        this.stage = stage;
    }

    public void show() {
        if (this.scene == null) {
            this.scene = new Scene(this.content);
        }
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void setScreenTitle(String string) {
        this.stage.setTitle(string);
    }

    public void setBController(BaseController bController){
        this.bController = bController;
    }

    public BaseController getBController(){
        return this.bController;
    }

    public void forward(Hashtable messages) {
        this.messages = messages;
    }

    public void setHomeScreenHandler(AdminHomeScreenHandler AdminHomeScreenHandler) {
        this.adminHomeScreenHandler = AdminHomeScreenHandler;
    }

}
