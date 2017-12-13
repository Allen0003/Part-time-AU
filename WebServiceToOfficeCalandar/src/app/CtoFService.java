package app;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import property.Leave;

@Path("/ctofservice")
public class CtoFService {

	private String account = "Allen.Wu@tripleasuper.com.au";
	private String password = "Apss1943";

	@POST
	@Consumes("application/json")
	public void test(Leave leave) {
		try {
			System.out.println("start....");
			EWSJavaAPI util = new EWSJavaAPI();
			ExchangeService service = util.connectViaExchangeAutodiscover(this.account, this.password);
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date startTime = dateFormat.parse(leave.getDateFrom());
			Date endTime = dateFormat.parse(leave.getDateTo());
			Appointment appointment = new Appointment(service);
			appointment.setSubject(leave.getType() + " ID: " + leave.getUserID());
			appointment.setStart(startTime);
			appointment.setEnd(endTime);

			appointment.save();

			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}