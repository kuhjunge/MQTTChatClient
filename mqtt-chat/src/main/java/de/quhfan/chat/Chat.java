package de.quhfan.chat;

import java.beans.PropertyChangeListener;

public interface Chat {

	void addPropertyChangeListener(PropertyChangeListener pcl);

	boolean connect();

	void disconnect();

	String getUserName();

	boolean isConnected();

	void openChat(String channel);

	void removePropertyChangeListener(PropertyChangeListener pcl);

	void sendMessage(String channel, String message);

}