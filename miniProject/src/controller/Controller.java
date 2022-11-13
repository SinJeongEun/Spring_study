package controller;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONObject;

import socket.server.SocketClient;

public interface Controller {
	abstract void control(SocketClient sc, JSONObject json) throws SQLException, IOException;
}
