package QuickBase.inputObjs;

import java.util.HashMap;

public class Insert {
	String udata = "qdbapi";
	String ticket;
	String apptoken;

	HashMap<String, String> field;

	public String getUdata() {
		return udata;
	}

	public void setUdata(String udata) {
		this.udata = udata;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getApptoken() {
		return apptoken;
	}

	public void setApptoken(String apptoken) {
		this.apptoken = apptoken;
	}

	public HashMap<String, String> getField() {
		return field;
	}

	public void setFields(HashMap<String, String> field) {
		this.field = field;
	}
}
