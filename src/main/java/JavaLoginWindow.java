import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class JavaLoginWindow {

    @FXML
    protected Button logButton;

    @FXML
    protected TextField loginInput;

    @FXML
    protected TextField ipInput;

    @FXML
    protected TextField portInput;

    @FXML
    protected Label wrongLogin;

    @FXML
    void actionLogButton(ActionEvent event) {
    }
}