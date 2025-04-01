<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code = "manager.leg.form.label.id" path= "id" readonly="true"/>
	<acme:input-moment code = "manager.leg.form.label.departureDate" path= "departureDate"/>
	<acme:input-moment code = "manager.leg.form.label.arrivalDate" path= "arrivalDate" />
	<acme:input-select code = "manager.leg.form.label.status" path= "status"  choices="${statusChoices}" />
	<acme:input-textbox code = "manager.leg.form.label.flightNumberDigits" path="flightNumberDigits"/>
	<acme:input-textbox code = "manager.leg.form.label.flightNumber" path="flightNumber" readonly="true"/>
	<acme:input-select code = "manager.leg.form.label.departureAirport" path= "departureAirport"  choices="${departureAirportChoices}"/>
	<acme:input-select code = "manager.leg.form.label.arrivalAirport" path= "arrivalAirport"  choices="${arrivalAirportChoices}"/>
	<acme:input-select code = "manager.leg.form.label.aircraft" path= "aircraft"  choices="${aircraftChoices}"/>

	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false && status=='ON_TIME'}">
			<acme:submit code="manager.leg.form.button.mark-as-delayed" action="/airline-manager/leg/delay"/>					
			<acme:submit code="manager.leg.form.button.cancel" action="/airline-manager/leg/cancel"/>					
			<acme:submit code="manager.leg.form.button.mark-as-landed" action="/airline-manager/leg/land"/>					
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="manager.leg.form.button.update" action="/airline-manager/leg/update"/>
			<acme:submit code="manager.leg.form.button.delete" action="/airline-manager/leg/delete"/>
			<acme:submit code="manager.leg.form.button.publish" action="/airline-manager/leg/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.leg.form.button.create" action="/airline-manager/leg/create?flightId=${flightId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>



