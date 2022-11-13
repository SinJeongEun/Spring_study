package controller.dto;

public enum ProductSex {
	FEMALE, MALE, ALL;
	
	public static String choosePtype(int n) {
		String ptype = "";
		switch(n) {
		case 1 : 
			ptype = ProductSex.FEMALE.toString();
			break;
		case 2 :
			ptype = ProductSex.MALE.toString();
			break;
		case 3 :
			ptype = ProductSex.ALL.toString();
			break;
		}
		return ptype;
	}
}
