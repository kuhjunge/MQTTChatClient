package de.quhfan.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import de.quhfan.chat.mqtt.MQTTAsyncChat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements PropertyChangeListener, Initializable {
	private final static String serverAddr = "ServerAddress";
	private final static String user = "user";
	private final static String pw = "pw";
	private final static String certPath = "cert";

	private Configuration conf;

	private Chat m;

	@FXML
	private TextArea chatBox;

	@FXML
	private TextField textFieldMessage;

	@FXML
	private TextField textBoxServer;

	@FXML
	private TextField textBoxCertificate;

	@FXML
	private TextField textFieldUser;

	@FXML
	private TextField textFieldPw;

	@FXML
	private TextField textFieldTopic;

	@FXML
	private TextField textFieldSendTopic;

	public Controller() {
	}

	@FXML
	private void connect(final ActionEvent event) {
		if (m == null) {
			connect(textFieldTopic.getText(), textFieldSendTopic.getText());
		} else if (m.isConnected()) {
			m.disconnect();
			m = null;
		}
	}

	public void connect(final String topic, final String sendTopic) {
		conf.setValue(serverAddr, textBoxServer.getText());
		conf.setValue(user, textFieldUser.getText());
		conf.setValue(pw, textFieldPw.getText());
		conf.setValue(certPath, textBoxCertificate.getText());
		conf.save();
		m = setUpChat(conf);
		m.connectClient();
		m.addPropertyChangeListener(this);
		m.openChat(topic);
		sendMessage(sendTopic, m, "Hello");
	}

	public void disconnect() {
		if (m != null) {
			m.disconnect();
		}
	}

	@FXML
	public void exitApplication(final ActionEvent event) {
		Platform.exit();
	}

	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1) {
		conf = new Configuration("MQTTChat");
		conf.setValue(serverAddr, "tcp://127.0.0.1:1883");
		conf.setValue(user, "admin");
		conf.setValue(pw, "");
		conf.setValue(certPath, "");
		conf.init();
		conf.save();
		textBoxServer.setText(conf.getValue(serverAddr));
		textFieldUser.setText(conf.getValue(user));
		textFieldPw.setText(conf.getValue(pw));
		textBoxCertificate.setText(conf.getValue(certPath));
		textFieldTopic.setText("/chat/+");
		textFieldSendTopic.setText("/chat/8");
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		final String str = (String) evt.getNewValue();
		setNews(str, str.startsWith("!"));
	}

	@FXML
	private void sendMessage(final ActionEvent event) {
		sendMessage(textFieldSendTopic.getText(), m, textFieldMessage.getText());
		textFieldMessage.clear();
	}

	public void sendMessage(final String channel, final Chat m, final String message) {
		if (m != null && m.isConnected()) {
			m.sendMessage(channel, "{\"message\": \"" + message + "\"}");
		}
	}

	public void setNews(final String news, final boolean red) {
		chatBox.appendText(news + "\r\n");
	}

	public MQTTAsyncChat setUpChat(final Configuration c) {
		return new MQTTAsyncChat(c.getValue(user), c.getValue(pw), c.getValue(serverAddr), c.getValue(certPath),
				"{\"message\": \"" + c.getValue(user) + " out!\"}", "/chat/" + c.getValue(user),
				c.getValue(user) + Math.random());
	}
}
