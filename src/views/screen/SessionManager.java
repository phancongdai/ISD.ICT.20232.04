package views.screen;

//Lớp này tôi dùng để quanr lí trạng thái đăng nhập của người
public class SessionManager {
    //Biến dùng để lưu trạng thái đăng nhập của người dùng. false là chưa đăng nhập và ngược lại
    private static boolean isLoggedIn = false;
    //Phương thức dungf để kểm tra người dùng có đăng nhập hay không?
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    //Phương thức dùng để chuyển trạng thái đăng nhập
    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}

