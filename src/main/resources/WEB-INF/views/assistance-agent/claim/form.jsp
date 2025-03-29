<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationmoment" path="registrationMoment"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengeremail" path="passengerEmail"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description" />
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
	<jstl:if test="${condition}">
    	<acme:input-checkbox code="assistance-agent.claim.form.label.isaccepted" path="isAccepted"/>
	</jstl:if>
	<acme:input-integer code="assistance-agent.claim.form.label.leg" path="leg"/>
	
	<jstl:if test="${!readonly}">
		<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
	</jstl:if>
	
</acme:form>