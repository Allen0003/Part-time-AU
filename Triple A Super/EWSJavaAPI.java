import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.Contact;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

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
	public ExchangeService connectViaExchangeAutodiscover(String email, String password) {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		try {
			service.setCredentials(new WebCredentials(email, password));
			service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
			// service.autodiscoverUrl(email, new RedirectionUrlCallback());
			service.setTraceEnabled(true);
			// service.sendItem(item, savedCopyDestinationFolderId);
			Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
			FindItemsResults<Item> results = service.findItems(inbox.getId(), new ItemView(5));
			List<Map<String, String>> msgDataList = new ArrayList<Map<String, String>>();
			System.out.println("messages: " + inbox.getTotalCount());
			for (Item item : results) {
				Map<String, String> messageData = new HashMap<String, String>();
				messageData = readEmailItem(item.getId(), service);
				System.out.println("subject : " + messageData.get("subject").toString());
				System.out.println("Sender : " + messageData.get("senderName").toString());
				msgDataList.add(messageData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}

	/**
	 * get calendar data from office 365
	 * 
	 */
	public List<Map<String, String>> getCal(ExchangeService service, String account, Date startDate, Date endDate) {
		List<Map<String, String>> apntmtDataList = null;
		try {
			Contact contact = new Contact(service);
			contact.save(WellKnownFolderName.Contacts);
			CalendarView cView = new CalendarView(startDate, endDate, 5);
			FolderId confRoomFolderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox(account));
			CalendarFolder calendarFolder = CalendarFolder.bind(service, confRoomFolderId);
			FindItemsResults<Appointment> appointments = calendarFolder.findAppointments(cView);
			apntmtDataList = new ArrayList<Map<String, String>>();
			List<Appointment> appList = appointments.getItems();
			for (Appointment appointment : appList) {
				Map<String, String> appointmentData = new HashMap<String, String>();
				appointmentData = new EWSJavaAPI().readAppointment(appointment);
				apntmtDataList.add(appointmentData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apntmtDataList;
	}

	/**
	 * get e-mail - outlook from office 365
	 * 
	 */

	public Map<String, String> readEmailItem(ItemId itemId, ExchangeService service) {
		Map<String, String> messageData = new HashMap<String, String>();
		try {
			Item itm = Item.bind(service, itemId, PropertySet.FirstClassProperties);
			EmailMessage emailMessage = EmailMessage.bind(service, itm.getId());
			messageData.put("emailItemId", emailMessage.getId().toString());
			messageData.put("subject", emailMessage.getSubject().toString());
			messageData.put("fromAddress", emailMessage.getFrom().getAddress().toString());
			messageData.put("senderName", emailMessage.getSender().getName().toString());
			Date dateTimeCreated = emailMessage.getDateTimeCreated();
			messageData.put("SendDate", dateTimeCreated.toString());
			Date dateTimeRecieved = emailMessage.getDateTimeReceived();
			messageData.put("RecievedDate", dateTimeRecieved.toString());
			messageData.put("Size", emailMessage.getSize() + "");
			messageData.put("emailBody", emailMessage.getBody().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageData;
	}

	public static void main(String[] args) {
		try {
			System.out.println("start....");
			// wenpinw@student.unimelb.edu.au
			// apss1943
			EWSJavaAPI util = new EWSJavaAPI();
			String account = "Allen.Wu@tripleasuper.com.au";
			String password = "Tas@llen2017";
			System.out.println(" ================== read main ================== ");
			ExchangeService service = util.connectViaExchangeAutodiscover(account, password);

			System.out.println(" ================== read cal ================== ");
			Date startDate = Calendar.getInstance().getTime();
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, 30);
			Date endDate = now.getTime();
			util.getCal(service, account, startDate, endDate);

			System.out.println("done");
			// service.get
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> readAppointment(Appointment appointment) {
		Map<String, String> appointmentData = new HashMap<String, String>();
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			System.out.println(" id = " + appointment.getId().toString());

			System.out.println(" subject = " + appointment.getSubject() + " get start = "
					+ df.format(appointment.getStart()) + " end = " + df.format(appointment.getEnd()));

			appointmentData.put("appointmentItemId", appointment.getId().toString());
			appointmentData.put("appointmentSubject", appointment.getSubject());
			appointmentData.put("appointmentStartTime", df.format(appointment.getStart()));
			appointmentData.put("appointmentEndTime", df.format(appointment.getEnd()));
		} catch (ServiceLocalException e) {
			e.printStackTrace();
		}
		return appointmentData;
	}
}
