package sample.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Class.ActionDataBase;
import sample.Class.HashPass;
import sample.Class.Student;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {
    @FXML
    private TextField signEmail,signName,signID;
    @FXML
    private PasswordField signPass,signCPass;
    @FXML
    private TextField loginID,loginPass;

    @FXML
    private Text target,baoLoi;
    @FXML
    private StackPane stackPane;
    private boolean verifyLogin,success = false;

    public static Student student;
    public ActionDataBase action = new ActionDataBase();

    public static Student getStudent() {
        return student;
    }

    public static void setStudent() {
        ControllerLogin.student = new Student();
    }

    public boolean isVerifyLogin() {
        return verifyLogin;
    }

    public void setVerifyLogin(boolean verifyLogin) {
        this.verifyLogin = verifyLogin;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void Summit(ActionEvent event) throws IOException {
        changeTop();
    }
    //public static Connect connect = new Connect();
    public void changeTop(){
        ObservableList<Node> list = this.stackPane.getChildren();
        if(list.size() > 0){
            Node topNode = list.get(list.size() -1);
            Node newNode = list.get(list.size()-2);
            topNode.setVisible(false);
            topNode.toBack();
            newNode.setVisible(true);
        }
    }


    public boolean signUp() throws Exception {
        if(signID.getText().equals("")||signPass.getText().equals("")
                ||signCPass.getText().equals("")){
            baoLoi.setFill(Color.RED);
            baoLoi.setText("Enter your account and password");
            return false;
        }
        else if(!signPass.getText().equals(signCPass.getText())){
            baoLoi.setFill(Color.RED);
            baoLoi.setText("Password does not match.");
            return false;
        }
        else if(action.haveUserName(signID.getText())){
            baoLoi.setFill(Color.RED);
            baoLoi.setText("Username " + signID.getText() + " is not available.");
            return false;
        }
        else {
            // insert data
            action.setSignUp(student,signID.getText(),signPass.getText(),
                    signName.getText(),signEmail.getText());
            action.insertData(student);
            return true;
        }
    }
    // vào màn hình chính
    public void enter(ActionEvent event) throws Exception {
        if(signUp() == true){
            setSceneHome(event);
        }
    }
    public void checkLogin() throws Exception {

        if(loginID.getText().equals("")||loginPass.getText().equals("")){
            verifyLogin = false;
        }else {
            String pass = action.checkLogin(loginID.getText());
            System.out.println(pass);
            HashPass hashPass = new HashPass();

            if(hashPass.checkPassword(loginPass.getText(),pass)){
                verifyLogin = true;
            }
            else {
                verifyLogin = false;
            }
        }
    }
    public void click(ActionEvent event) throws Exception {
        checkLogin();
        try{
            if(verifyLogin == false){
                Error();
            }
            if(verifyLogin == true){
               // Main.student.setUserName(loginID.getText());
                action.setData(loginID.getText(),student);
                action.insertState(student);
                //student.insertState();
                /*try{
                    Main.students[Main.dem].setPoint();
                } catch (SQLException e){
                    e.printStackTrace();
                }*/
                System.out.println(student.getFullName());
                setSceneHome(event);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setSceneHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/home.fxml"));
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1083,690);
        window.getIcons().add(new Image(getClass().getResourceAsStream("../book.png")));
        window.setTitle("English Application");
        window.setScene(scene);
        window.show();
    }
    //error login
    public void Error(){
        this.target.setFill(Color.RED);
        this.target.setText("User account or password is incorrect.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        student = new Student();
    }
}
