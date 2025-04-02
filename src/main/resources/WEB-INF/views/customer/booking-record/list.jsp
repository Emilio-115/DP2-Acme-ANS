<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "customer.booking-record.list.label.locatorcode" path= "associatedBooking"/>
	<acme:list-column code = "customer.booking-record.list.label.passportnumber" path= "associatedPassenger"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="customer.booking-record.list.label.create" action="/customer/booking-record/create"/>