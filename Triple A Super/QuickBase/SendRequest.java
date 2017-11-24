package QuickBase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import Const.Const;
import QuickBase.inputObjs.Delete;
import QuickBase.inputObjs.Insert;
import QuickBase.inputObjs.Select;
import QuickBase.outputObjs.Auth;

public class SendRequest {
	public static void main(String[] args) {
		try {
//			System.out.println(new SendRequest().sendSelect());
			System.out.println(new SendRequest().sendDelete("1511311977929"));
			// Select select = new Select();
			// select.setApptoken("apptoken");
			// select.setTicket("tick");
			// Util.showFields(select);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String sendDelete(String rid) throws Exception {
		HttpsURLConnection con = setHeader(Const.qbURls.get(Const.API_DeleteRecord), Const.API_DeleteRecord);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		Delete delete = new Delete();
		delete.setApptoken(Const.qbAppToken);
		delete.setTicket(sendAuth());
		delete.setRid(rid);
		wr.writeBytes(new CreateXml().getXML(delete));
		wr.flush();
		wr.close();

		return getOutPutStream(con.getInputStream());
	}

	public String sendSelect() throws Exception {
		HttpsURLConnection con = setHeader(Const.qbURls.get(Const.API_DoQuery), Const.API_DoQuery);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		Select select = new Select();
		select.setApptoken(Const.qbAppToken);
		select.setTicket(sendAuth());
		wr.writeBytes(new CreateXml().getXML(select));
		wr.flush();
		wr.close();

		return getOutPutStream(con.getInputStream());
	}

	public String sendInsert() throws Exception {
		HttpsURLConnection con = setHeader(Const.qbURls.get(Const.API_AddRecord), Const.API_AddRecord);
		// TODO do insert
		Insert insert = new Insert();
		insert.setApptoken(Const.qbAppToken);
		insert.setTicket(sendAuth());
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("company___state", "vic");
		fields.put("tas_or_preset", "TAS");
		fields.put("fund_code", "MYGGYYGG");
		insert.setFields(fields);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(new CreateXml().getXML(insert));
		wr.flush();
		wr.close();
		return getOutPutStream(con.getInputStream());
	}

	/*
	 * return ticket
	 * 
	 **/
	private String sendAuth() throws Exception {
		HttpsURLConnection con = setHeader(Const.qbURls.get(Const.API_Authenticate), Const.API_Authenticate);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(new CreateXml().getXML(Const.user));
		wr.flush();
		wr.close();
		JAXBContext jaxbContext = JAXBContext.newInstance(Auth.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(getOutPutStream(con.getInputStream()));
		Auth auth = (Auth) jaxbUnmarshaller.unmarshal(reader);
		return auth.getTicket();
	}

	private HttpsURLConnection setHeader(String url, int action) throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/xml");
		if (action == Const.API_Authenticate) {
			con.setRequestProperty("QUICKBASE-ACTION", "API_Authenticate");
			con.setRequestProperty("CURLOPT_RETURNTRANSFER", "true");
		} else if (action == Const.API_AddRecord) {
			con.setRequestProperty("QUICKBASE-ACTION", "API_AddRecord");
		} else if (action == Const.API_DeleteRecord) {
			con.setRequestProperty("QUICKBASE-ACTION", "API_DeleteRecord");
		} else if (action == Const.API_DoQuery) {
			con.setRequestProperty("QUICKBASE-ACTION", "API_DoQuery");
		}
		con.setDoOutput(true);
		return con;
	}

	private String getOutPutStream(InputStream inp) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(inp));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

}
