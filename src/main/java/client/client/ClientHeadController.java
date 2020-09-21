package client.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ClientHeadController {

	@FXML
	private TextField IP;

	@FXML
	private TextField Port;

	@FXML
	private Button sync;
	
    @FXML
    private Button login;

	@FXML
	private ProgressBar totalProg;

	@FXML
	private ProgressBar currentProg;

	@FXML
	private VBox logOutput;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Label loginStatus;

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	void startSync(ActionEvent event) {
		if (!IP.getText().equals("") && !Port.getText().equals("")) {
			new Sync(IP.getText(), Port.getText(), currentProg, totalProg, logOutput, false).start();
			sync.setDisable(true);
		} else {
			output("Please provide a valid port and IP address");
		}

		logOutput.heightProperty().addListener(observable -> {
			scrollPane.setVvalue(1.0);
		});
	}

	@FXML
	void launch(ActionEvent event) {
		logOutput.heightProperty().addListener(observable -> {
			scrollPane.setVvalue(1.0);
		});
		if(sync.isDisabled() && login.isDisabled()) {
			//TODO launch
		}else {
			output("Please sync and login before launching");
		}
	}

	@FXML
	void login(ActionEvent event) {
		logOutput.heightProperty().addListener(observable -> {
			scrollPane.setVvalue(1.0);
		});
		if(!username.getText().equals("") && !password.getText().equals("")) {
			//TODO login
		}else {
			output("Please provide a valid username and password");
		}
	}

	
	public void output(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logOutput.getChildren().add(new Label(message));
			}
		});
	}
	

	public TextField getIP() {
		return IP;
	}

	public TextField getPort() {
		return Port;
	}

	public Button getSync() {
		return sync;
	}

	public ProgressBar getTotalProg() {
		return totalProg;
	}

	public ProgressBar getCurrentProg() {
		return currentProg;
	}

	public VBox getLogOutput() {
		return logOutput;
	}

	public ScrollPane getScrollPane() {
		return scrollPane;
	}

}
