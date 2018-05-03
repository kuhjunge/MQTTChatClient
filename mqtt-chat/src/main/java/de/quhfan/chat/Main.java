package de.quhfan.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import de.quhfan.chat.mqtt.MQTTChat;

public class Main implements PropertyChangeListener {

	public static void main(final String[] args) {
		new Main(setUpChat());
	}

	private static MQTTChat setUpChat() {
		final String serverAddr = "ServerAddress";
		final String user = "user";
		final String pw = "pw";
		final String cert = "cert";
		final Map<String, String> m = new HashMap<>();
		m.put(serverAddr, "");
		m.put(user, "");
		m.put(pw, "");
		m.put(cert, "");
		final Configuration c = new Configuration("MQTTChat", m);
		c.init();
		c.save();
		return new MQTTChat(c.getValue(user), c.getValue(pw), c.getValue(serverAddr), c.getValue(cert));
	}

	Chat m;

	private Main(final Chat c) {
		m = c;
		m.connect();
		m.addPropertyChangeListener(this);
		m.openChat("/chat/+");
		sendMessage(m, "Hello");

		final Scanner scanner = new Scanner(System.in);
		while (m.isConnected()) {
			final String input = scanner.next();
			sendMessage(m, input);
		}
		scanner.close();
		m.disconnect();
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		setNews((String) evt.getNewValue());
	}

	public void sendMessage(final Chat m, final String message) {
		m.sendMessage("/chat/" + m.getUserName(), "{\"message\": \"" + message + "\"}");
	}

	public void setNews(final String news) {
		System.out.print(news);
	}
}
