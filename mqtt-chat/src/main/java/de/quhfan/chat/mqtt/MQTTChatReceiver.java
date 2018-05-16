package de.quhfan.chat.mqtt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTChatReceiver implements MqttCallback {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private String message;

	private final PropertyChangeSupport support;

	public MQTTChatReceiver() {
		support = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(final PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	@Override
	public void connectionLost(final Throwable arg0) {
		setMessage("Connection lost", true);
	}

	@Override
	public void deliveryComplete(final IMqttDeliveryToken arg0) {
		setMessage("Send", true);
	}

	@Override
	public void messageArrived(final String arg0, final MqttMessage arg1) throws Exception {
		setMessage(arg0 + ": " +new String(arg1.getPayload(), StandardCharsets.UTF_8), false);
	}

	public void removePropertyChangeListener(final PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	public void setMessage(final String value, final boolean system) {
		support.firePropertyChange("news", " " + message,
				(system ? "!" : " ") + LocalDateTime.now().format(formatter) + ": " + value);
		message = value;
	}
}
