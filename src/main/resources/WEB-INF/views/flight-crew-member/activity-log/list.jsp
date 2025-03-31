<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.flight-number" path= "flightNumber"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.incident-type" path= "incidentType"/>
	<acme:list-column code = "flight-crew-member.activity-log.list.label.severity-level" path= "severityLevel"/>
</acme:list>

<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create"/>