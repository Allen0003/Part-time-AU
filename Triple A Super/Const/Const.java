package Const;

import java.util.HashMap;

import QuickBase.inputObjs.User;

public class Const {

	public static final String URL = "http://localhost/test/psAdviser.php";

	public static final String URLOFFICE = "https://outlook.office365.com/EWS/Exchange.asmx";
	public static final int DAY = 180;

	public static final String USERNAME = "Allen.Wu@tripleasuper.com.au";
	public static final String PASSWORD = "Tas@llen2017";

	public static final int API_Authenticate = 1;
	public static final int API_AddRecord = 2;
	public static final int API_DeleteRecord = 3;
	public static final int API_DoQuery = 4;

	public static User user;

	public static HashMap<Integer, String> qbURls;

	public static final String qbUser = "apss1943@gmail.com";
	public static final String qbPassword = "apss1943";

	static {
		user = new User();
		user.setUsername(qbUser);
		user.setPassword(qbPassword);

		qbURls = new HashMap<Integer, String>();
		qbURls.put(API_Authenticate, "https://brianpedretti.quickbase.com/db/main");
		qbURls.put(API_AddRecord, "https://brianpedretti.quickbase.com/db/bk7zm6n2q");
		qbURls.put(API_DeleteRecord, "https://brianpedretti.quickbase.com/db/bk7zm6n2q");
		qbURls.put(API_DoQuery, "https://brianpedretti.quickbase.com/db/bk7zm6n2q");
	}

	public static String qbAppToken = "d25qirddiecbacezx8i4gbjsgx";

}
