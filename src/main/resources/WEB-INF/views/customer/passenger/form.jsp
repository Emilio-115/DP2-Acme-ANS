<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.passenger.form.label.fullname" path="fullName"/>
	<acme:input-textbox code="customer.passenger.form.label.passportnumber" path="passportNumber"/>
	<acme:input-email code="customer.passenger.form.label.email" path="email"/>
	<acme:input-moment code="customer.passenger.form.label.birthdate" path="birthDate"/>
	<acme:input-textarea code="customer.passenger.form.label.specialneeds" path="specialNeeds"/>
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
			<acme:submit code="customer.passenger.form.button.delete" action="/customer/passenger/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger.form.label.create" action="/customer/passenger/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>