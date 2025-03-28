<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.passenger.form.label.fullname" path="fullName"/>
	<acme:input-textbox code="customer.passenger.form.label.passportnumber" path="passportNumber"/>
	<acme:input-email code="customer.passenger.form.label.email" path="email"/>
	<acme:input-moment code="customer.passenger.form.label.birthdate" path="birthDate"/>
	<acme:input-textarea code="customer.passenger.form.label.specialneeds" path="specialNeeds"/>
	<acme:input-checkbox code="customer.passenger.form.label.draftmode" path="draftMode"/>
</acme:form>