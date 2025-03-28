<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "customer.passenger.list.label.email" path= "email"/>
	<acme:list-column code = "customer.passenger.list.label.passportnumber" path= "passportNumber"/>
	<acme:list-column code = "customer.passenger.list.label.draftmode" path = "draftMode"/>
</acme:list>>
