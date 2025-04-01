<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="customer.booking-record.form.label.locatorcode" path="associatedBooking" choices = "${bookingChoices}" readonly="${acme:anyOf(_command, 'show|delete')}"/>
	<acme:input-select code="customer.booking-record.form.label.passportnumber" path="associatedPassenger" choices = "${passengerChoices}" readonly="${acme:anyOf(_command, 'show|delete')}"/>
	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && canDelete == true}">
			<acme:submit code="customer.booking-record.form.button.delete" action="/customer/booking-record/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking-record.form.label.create" action="/customer/booking-record/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>

