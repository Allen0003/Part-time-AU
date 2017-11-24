package QuickBase.outputObjs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "qdbapi")
public class Auth {

	@XmlElement(name = "action")
	String action;
	@XmlElement(name = "errcode")
	String errcode;
	@XmlElement(name = "errtext")
	String errtext;
	@XmlElement(name = "ticket")
	String ticket;
	@XmlElement(name = "userid")
	String userid;

	public String getAction() {
		return action;
	}

	public String getErrcode() {
		return errcode;
	}

	public String getErrtext() {
		return errtext;
	}

	public String getTicket() {
		return ticket;
	}

	public String getUserid() {
		return userid;
	}

}
