package de.quhfan.chat.mqtt;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTChatReceiver implements MqttCallback {
	private static final String eol = "\r\n";

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
		setMessage("!Connection lost!" + eol);
	}

	@Override
	public void deliveryComplete(final IMqttDeliveryToken arg0) {
		setMessage("!Send!" + eol);
	}

	@Override
	public void messageArrived(final String arg0, final MqttMessage arg1) throws Exception {
		setMessage("+" + arg0 + ":" + arg1 + eol);
	}

	public void removePropertyChangeListener(final PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	public void setMessage(final String value) {
		support.firePropertyChange("news", message, value);
		message = value;
	}
}
