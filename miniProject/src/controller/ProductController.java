package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.dto.PagingVo;
import controller.dto.ProductList;
import controller.dto.RegisterProduct;
import domain.Product;
import service.ProductService;
import socket.server.SocketClient;

// 컨트롤러는 function으로 switch
public class ProductController implements Controller {
	// 필드
	ProductService productService = ProductService.getInstance();
	//컨트롤러
	@Override
	public void control(SocketClient socketClient, JSONObject cMessage) throws SQLException, IOException {
		String function = cMessage.getString("function");
		switch (function) {
		case "start":
			
			int pageNum = cMessage.getInt("pageNum");
			PagingVo pvo = new PagingVo(productService.countAllProducts(), pageNum);
			List<ProductList> list = productService.showProducts(pvo);

			JSONArray jsonarr = new JSONArray();
			for (ProductList p : list) {
				jsonarr.put(p.productListTOJson(p));
			}
			JSONObject page = pvo.pageToJson(pvo);
		
			JSONObject result = new JSONObject();
			result.put("productList", jsonarr);
			result.put("pvo", page);
			socketClient.send(result.toString());
			break;

		case "filter":
			switch (cMessage.getString("standard")) {
			case "1":// 카테고리 기준
	
				int categoryId = cMessage.getInt("data");
				int pageNum1 = cMessage.getInt("pageNum");
				PagingVo pvo1 = new PagingVo(productService.countCategory(categoryId), pageNum1);
				
				List<ProductList> list1 = productService.showCategory(pvo1, categoryId);
				
				JSONArray jsonarr1 = new JSONArray();
				for (ProductList p : list1) {
					jsonarr1.put(p.productListTOJson(p));
				}
				JSONObject page1 = pvo1.pageToJson(pvo1);
				//send
				JSONObject result1 = new JSONObject();
				result1.put("productList", jsonarr1);
				result1.put("pvo", page1);
				socketClient.send(result1.toString());

				break;

			case "2":// 브랜드 선택
				int companyId = cMessage.getInt("data");
				int pageNum2 = cMessage.getInt("pageNum");
				PagingVo pvo2 = new PagingVo(productService.countCompany(companyId), pageNum2);
				List<ProductList> list2 = productService.showCompany(pvo2, companyId);
				// DTO -> JSON
				JSONArray jsonarr2 = new JSONArray();
				for (ProductList p : list2) {
					jsonarr2.put(p.productListTOJson(p));
				}
				JSONObject page2 = pvo2.pageToJson(pvo2);
				// send
				JSONObject result2 = new JSONObject();
				result2.put("productList", jsonarr2);
				result2.put("pvo", page2);
				socketClient.send(result2.toString());

				break;

			case "3": // 성별 기준
				// 데이터 변환
				String sex = "";
				if (cMessage.getInt("data") == 1) {
					sex = "MALE";
				} else if (cMessage.getInt("data") == 2) {
					sex = "FEMALE";
				} else {
					sex = "ALL";
				}

				int pageNum3 = cMessage.getInt("pageNum");
				PagingVo pvo3 = new PagingVo(productService.countSex(sex), pageNum3);
				List<ProductList> list3 = productService.showSex(pvo3, sex);
				JSONArray jsonarr3 = new JSONArray();
				for (ProductList p : list3) {
					jsonarr3.put(p.productListTOJson(p));
				}
				JSONObject page3 = pvo3.pageToJson(pvo3);
				
				JSONObject result3 = new JSONObject();
				result3.put("productList", jsonarr3);
				result3.put("pvo", page3);
				socketClient.send(result3.toString());

				break;

			case "4":
				String price = "";
				if (cMessage.getInt("data") == 1) {
					price = "asc";
				} else {
					price = "desc";
				}

				int pageNum4 = cMessage.getInt("pageNum");
				PagingVo pvo4 = new PagingVo(productService.countAllProducts(), pageNum4);
				List<ProductList> list4 = productService.showprice(pvo4, price);
				JSONArray jsonarr4 = new JSONArray();
				for (ProductList p : list4) {
					jsonarr4.put(p.productListTOJson(p));
				}
				JSONObject page4 = pvo4.pageToJson(pvo4);
				
				JSONObject result4 = new JSONObject();
				result4.put("productList", jsonarr4);
				result4.put("pvo", page4);
				socketClient.send(result4.toString());

			}
			break;

		case "registerProduct":
			// JSON -> DTO

			String name = cMessage.getString("name");
			int price = cMessage.getInt("price");
			List<Integer> sizeList = new ArrayList<>();
			JSONArray jarrSize = cMessage.getJSONArray("size");
			for (Object obj : jarrSize) {
				int size = Integer.parseInt((String) obj);
				sizeList.add(size);
			}
			List<Integer> colorList = new ArrayList<>();
			JSONArray jarrcolor = cMessage.getJSONArray("color");
			for (Object obj : jarrcolor) {
				System.out.println((String) obj);
				int color = Integer.parseInt((String) obj);
				colorList.add(color);
			}
			int categoryId = cMessage.getInt("categoryId");
			int companyId = cMessage.getInt("companyId");
			String sex = cMessage.getString("productSex");
			
			RegisterProduct registerProduct = new RegisterProduct(name, price, sex, companyId, categoryId, colorList,
					sizeList);
			
			//등록 결과
			String message = productService.registerProduct(registerProduct);
			JSONObject regiResult = new JSONObject();
			regiResult.put("result", message);
			
			socketClient.send(regiResult.toString());
			break;

		case "showOne":
			int pid = cMessage.getInt("productId");
			Product p = productService.showOneProduct(pid);
			JSONObject showOneResult = new JSONObject();
			if (p != null) {
				JSONObject jo = p.productToJson(p);
				showOneResult.put("product", jo);
				socketClient.send(showOneResult.toString());
			} else {
				showOneResult.put("result", "존재하지 않는 상품입니다.");
				socketClient.send(showOneResult.toString());
			}
			break;
			
		case "deleteProduct":
			int proId = cMessage.getInt("productId");
			String delete = productService.deleteProduct(proId);
			JSONObject deleteResult = new JSONObject();
			deleteResult.put("result", delete);
			socketClient.send(deleteResult.toString());
			break;

		}
	}

}
