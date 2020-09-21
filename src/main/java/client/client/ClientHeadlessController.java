package client.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ClientHeadlessController {

	@FXML
	private TextField IP;

	@FXML
	private TextField Port;

	@FXML
	private Button sync;

	@FXML
	private ProgressBar totalProg;

	@FXML
	private ProgressBar currentProg;

	@FXML
	private VBox logOutput;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	void startSync(ActionEvent event) {
		new Sync(IP.getText(), Port.getText(), currentProg, totalProg, logOutput).start();
		sync.setDisable(true);
		logOutput.heightProperty().addListener(observable -> {
			scrollPane.setVvalue(1.0);
		});
	}
	
	public void output(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logOutput.getChildren().add(new Label(message));
			}
		});
	}

	public void updateSmall(double update) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				currentProg.setProgress(update);
			}
		});
	}

	public void updateBig(double update) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				totalProg.setProgress(update);
			}
		});
	}
	
	public void sync(String ip, String port) {
		new Sync(ip, port, currentProg, totalProg, logOutput).start();
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
