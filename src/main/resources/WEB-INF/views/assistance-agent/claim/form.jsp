<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationmoment" path="registrationMoment"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengeremail" path="passengerEmail" />
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description"/>
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}" />
	<acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices = "${landedLegs}" />
	<jstl:if test="${acme:anyOf(_command,'show|update|delete|publish')}">
    	<acme:input-checkbox code="assistance-agent.claim.form.label.isaccepted" path="isAccepted" />
    	<jstl:if test="${draftMode == true}">
    		<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
    		<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
    	</jstl:if>
	</jstl:if>
	<jstl:if test="${!readonly}">
		<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
	</jstl:if>
	
</acme:form>