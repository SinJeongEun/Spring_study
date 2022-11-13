
package socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import org.json.JSONObject;

import view.user.CartView;
import view.user.UserView;
import view.user.MyReviewView;
import view.user.MypageView;
import view.user.OrderView;
import view.user.ProductView;
import view.user.QnaView;
import view.user.ReviewView;
import view.user.StartView;

//이거로 설명
public class ShoesClient {
	// 필드
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	JSONObject json; // 서버측 결과 값 받을 키

	public void connect() throws IOException {

		socket = new Socket("localhost", 50001);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		System.out.println("[클라이언트] 서버에 연결됨");
	}

	public JSONObject receive() {
		try {

			String read = dis.readUTF();
			json = new JSONObject(read);

		} catch (IOException e) {
			json = new JSONObject("[클라이언트] 서버 연결 끊김");
			System.exit(0);
		}
		return json;
	}

	public void send(String json) throws IOException {
		dos.writeUTF(json);
		dos.flush();
	}

	public void unconnect() throws IOException {
		socket.close();

	}

	public void goBack(JSONObject json) throws IOException, SQLException {
		String view = json.getString("view");

		switch (view) {
		case "StartView":

			UserView userView2 = new UserView();
			userView2.startquestion(this);
			break;

		case "UserView":

			UserView userView = new UserView();
			userView.startquestion(this);
			break;

		case "ProductView":

			if (json.has("id")) {
				int productId = Integer.parseInt(json.getString("id"));
				ProductView productView = new ProductView();
				productView.oneProduct(productId);
			}

			else if (json.has("page")) {
				int page = Integer.parseInt(json.getString("page"));
				ProductView productView = new ProductView();
				productView.startquestion(this, page);
			}

			break;

		case "CartView":

			CartView cartView = new CartView();
			cartView.startquestion(this);
			break;

		case "MypageView":

			MypageView mypageView = new MypageView();
			mypageView.startquestion(this);
			break;

		case "ReviewView":

			int id = Integer.parseInt(json.getString("id"));

			ReviewView reviewView = new ReviewView();
			reviewView.startquestion(this, id); ///// ??????????
			break;

		case "MyReviewView":

			MyReviewView myReviewView = new MyReviewView();
			myReviewView.startquestion(this); ///// ??????????
			break;

		case "QnaView":

			int id2 = Integer.parseInt(json.getString("id"));

			QnaView qnaView = new QnaView();
			qnaView.startquestion(this, id2); ///// ??????????
			break;

		case "OrderView":

			int pnum = Integer.parseInt(json.getString("page"));
			OrderView orderView = new OrderView();
			orderView.showOrders(this, pnum);
			break;

		}
	}

	public static void main(String[] args) { ///////////////////////// 여기서 부터 실행
		try {
			ShoesClient shoesClient = new ShoesClient();
			shoesClient.connect();

			StartView startView = new StartView(); // 회원가입 로그인 기능 뷰 초기 사용자가 보는 뷰
			startView.startquestion(shoesClient);

			boolean run = true;
			while (run) {
			}

			shoesClient.unconnect();

		} catch (Exception e) {
			System.out.println("[클라이언트] 서버 연결 안됨");
			e.printStackTrace();

		}
		System.out.println("[클라이언트] 종료");
	}

}
