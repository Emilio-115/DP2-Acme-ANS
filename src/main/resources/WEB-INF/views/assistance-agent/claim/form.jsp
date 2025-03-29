<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description" />
	<acme:input-textbox code="assistance-agent.claim.form.label.type" path="type"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.isAccepted" path="isAccepted"/>
	<acme:input-integer code="assistance-agent.claim.form.label.leg" path="leg"/>
</acme:form>