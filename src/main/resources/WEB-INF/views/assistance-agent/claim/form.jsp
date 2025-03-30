<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationmoment" path="registrationMoment" readonly="${readonly}"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengeremail" path="passengerEmail" readonly="${readonly}"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description" readonly="${readonly}"/>
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}" readonly="${readonly}"/>
	<jstl:if test="${acme:anyOf(_command,'show|update|delete|publish')}">
    	<acme:input-checkbox code="assistance-agent.claim.form.label.isaccepted" path="isAccepted" readonly="${readonly}"/>
	</jstl:if>
	<acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices = "${landedLegs}" readonly="${readonly}"/>
	
	<jstl:if test="${!readonly}">
		<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
	</jstl:if>
	
</acme:form>