<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "customer.passenger.list.label.passportnumber" path= "passportNumber"/>
	<acme:list-column code = "customer.passenger.list.label.draftmode" path = "draftMode"/>
	<acme:list-column code = "customer.passenger.list.label.birthdate" path = "birthDate"/>
	<acme:list-payload path="payload"/>
</acme:list>
<jstl:if test="${_command == 'list-all'}">
	<acme:button code="customer.passenger.list-all.label.create" action="/customer/passenger/create"/>
</jstl:if>
