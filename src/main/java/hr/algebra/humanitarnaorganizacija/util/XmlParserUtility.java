package hr.algebra.humanitarnaorganizacija.util;

import hr.algebra.humanitarnaorganizacija.exception.XmlException;
import hr.algebra.humanitarnaorganizacija.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class XmlParserUtility {
    private static final Logger log = LoggerFactory.getLogger(XmlParserUtility.class);

    private XmlParserUtility(){}

    public static List<Organisation> parse_Organisations(InputStream xml, List<Country> countries, List<Mission> missions, List<Volunteer> volunteers, List<Sponsor> sponsors, List<Campaign> campaigns) {
        if (xml == null) {
            String msg = "XML file not found";
            log.error(msg);
            throw new XmlException(msg);
        }
        //def container type to persist Organisations
        List<Organisation> organisations = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //create XML parser instance
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);
            Document document = factory.newDocumentBuilder().parse(xml); //create xml parser
            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName("organisation"); //taking nodes from root organisations
            for (int i = 0; i < nodes.getLength(); i++) {
                Element el = (Element) nodes.item(i); //organisation
                Organisation o = new Organisation();
                o.setTitle(text(el, "title"));
                o.setYearEstablishment(Integer.parseInt(text(el, "establishmentYear")));
                o.setNumOfEmployees(Integer.parseInt(text(el, "numOfEmployees")));
                o.setYearlyBudget(Double.parseDouble(text(el, "yearlyBudget")));
                o.setEndGoal(text(el, "endGoal"));
                o.setLogo(textOptional(el, "logo"));
                o.setCountry(findByName(text(el, "country"), countries, Country::getStateName, "country"));
                o.setMission(findByName(text(el, "mission"), missions, Mission::getMissionTitle, "mission"));
                o.setVolunteer(findPerson(text(el, "volunteerName"), text(el, "volunteerSurname"), volunteers, "volunteer"));
                o.setSponsor(findPerson(text(el, "sponsorName"), text(el, "sponsorSurname"), sponsors, "sponsor"));
                o.setCampaign(findByName(text(el, "campaign"), campaigns, Campaign::getCampaignTitle, "campaign"));
                organisations.add(o);       //add to list
            }
        } catch (XmlException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Error while parsing XML file";
            log.error(msg, e);
            throw new XmlException(msg, e);
        }
        return organisations;
    }


    private static <T> T findByName(String name, List<T> values, Function<T, String> nameGetter, String label) {
        return values.stream().filter(value -> nameGetter.apply(value).equalsIgnoreCase(name)).findFirst().orElseThrow(() -> {
            String msg = "Non-existent value in XML (" + label + "): " + name;
            log.error(msg);
            return new XmlException(msg);
        });
    }

    private static <T extends Person> T findPerson(String name, String surname, List<T> people, String label) {
        return people.stream().filter(p -> p.getName().equalsIgnoreCase(name) && p.getSurName().equalsIgnoreCase(surname)).findFirst().orElseThrow(() -> {
            String msg = "Non-existent person in XML (" + label + "): " + name + " " + surname;
            log.error(msg);
            return new XmlException(msg);
        });
    }

    private static String textOptional(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        String value = nodes.item(0).getTextContent().trim();
        return value.isBlank() ? null : value;
    }

    private static String text(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        //ovdje mi je tagName npr title - iz node-a uzimam TagName - parent je element iz petlje
        if (nodes.getLength() == 0) {
            //ako ne postoji tag unutar elementa baci grešku
            String msg = "Missing XML element: " + tagName;
            log.error(msg);
            throw new XmlException(msg);
        }
        String value = nodes.item(0).getTextContent().trim();
        //ovdje izvlačim tekst iz tog tagName-a value npr.= <Title>GreenFutureAlliance</Title>
        if (value.isBlank()) {
            String msg = "XML element must not be empty " + tagName;
            log.error(msg);
            throw new XmlException(msg);
        }
        return value;
    }


}
