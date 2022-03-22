import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    protected TextArea msgField;

    @FXML
    protected TextField sendInput;

    @FXML
    protected Button sendButton;

    @FXML
    protected Button exitButton;

    @FXML
    protected TextArea currentOnline;

    @FXML
    protected Label usernameDisplay;

    @FXML
    void actionExitButton(ActionEvent event) {

    }

    @FXML
    void actionSendButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}