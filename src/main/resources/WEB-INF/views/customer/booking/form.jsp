<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.booking.form.label.locatorcode" path="locatorCode"/>
	<acme:input-moment code="customer.booking.form.label.purchasemoment" path="purchaseMoment"/>
	<acme:input-select code="customer.booking.form.label.travelclass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-money code="customer.booking.form.label.price" path="price"/>
	<acme:input-textbox code="customer.booking.form.label.creditcardlastnibble" path="creditCardLastNibble"/>
</acme:form>