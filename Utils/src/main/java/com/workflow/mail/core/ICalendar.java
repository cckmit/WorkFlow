package com.workflow.mail.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import net.fortuna.ical4j.agent.VEventUserAgent;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.XParameter;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Trigger;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.RandomUidGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

public class ICalendar {

    private static final Logger LOG = Logger.getLogger(ICalendar.class.getName());

    private static ProdId prodId = new ProdId("-//Ben Fortuna//iCal4j 3.0.7//EN");
    // private DateTime startDateTime;
    // private long durationsMins;
    // private String subject;
    // private String description;
    private String organiser;
    private List<Address> attendees;
    VEventUserAgent agent;
    Organizer organizer;

    public ICalendar() {

	// TimeZoneRegistry registry =
	// TimeZoneRegistryFactory.getInstance().createRegistry();
	// TimeZone timezone = registry.getTimeZone("GMT");
	// tz = timezone.getVTimeZone();
    }

    private void createAgent() throws URISyntaxException {
	organizer = new Organizer("mailto:" + organiser);
	organizer.getParameters().add(Role.REQ_PARTICIPANT);
	organizer.getParameters().add(new Cn("zTPFM Devops Toolchain"));
	agent = new VEventUserAgent(prodId, organizer, new RandomUidGenerator());
    }

    private Calendar readCalendar(String pCalendarString) throws Exception {
	StringReader sin = new StringReader(pCalendarString);
	CalendarBuilder builder = new CalendarBuilder();
	Calendar calendar = builder.build(sin);
	return calendar;
    }

