<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationmoment" path="registrationMoment" readonly="${readonly}"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengeremail" path="passengerEmail" readonly="${readonly}"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description" readonly="${readonly}"/>
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}" readonly="${readonly}"/>
	<acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices = "${landedLegs}" readonly="${readonly}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish')}">
    		<acme:input-checkbox code="assistance-agent.claim.form.label.isaccepted" path="isAccepted" readonly="${readonly}"/>
    		<jstl:if test="${draftMode == true}">
    			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
    			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
    		</jstl:if>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>