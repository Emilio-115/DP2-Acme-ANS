<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "manager.leg.list.label.departureAirport" path= "departureAirport" sortable="false"/>
	<acme:list-column code = "manager.leg.list.label.arrivalAirport" path= "arrivalAirport" sortable="false"/>
	<acme:list-column code = "manager.leg.list.label.departureDate" path= "departureDate" sortable="true"/>
	<acme:list-column code = "manager.leg.list.label.arrivalDate" path= "arrivalDate" sortable="false"/>
	<acme:list-column code = "manager.leg.list.label.status" path= "status" sortable="false"/>
	<acme:list-column code = "manager.leg.list.label.status" path= "draftMode" sortable="false"/>
</acme:list>

<jstl:if test="${_command == 'list' && draftMode==true}">
	<acme:button code="manager.leg.list.button.create" action="/airline-manager/leg/create?flightId=${flightId}"/>
</jstl:if>	