package hr.algebra.humanitarnaorganizacija.util;
import hr.algebra.humanitarnaorganizacija.exception.XmlException;
import hr.algebra.humanitarnaorganizacija.poco.UserActionLogg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;

public final class UserActionLoggerUtility {

    private static final String LOG_PATH = "./user-actions-log.xml";

    private static final Logger logger = LoggerFactory.getLogger(UserActionLoggerUtility.class);

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private UserActionLoggerUtility() {
    }

    //1)
    public static void log(String username, String action, String details) {
        String timestamp = LocalDateTime.now().format(FORMAT);
        UserActionLogg entry = new UserActionLogg(action, username, timestamp, details);
        List<UserActionLogg> entries = readExisting();
        entries.add(entry);
        writeAll(entries);
    }

    private static List<UserActionLogg> readExisting() {
        List<UserActionLogg> entries = new ArrayList<>();
        File file = new File(LOG_PATH);

        if (!file.exists()) {
            return entries;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);
            Document document = factory.newDocumentBuilder().parse(file);
            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName("entry");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String action = text(element, "action");
                String username = text(element, "username");
                String timestamp = text(element, "timestamp");
                String details = text(element, "details");
                entries.add(new UserActionLogg(action, username, timestamp, details));
            }
        } catch (Exception e) {
            String msg = "Error while reading user action log";
            logger.error(msg, e);
            throw new XmlException(msg, e);
        }
        return entries;
    }

    private static String text(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return "";
        }
        return nodes.item(0).getTextContent().trim();
    }

    private static void writeAll(List<UserActionLogg> entries) {
        try (FileWriter fWriter = new FileWriter(LOG_PATH, StandardCharsets.UTF_8)) {
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fWriter);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("actions");
            for (UserActionLogg entry : entries) {
                writer.writeStartElement("entry");
                writeElement(writer, "timestamp", entry.getTimestamp());
                writeElement(writer, "username", entry.getUsername());
                writeElement(writer, "action", entry.getAction());
                writeElement(writer, "details", entry.getDetails());
                writer.writeEndElement(); //entry
            }
            writer.writeEndElement(); //entries
            writer.writeEndDocument();
            writer.flush();
            writer.close();

        } catch (IOException | XMLStreamException e) {
            String msg = "Error while writing user action log";
            logger.error(msg, e);
            throw new XmlException(msg, e);
        }
    }

    private static void writeElement(XMLStreamWriter writer, String tagName, String value) throws XMLStreamException {
        writer.writeStartElement(tagName);
        if (value != null) {
            writer.writeCharacters(value);
        }
        writer.writeEndElement();
    }


}
