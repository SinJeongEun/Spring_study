
package socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import service.ReviewService;

public class ShoesServer {
	// 필드
	ServerSocket serverSocket;
	ExecutorService threadPool = Executors.newFixedThreadPool(100);

	public void start() throws IOException {
//		
		serverSocket = new ServerSocket(50001);
		System.out.println("[서버] 시작됨");

		Thread thread = new Thread(() -> {
			try {
				while (true) {

					Socket socket = serverSocket.accept();
					SocketClient sc = new SocketClient(this, socket);
					
				}
			} catch (Exception e) {
			}
		});
		thread.start();
	}

	public void stop() {
		try {
			serverSocket.close();
			threadPool.shutdownNow();
			System.out.println("[서버] 종료됨 ");
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {

		try {
			ShoesServer shoesServer = new ShoesServer();
			shoesServer.start();

			System.out.println("----------------------------------------------------");
			System.out.println("서버를 종료하려면 q 또는 Q를 입력하고 enter키를 입력하세요.");
			System.out.println("----------------------------------------------------");

			// 키보드 입력을 받음
			Scanner sc = new Scanner(System.in);
			while (true) {
				String key = sc.nextLine();

				if (key.toLowerCase().equals("q")) {
					break;
				}
			}

			sc.close();

			// TCP 서버 종료
			shoesServer.stop();

		} catch (IOException e) {
			System.out.println("[서버] " + e.getMessage());
		}

	}

}
