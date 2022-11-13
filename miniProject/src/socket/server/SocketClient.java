package socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.json.JSONObject;

import controller.CartController;
import controller.Controller;
import controller.MyReviewController;
import controller.MypageController;
import controller.OrderController;
import controller.ProductController;
import controller.QnaController;
import controller.ReviewController;
import controller.UserController;
import domain.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketClient {
	public static Product product;
	// 필드
	ShoesServer shoesServer;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	Controller controller;

	String loginId; // 로그인 성공시 유저의 정보가 담김
	
	Stack<Map<String, String>> stack = new Stack<>(); // 뒤로가기를 위한 Stack 사용 

	JSONObject result = new JSONObject();

	// 생성자
	public SocketClient(ShoesServer shoesServer, Socket socket) {

		try {

			this.shoesServer = shoesServer;
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			receive();

		} catch (IOException e) {

		}

	}

	// 클라이언트로부터 받음
	public void receive() {

		shoesServer.threadPool.execute(() -> {
			try {
				while (true) {
					String clientMessage = dis.readUTF();
					JSONObject cMessage = new JSONObject(clientMessage);
					String viewName = cMessage.getString("viewName");
					
					switch (viewName) {
						case "StartView":
							Map<String, String> map = new HashMap<>();
							map.put("view", "StartView");
							this.stack.push(map);
							
							controller = new UserController();
							controller.control(this, cMessage);
							break;
							
						case "ProductView":
							Map<String, String> map1 = new HashMap<>();
							map1.put("view", "ProductView");
							
							if(cMessage.has("pageNum")) {
								map1.put("page", cMessage.getBigInteger("pageNum") + "");	

							}
							else if(cMessage.has("productId")) {
								map1.put("id", cMessage.getBigInteger("productId") + "");

							}
							this.stack.push(map1);
			
	
							System.out.println(loginId);
							controller = new ProductController();
							controller.control(this, cMessage);
							break;
							
						case "CartView":
							Map<String, String> map2 = new HashMap<>();
							map2.put("view", "CartView");
							this.stack.push(map2);
	
							controller = new CartController();
							controller.control(this, cMessage);
							break;
							
						case "MypageView":
							Map<String, String> map3 = new HashMap<>();
							map3.put("view", "MypageView");
							this.stack.push(map3);
	
							controller = new MypageController();
							controller.control(this, cMessage);
							break;
							
	
						case "ReviewView":
							Map<String, String> map4 = new HashMap<>();
							map4.put("view", "ReviewView");
							if(cMessage.has("productId")) {
								map4.put("id", cMessage.getInt("productId") + "");	
							
							}
							this.stack.push(map4);
	
							controller = new ReviewController();
							controller.control(this, cMessage);
							break;
							
						case "MyReviewView":
							Map<String, String> mmap = new HashMap<>();
							mmap.put("view", "MyReviewView");
							this.stack.push(mmap);
	
							controller = new MyReviewController();
							controller.control(this, cMessage);
							break;
	
						case "QnaView":
							Map<String, String> map5 = new HashMap<>();
							map5.put("view", "QnaView");
							map5.put("id", cMessage.getInt("productId") + "");
							this.stack.push(map5);
	
							controller = new QnaController();
							controller.control(this, cMessage);
							break;
							
						case "UserView":
							Map<String, String> map9 = new HashMap<>();
							map9.put("view", "UserView");
							this.stack.push(map9);
							break;
							
						case "OrderView":
							Map<String, String> map6 = new HashMap<>();
							map6.put("view", "OrderView");
							map6.put("page", cMessage.getInt("pageNum") + "");
							this.stack.push(map6);
								
							controller = new OrderController();
							controller.control(this, cMessage);	
							break;
							
						case "AdReviewView":
							Map<String, String> map7 = new HashMap<>();
							map7.put("view", "AdReviewView");
							map7.put("id", cMessage.getInt("productId")+"");
							this.stack.push(map7);
							
							controller = new ReviewController();
							controller.control(this, cMessage);
							break;
	
						case "AdQnaView":
							Map<String, String> map8 = new HashMap<>();
							map8.put("view", "AdQnaView");
							map8.put("AdQnaView", cMessage.getInt("productId")+"");
							this.stack.push(map8);
							
							controller = new QnaController();
							controller.control(this, cMessage);
							break;
							
						case "back":
					
							
							Iterator<Map<String, String>> itr = this.stack.iterator();
							while(itr.hasNext()) {
//								System.out.println(itr.next().get("view"));
//								System.out.println(iter.next().get("id"));
								Map<String, String> mm = itr.next();
							
								if(mm.containsKey("page")) {
														
								}	
							}
					
							
//							this.stack.pop();
							this.stack.pop();
							if(stack.size() > 0) {
								Map<String, String> previous = this.stack.pop();
								//이동할 페이지 이름
								result.put("view", previous.get("view"));
								
								if(previous.containsKey("id")) {
									// 필요한 값 id
									result.put("id",previous.get("id"));								
								}	
								if(previous.containsKey("page")) {
									// 필요한 값 id
									result.put("page",previous.get("page"));								
								}	
								if(previous.containsKey("clientMessage")) {
									result.put("clientMessage",previous.get("clientMessage"));					
								}
							}
							
							else result.put("view", "UserView");
							
					
							send(result.toString());
							break;
							
						case "logout":
							loginId = null;
							this.stack = null;
							this.stack = new Stack<>();
							
							result.put("result", "로그아웃되었습니다.");
							send(result.toString());
							break;
					}

				}

			} catch (Exception e) {
				System.out.println("[클라이언트] 종료");
				e.printStackTrace();
			}

		});

	}

	// 클라이언트로 보냄
	public void send(String json) {
		try {
			dos.writeUTF(json);
			dos.flush();
		} catch (IOException e) {
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {

		}

	}

}
