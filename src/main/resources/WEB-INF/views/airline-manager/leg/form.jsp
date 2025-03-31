<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code = "manager.leg.form.label.id" path= "id" readonly="true"/>
	<acme:input-moment code = "manager.leg.form.label.departureDate" path= "departureDate"/>
	<acme:input-moment code = "manager.leg.form.label.arrivalDate" path= "arrivalDate" />
	<acme:input-select code = "manager.leg.form.label.status" path= "status"  choices="${statusChoices}" readonly="true"/>
	<acme:input-textbox code = "manager.leg.form.label.flightNumberDigits" path="flightNumberDigits"/>
	<acme:input-textbox code = "manager.leg.form.label.flightNumber" path="flightNumber" readonly="true"/>
	<acme:input-select code = "manager.leg.form.label.departureAirport" path= "departureAirport"  choices="${departureAirportChoices}"/>
	<acme:input-select code = "manager.leg.form.label.arrivalAirport" path= "arrivalAirport"  choices="${arrivalAirportChoices}"/>
	<acme:input-select code = "manager.leg.form.label.aircraft" path= "aircraft"  choices="${aircraftChoices}"/>

	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="manager.leg.form.button.legs" action="/airline-manager/leg/list?flightId=${flightId}"/>					
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="manager.leg.form.button.update" action="/airline-manager/leg/update"/>
			<acme:submit code="manager.leg.form.button.delete" action="/airline-manager/leg/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.leg.form.button.create" action="/airline-manager/leg/create?flightId=${flightId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>



