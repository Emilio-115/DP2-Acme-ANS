<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="customer.booking-record.form.label.locatorcode" path="associatedBooking" choices = "${bookingChoices}"/>
	<acme:input-select code="customer.booking-record.form.label.passportnumber" path="associatedPassenger" choices = "${passengerChoices}"/>
	<jstl:if test="${_command == 'create'}">			
		<acme:submit code="customer.booking-record.form.label.create" action="/customer/booking-record/create"/>
	</jstl:if>
</acme:form>