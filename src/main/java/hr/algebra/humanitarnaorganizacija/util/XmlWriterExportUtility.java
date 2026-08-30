package hr.algebra.humanitarnaorganizacija.util;

import hr.algebra.humanitarnaorganizacija.exception.XmlException;
import hr.algebra.humanitarnaorganizacija.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class XmlWriterExportUtility {

    //LOGGER
    private static final Logger log = LoggerFactory.getLogger(XmlWriterExportUtility.class);

    private XmlWriterExportUtility() {
    }

    //XMLStreamWriter WRITER  = XMLOutputFactory.newInstance().create

    public static void export_Backup(List<Country> countries, List<Mission> missions, List<Volunteer> volunteers,
                                     List<Sponsor> sponsors, List<Campaign> campaigns, List<Organisation> organisations,
                                     String path) {
        try (FileWriter fWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fWriter);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("backup");

            writer.writeStartElement("countries");
            for (Country c : countries) {
                writer.writeStartElement("country");
                writeElement(writer, "stateName", c.getStateName());
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement("missions");
            for (Mission m : missions) {
                writer.writeStartElement("mission");
                writeElement(writer, "missionTitle", m.getMissionTitle());
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement("volunteers");
            for (Volunteer v : volunteers) {
                writer.writeStartElement("volunteer");
                writeElement(writer, "name", v.getName());
                writeElement(writer, "surname", v.getSurName());
                writeElement(writer, "specialisation", v.getSpecialisation());
                writeElement(writer, "hoursNum", String.valueOf(v.getHoursNum()));
                writeElement(writer, "status", v.getVolunteerStatus().toString());
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement("sponsors");
            for (Sponsor s : sponsors) {
                writer.writeStartElement("sponsor");
                writeElement(writer, "name", s.getName());
                writeElement(writer, "surname", s.getSurName());
                writeElement(writer, "donatorType", s.getDonatorType().toString());
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement("campaigns");
            for (Campaign c : campaigns) {
                writer.writeStartElement("campaign");
                writeElement(writer, "campaignTitle", c.getCampaignTitle());
                writeElement(writer, "budget", String.valueOf(c.getBudget()));
                writeElement(writer, "deadline", c.getDeadLine());
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement("organisations");
            for (Organisation o : organisations) {
                writer.writeStartElement("organisation");
                writeElement(writer, "title", o.getTitle());
                writeElement(writer, "establishmentYear", String.valueOf(o.getYearEstablishment()));
                writeElement(writer, "numOfEmployees", String.valueOf(o.getNumOfEmployees()));
                writeElement(writer, "yearlyBudget", String.valueOf(o.getYearlyBudget()));
                writeElement(writer, "endGoal", o.getEndGoal());
                writeElement(writer, "logo", o.getLogo());
                writeElement(writer, "country", o.getCountry().getStateName());
                writeElement(writer, "mission", o.getMission().getMissionTitle());
                writeElement(writer, "volunteerName", o.getVolunteer().getName());
                writeElement(writer, "volunteerSurname", o.getVolunteer().getSurName());
                writeElement(writer, "sponsorName", o.getSponsor().getName());
                writeElement(writer, "sponsorSurname", o.getSponsor().getSurName());
                writeElement(writer, "campaign", o.getCampaign().getCampaignTitle());
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeEndElement(); // backup
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (IOException | XMLStreamException e) {
            String msg = "Can not write Database Backup to XML";
            log.error(msg, e);
            throw new XmlException(msg, e);
        }
    }

    public static void export_Organisations(List<Organisation> organisations, String path) {
        try (FileWriter fWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fWriter);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("organisations");

            for (Organisation o : organisations) {
                writer.writeStartElement("organisation");
                writeElement(writer, "title", o.getTitle());
                writeElement(writer, "establishmentYear", String.valueOf(o.getYearEstablishment()));
                writeElement(writer, "numOfEmployees", String.valueOf(o.getNumOfEmployees()));
                writeElement(writer, "yearlyBudget", String.valueOf(o.getYearlyBudget()));
                writeElement(writer, "endGoal", o.getEndGoal());
                writeElement(writer, "logo", o.getLogo());
                writeElement(writer, "country", o.getCountry().getStateName());
                writeElement(writer, "mission", o.getMission().getMissionTitle());
                writeElement(writer, "volunteerName", o.getVolunteer().getName());
                writeElement(writer, "volunteerSurname", o.getVolunteer().getSurName());
                writeElement(writer, "sponsorName", o.getSponsor().getName());
                writeElement(writer, "sponsorSurname", o.getSponsor().getSurName());
                writeElement(writer, "campaign", o.getCampaign().getCampaignTitle());

                writer.writeEndElement(); //organisation
            }


            writer.writeEndElement(); // organisations
            writer.writeEndDocument();

            writer.flush();
            writer.close();
        } catch (IOException | XMLStreamException e) {
            String msg = "Can not write Organisation to XML";
            log.error(msg, e);
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
