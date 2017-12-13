package app;

// 13/12/2017 successfully save data to office 365 calendar

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

/**
 * get data from Office 365
 * 
 */

public class EWSJavaAPI {
	public static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {
		public boolean autodiscoverRedirectionUrlValidationCallback(String redirectionUrl) {
			return redirectionUrl.toLowerCase().startsWith("https://");
		}
	}

	/**
	 * using account number and password to get connection
	 * 
	 */
	public ExchangeService connectViaExchangeAutodiscover(String email, String password) throws Exception {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		service.setCredentials(new WebCredentials(email, password));
		service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
		return service;
	}

	public void addCal(ExchangeService service) {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = dateFormat.parse("2017-12-25 16:00:00");
			Date endTime = dateFormat.parse("2017-12-25 18:00:00");

			Appointment appointment = new Appointment(service);

			appointment.setSubject("TestQooTest");
			appointment.setBody(new MessageBody("Body text"));
			appointment.setStart(startTime);
			appointment.setEnd(endTime);
			appointment.setLocation("My Office");
			appointment.setReminderMinutesBeforeStart(30);

			appointment.save();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			System.out.println("start....");
			EWSJavaAPI util = new EWSJavaAPI();

			String account = "Allen.Wu@tripleasuper.com.au";
			String password = "Apss1943";
			ExchangeService service = util.connectViaExchangeAutodiscover(account, password);
			util.addCal(service);

			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
