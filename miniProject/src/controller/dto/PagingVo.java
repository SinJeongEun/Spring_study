package controller.dto;

import org.json.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PagingVo {
	// 클라이언트에엇 3번을 요청하면 서번에 있는 pager 객체의 3번 필을 json 으로 전달한다.
	private int totalRows; // 전체 행수 // 전체 상품 갯수
	private int totalPageNo; // 전체 페이지 수
	private int totalGroupNo; // 전체 그룹 수
	private int startPageNo; // 그룹의 시작 페이지 번호
	private int endPageNo; // 그룹의 끝 페이지 번호
	private int pageNo; // 현재 페이지 번호
	private final int pagesPerGroup = 4; // 그룹당 페이지 수
	private int groupNo; // 현재 그룹 번호
	private int rowsPerPage = 5; // 페이지당 행 수
	private int startRowNo; // 페이지의 시작 행 번호(1, ..., n)
	private int endRowNo; // 페이지의 마지막 행 번호
//	private boolean hasPreviousPage;
//	private boolean hasNextPage;

	public PagingVo(int totalRows, int pageNo) { // 총 상품갯수와 사용자가 요구한 페이지번호
		this.totalRows = totalRows; // 필드에 총 상품 갯수 넣음

		totalPageNo = totalRows / rowsPerPage; // 전체 페이지 수 = 전체 갯수 / 페이지 당 행수
		if (totalRows % rowsPerPage != 0) // 페이지당 행수가 안된다면 1페이지
			totalPageNo++;

		if (pageNo > totalPageNo) // 현재고른 페이지보다 총 페이지가 적다면
			this.pageNo = totalPageNo; // 마지막 페이지 번호로
		else
			this.pageNo = pageNo; // 아니라면 현재페이지 필드에 저장

		totalGroupNo = totalPageNo / pagesPerGroup; // 총 그룹의 숫자 = 전체 페이지 / 그룹 당 페이지 수
		if (totalPageNo % pagesPerGroup != 0)
			totalGroupNo++;

		groupNo = (pageNo - 1) / pagesPerGroup + 1; // 그룹의 수 = 현재 페이지 -1 / 그룹 당 페이지수 +1

		startPageNo = (groupNo - 1) * pagesPerGroup + 1; // 그룹 시작 페이지 번호

		endPageNo = startPageNo + pagesPerGroup - 1; // 그룹의 끝 페이지 번호
		if (groupNo == totalGroupNo)
			endPageNo = totalPageNo;

		startRowNo = (pageNo - 1) * rowsPerPage + 1; // 페이지의 시작 행 번호
		endRowNo = pageNo * rowsPerPage; // 페이지의 끝 행 번호

//		// 이전 페이지 존재 여부
//		//
//		hasPreviousPage = startPageNo == 1 ? false : true;
//		if(hasPreviousPage == false) {
//			if(pageNo != startPageNo) {
//				hasPreviousPage = true;
//			}else hasPreviousPage = false;
//		}
//		
//		// 다음 페이지 존재 여부
//		hasNextPage = 
	}
	// rows per page 직접 지정이 가능한 생성자  
		public PagingVo(int totalRows, int pageNo, int rowsPerPage) { // 총 상품갯수와 사용자가 요구한 페이지번호
			this.totalRows = totalRows; // 필드에 총 상품 갯수 넣음
			this.rowsPerPage = rowsPerPage;
			
			totalPageNo = totalRows / rowsPerPage; // 전체 페이지 수 = 전체 갯수 / 페이지 당 행수
			if (totalRows % rowsPerPage != 0) // 페이지당 행수가 안된다면 1페이지
				totalPageNo++;

			if (pageNo > totalPageNo) // 현재고른 페이지보다 총 페이지가 적다면
				this.pageNo = totalPageNo; // 마지막 페이지 번호로
			else
				this.pageNo = pageNo; // 아니라면 현재페이지 필드에 저장

			totalGroupNo = totalPageNo / pagesPerGroup; // 총 그룹의 숫자 = 전체 페이지 / 그룹 당 페이지 수
			if (totalPageNo % pagesPerGroup != 0)
				totalGroupNo++;

			groupNo = (pageNo - 1) / pagesPerGroup + 1; // 그룹의 수 = 현재 페이지 -1 / 그룹 당 페이지수 +1

			startPageNo = (groupNo - 1) * pagesPerGroup + 1; // 그룹 시작 페이지 번호

			endPageNo = startPageNo + pagesPerGroup - 1; // 그룹의 끝 페이지 번호
			if (groupNo == totalGroupNo)
				endPageNo = totalPageNo;

			startRowNo = (pageNo - 1) * rowsPerPage + 1; // 페이지의 시작 행 번호
			endRowNo = pageNo * rowsPerPage; // 페이지의 끝 행 번호

		}

	public JSONObject pageToJson(PagingVo pvo) {
		JSONObject jobj = new JSONObject();
		jobj.put("totalRows", pvo.getTotalRows());
		jobj.put("totalPageNo", pvo.getTotalPageNo());
		jobj.put("totalGroupNo", pvo.getTotalGroupNo());
		jobj.put("startPageNo", pvo.getStartPageNo());
		jobj.put("endPageNo", pvo.getEndPageNo());
		jobj.put("pageNo", pvo.getPageNo());
		jobj.put("pagesPerGroup", pvo.getPagesPerGroup());
		jobj.put("groupNo", pvo.getGroupNo());
		jobj.put("rowPerPage", pvo.getRowsPerPage());
		jobj.put("startRowNo", pvo.getStartRowNo());
		jobj.put("endRowNo", pvo.getEndRowNo());

		return jobj;
	}

	public PagingVo jsonToPage(JSONObject json) {
		PagingVo pvo = new PagingVo(json.getInt("totalRows"), json.getInt("pageNo"));

		return pvo;
	}
	public PagingVo jsonToPage2(JSONObject json) {
		PagingVo pvo = new PagingVo(json.getInt("totalRows"), json.getInt("pageNo"), 3);

		return pvo;
	}

	public void PagingInfo(PagingVo p) {
		this.startPageNo = p.getStartPageNo();
		this.endPageNo = p.getEndPageNo();
		this.pageNo = p.getPageNo();
		this.totalPageNo = p.getTotalPageNo();

	}
	
	

	public void navigator(PagingVo pvo) {
		System.out.print("                                 ");
		if (pvo.getTotalPageNo() <= pvo.pagesPerGroup) { // 그룹이 1개밖에 없을 경우
			if (pvo.getPageNo() != 1) {
				System.out.print("[맨앞] ");
			}

			System.out.print("< ");
			for (int i = pvo.getStartPageNo(); i <= pvo.getEndPageNo(); i++) {
				if (i == pvo.getPageNo()) {
					System.out.print("(" + i + ") ");
				} else
					System.out.print(i + " ");
			}
			System.out.print(">");
			if (pvo.getPageNo() != pvo.getTotalPageNo())
				System.out.print(" [맨뒤]");

		} else {
		
			if (pvo.getStartPageNo() == 1) { // 1그룹 이라면
				if (pvo.getPageNo() != 1) {
					System.out.print("[맨앞] ");
				}

				System.out.print("< ");
				for (int i = pvo.getStartPageNo(); i <= pvo.getEndPageNo(); i++) {
					if (i == pvo.getPageNo()) {
						System.out.print("(" + i + ") ");
					} else
						System.out.print(i + " ");
				}
				System.out.print(">");
				System.out.print(" [다음] [맨뒤]");

			} else if (pvo.getEndPageNo() == pvo.getTotalPageNo()) {// 마지막 그룹이라면
				System.out.print("[맨앞] [이전] ");
				System.out.print("< ");
				for (int i = pvo.getStartPageNo(); i <= pvo.getEndPageNo(); i++) {
					if (i == pvo.getPageNo()) {
						System.out.print("(" + i + ") ");
					} else
						System.out.print(i + " ");
				}
				System.out.print(">");
				if (pvo.getPageNo() != pvo.getTotalPageNo()) {
					System.out.print(" [맨뒤]");
				}

			} else { //
				System.out.print("[맨앞] [이전] ");
				for (int i = pvo.getStartPageNo(); i <= pvo.getEndPageNo(); i++) {
					if (i == pvo.getPageNo()) {
						System.out.print("(" + i + ") ");
					} else {
						System.out.print(i + " ");
					}
				}
				System.out.print(" [다음] [맨뒤]");
			}
			
		}
		System.out.println();
		System.out.println(
				"===================================================================================================================");
	}

}
