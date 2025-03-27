<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "customer.passenger.list.label.fullname" path= "fullName"/>
	<acme:list-column code = "customer.passenger.list.label.passportnumber" path= "passportNumber"/>
</acme:list>>
