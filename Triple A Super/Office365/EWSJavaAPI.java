package Office365;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Const.Const;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
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
import properties.UserCalendar;


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
		service.setUrl(new URI(Const.URLOFFICE));
		service.setTraceEnabled(true);
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		FindItemsResults<Item> results = service.findItems(inbox.getId(), new ItemView(5));
		List<Map<String, String>> msgDataList = new ArrayList<Map<String, String>>();
		for (Item item : results) {
			Map<String, String> messageData = new HashMap<String, String>();
			messageData = readEmailItem(item.getId(), service);
			msgDataList.add(messageData);
		}
		return service;
	}

	/**
	 * get calendar data from office 365
	 * 
	 */
	public List<Map<String, String>> getCal(ExchangeService service, String account, Date startDate, Date endDate)
			throws Exception {
		List<Map<String, String>> apntmtDataList = null;

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
		return apntmtDataList;
	}

	/**
	 * get e-mail - outlook from office 365
	 * 
	 */

	public Map<String, String> readEmailItem(ItemId itemId, ExchangeService service) throws Exception {
		Map<String, String> messageData = new HashMap<String, String>();
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
		return messageData;
	}

	public static void main(String[] args) {
		try {
			System.out.println("start....");
			EWSJavaAPI util = new EWSJavaAPI();
			String account = Const.USERNAME;
			String password = Const.PASSWORD;
			ExchangeService service = util.connectViaExchangeAutodiscover(account, password);

			Date startDate = Calendar.getInstance().getTime();
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, Const.DAY);
			Date endDate = now.getTime();
			List<Map<String, String>> apntmtDataList = util.getCal(service, account, startDate, endDate);

			ArrayList<UserCalendar> userCalendarList = new ArrayList<UserCalendar>();
			for (Map<String, String> apntmtData : apntmtDataList) {
				UserCalendar userCalendar = new UserCalendar();
				userCalendar.setId(apntmtData.get("appointmentItemId"));
				userCalendar.setSubject(apntmtData.get("appointmentSubject"));
				userCalendar.setStartTime(apntmtData.get("appointmentStartTime"));
				userCalendar.setEndTime(apntmtData.get("appointmentEndTime"));
				userCalendarList.add(userCalendar);
			}

			for (UserCalendar calendar : userCalendarList) {
				System.out.println(calendar.getStartTime());
//				new JavaToPhp().sendPost(calendar);
			}

			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> readAppointment(Appointment appointment) throws Exception {
		Map<String, String> appointmentData = new HashMap<String, String>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		appointmentData.put("appointmentItemId", appointment.getId().toString());
		appointmentData.put("appointmentSubject", appointment.getSubject());
		appointmentData.put("appointmentStartTime", df.format(appointment.getStart()));
		appointmentData.put("appointmentEndTime", df.format(appointment.getEnd()));
		return appointmentData;
	}
}
