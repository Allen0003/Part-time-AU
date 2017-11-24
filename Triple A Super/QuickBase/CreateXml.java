package QuickBase;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Const.Const;
import Const.Util;
import QuickBase.inputObjs.Insert;
import QuickBase.inputObjs.Select;

public class CreateXml {

	public static void main(String[] args) {
		Select select = new Select();
		select.setApptoken("token");
		select.setTicket("ggg");
		try {
			System.out.println(new CreateXml().getXML(select));
			Insert insert = new Insert();
			insert.setApptoken(Const.qbAppToken);
			insert.setTicket("tick");

			HashMap<String, String> fields = new HashMap<String, String>();
			fields.put("company___state", "vic");
			fields.put("tas_or_preset", "TAS");
			fields.put("fund_code", "Qoo1000");
			insert.setFields(fields);

			System.out.println((new CreateXml().getXML(insert)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getXML(Object obj) throws Exception {
		return createXMLString(obj);
	}

	private String xmlObjToString(Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter outWriter = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(outWriter));
		return outWriter.getBuffer().toString();
	}

	@SuppressWarnings("unchecked")
	private String createXMLString(Object obj) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element rootElement = doc.createElement("qdbapi");
		doc.appendChild(rootElement);
		Element elem = null;
		for (Map.Entry<String, Object> entry : Util.showFields(obj).entrySet()) {
			if (entry.getValue() instanceof String) {
				elem = doc.createElement(entry.getKey());
				elem.appendChild(doc.createTextNode((String) entry.getValue()));
				rootElement.appendChild(elem);
			} else if (entry.getValue() instanceof HashMap) {
				for (Map.Entry<String, String> entry1 : ((HashMap<String, String>) entry.getValue()).entrySet()) {
					Element field = doc.createElement(entry.getKey());
					field.appendChild(doc.createTextNode(entry1.getValue()));
					rootElement.appendChild(field);
					Attr attr = doc.createAttribute("name");
					attr.setValue(entry1.getKey());
					field.setAttributeNode(attr);
				}
			}
		}
		return xmlObjToString(doc);
	}
}
