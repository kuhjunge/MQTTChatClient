package de.quhfan.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import de.quhfan.chat.mqtt.MQTTChat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements PropertyChangeListener, Initializable {
	public static MQTTChat setUpChat() {
		final String serverAddr = "ServerAddress";
		final String user = "user";
		final String pw = "pw";
		final String certPath = "cert";
		final Map<String, String> m = new HashMap<>();
		m.put(serverAddr, "tcp://127.0.0.1:1883");
		m.put(user, "admin");
		m.put(pw, "");
		m.put(certPath, "");
		final Configuration c = new Configuration("MQTTChat", m);
		c.init();
		c.save();
		return new MQTTChat(c.getValue(user), c.getValue(pw), c.getValue(serverAddr), c.getValue(certPath));
	}

	Chat m;

	@FXML
	private TextArea chatWindow;

	@FXML
	private TextField textFieldMessage;

	public Controller(final Chat c) {
		m = c;
	}

	public void connect() {
		m.connect();
		m.addPropertyChangeListener(this);
		m.openChat("/chat/+");
		sendMessage(m, "Hello");
	}

	public void disconnect() {
		m.disconnect();
	}

	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1) {
		connect();
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		setNews((String) evt.getNewValue());
	}

	@FXML
	private void sendMessage(final ActionEvent event) {
		sendMessage(m, textFieldMessage.getText());
		textFieldMessage.clear();
	}

	public void sendMessage(final Chat m, final String message) {
		m.sendMessage("/chat/" + m.getUserName(), "{\"message\": \"" + message + "\"}");
	}

	public void setNews(final String news) {
		chatWindow.appendText(news);
	}
}
