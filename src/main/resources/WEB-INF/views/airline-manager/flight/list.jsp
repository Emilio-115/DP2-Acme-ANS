<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "manager.flight.list.label.tag" path= "tag"/>
	<acme:list-column code = "manager.flight.list.label.origin" path= "origin"/>
	<acme:list-column code = "manager.flight.list.label.destiny" path= "destiny"/>
	<acme:list-column code = "manager.flight.list.label.departureDate" path= "departureDate"/>
	<acme:list-column code = "manager.flight.list.label.arrivalDate" path= "arrivalDate"/>
</acme:list>