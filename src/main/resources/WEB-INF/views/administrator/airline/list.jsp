<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airline.list.label.name" path="name" width="10%"/>
	<acme:list-column code="administrator.airline.list.label.iata-code" path="iataCode" width="5%"/>
	<acme:list-column code="administrator.airline.list.label.type" path="type"/>
</acme:list>

<acme:button code="administrator.airline.list.button.create" action="/administrator/airline/create"/>