    private void setDescription(VEvent event, String pDescription) {
	ParameterList htmlParameters = new ParameterList();
	XParameter fmtTypeParameter = new XParameter("FMTTYPE", "text/html");
	htmlParameters.add(fmtTypeParameter);
	XProperty htmlProp = new XProperty("X-ALT-DESC", htmlParameters, pDescription);
	event.getProperties().add(htmlProp);

	event.getProperties().forEach(pro -> {
	    if (pro.getName().equals("X-ALT-DESC")) {
		try {
		    pro.setValue(pDescription);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    private VEvent getNewEvent(DateTime pStart, long pMins, String pDescription, String pSubject) {
	VEvent event = new VEvent(pStart, java.time.Duration.ofMinutes(pMins), pSubject);
	setDescription(event, pDescription);
	VAlarm alarm = new VAlarm();
	alarm.getProperties().add(new Trigger(new Dur(0, 0, -15, 0)));
	alarm.getProperties().add(Action.DISPLAY);
	event.getAlarms().add(alarm);
	return event;
    }

    private VEvent getNewEvent(net.fortuna.ical4j.model.Date pStart, net.fortuna.ical4j.model.Date pEnd, String pDescription, String pSubject) {
	VEvent event = new VEvent(pStart, pEnd, pSubject);
	setDescription(event, pDescription);
	VAlarm alarm = new VAlarm();
	alarm.getProperties().add(new Trigger(new Dur(0, 0, -15, 0)));
	alarm.getProperties().add(Action.DISPLAY);
	event.getAlarms().add(alarm);
	return event;
    }

    private VEvent getEvent(Calendar pCalendar) {
	VEvent event = (VEvent) pCalendar.getComponent(VEvent.VEVENT);
	return event;
    }

    private String saveCalendar(Calendar pCalendar, String pFileName) throws Exception {
	try (FileOutputStream fout = new FileOutputStream(pFileName, false)) {
	    CalendarOutputter outputter = new CalendarOutputter();
	    outputter.output(pCalendar, fout);
	}
	String lReturn = "";

	try (StringWriter sout = new StringWriter()) {
	    CalendarOutputter outputter = new CalendarOutputter();
	    outputter.output(pCalendar, sout);
	    lReturn = sout.toString();
	}
	return lReturn;
    }

    public String getCalendar(String pFileName, Date startTime, int duration, String description, String subject) throws Exception {
	return createCalendarEvent(pFileName, null, null, startTime, duration, description, subject, false);
    }

    public String getCalendar(String pFileName, Date startDate, Date endDate, String description, String subject) throws Exception {
	return createCalendarEvent(pFileName, startDate, endDate, null, 0, description, subject, false);
    }

    public String getCalendar(String pFileName, Date startDate, String description, String subject) throws Exception {
	Date formattedDate = DateUtils.truncate(startDate, java.util.Calendar.DATE);
	return createCalendarEvent(pFileName, formattedDate, DateUtils.addDays(formattedDate, 1), null, 0, description, subject, false);
    }

    public String cancelCalendar(String pFileName) throws Exception {
	return createCalendarEvent(pFileName, null, null, null, 0, null, null, true);
    }

    private String createCalendarEvent(String pFileName, Date startDate, Date endDate, Date startDateTime, int duration, String description, String subject, boolean isCancelled) throws Exception {
	Calendar calendar = null;
	VEvent event = null;
	boolean update = false;
	File lCalendarFile = new File(pFileName);

	createAgent();
	if (lCalendarFile.exists()) {
	    String lOldCalendarContent = FileUtils.readFileToString(lCalendarFile, "UTF-8");
	    LOG.info(lOldCalendarContent);
	    Calendar lOldCalendar = readCalendar(lOldCalendarContent);
	    event = getEvent(lOldCalendar);
	    update = true;
	} else {
	    if (startDateTime != null) {
		DateTime lStartDateTime = new DateTime(startDateTime);
		lStartDateTime.setUtc(true);
		event = getNewEvent(lStartDateTime, duration, description, subject);
	    } else if (startDate != null && endDate != null) {
		event = getNewEvent(new net.fortuna.ical4j.model.Date(startDate), new net.fortuna.ical4j.model.Date(endDate), description, subject);
	    }
	    calendar = agent.request(event);
	}
	if (update) {
	    if (isCancelled) {
		event.getAlarms().clear();
		calendar = agent.cancel(event);
	    } else {
		if (startDateTime != null) {
		    DtStart lStart = event.getProperties().getProperty(Property.DTSTART);
		    DateTime lStartDateTime = new DateTime(startDateTime);
		    lStartDateTime.setUtc(true);
		    lStart.setDate(lStartDateTime);

		    Duration lDuration = event.getProperties().getProperty(Property.DURATION);
		    lDuration.setDuration(java.time.Duration.ofMinutes(duration));
		} else {
		    DtStart lStart = event.getProperties().getProperty(Property.DTSTART);
		    net.fortuna.ical4j.model.Date lStartDateTime = new net.fortuna.ical4j.model.Date(startDate);
		    lStart.setDate(lStartDateTime);

		    DtStart lEnd = event.getProperties().getProperty(Property.DTEND);
		    net.fortuna.ical4j.model.Date lEndDateTime = new net.fortuna.ical4j.model.Date(endDate);
		    lEnd.setDate(lEndDateTime);
		}

		setDescription(event, description);

		Summary lSummary = event.getProperties().getProperty(Property.SUMMARY);
		lSummary.setValue(subject);
		calendar = agent.request(event);
	    }
	}
	if (!isCancelled) {
	    event.getProperties().removeAll(event.getProperties(Property.ATTENDEE));
	    for (Address attende : attendees) {
		Attendee lAttendee = new Attendee("mailto:" + attende.getAddress());
		lAttendee.getParameters().add(Role.REQ_PARTICIPANT);
		lAttendee.getParameters().add(new Cn(attende.getName()));
		event.getProperties().add(lAttendee);
	    }
	}
	return saveCalendar(calendar, pFileName);
    }

    // public DateTime getStartDateTime() {
    // return startDateTime;
    // }
    //
    // public void setStartDateTime(DateTime startDateTime) {
    // this.startDateTime = startDateTime;
    // this.startDateTime.setUtc(true);
    // }
    //
    // public void setStartDateTime(Date startDateTime) {
    // this.startDateTime = new DateTime(startDateTime);
    // this.startDateTime.setUtc(true);
    // }
    //
    // public long getDurationsMins() {
    // return durationsMins;
    // }
    //
    // public void setDurationsMins(long durationsMins) {
    // this.durationsMins = durationsMins;
    // }
    //
    // public String getSubject() {
    // return subject;
    // }
    //
    // public void setSubject(String subject) {
    // this.subject = subject;
    // }
    //
    // public String getDescription() {
    // return description;
    // }
    //
    // public void setDescription(String description) {
    // this.description = description;
    // }
    public String getOrganiser() {
	return organiser;
    }

    public void setOrganiser(String organiser) {
	this.organiser = organiser;
    }

    public List<Address> getAttendees() {
	return attendees;
    }

    public void setAttendees(List<Address> attendees) {
	this.attendees = attendees;
    }
}
