<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="${readonly}"/>
	<acme:input-textbox code="assistance-agent.tracking-log.form.label.undergoingStep" path="undergoingStep" readonly="${readonly}"/>
	<acme:input-double code="assistance-agent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage" readonly="${readonly}"/>
	<acme:input-select code="assistance-agent.tracking-log.form.label.status" path="status" choices="${statuses}" readonly="${readonly}"/>
	<acme:input-select code="assistance-agent.tracking-log.form.label.claim" path="claim" choices="${claimOptions}" readonly="${readonly}"/>
	<acme:input-textarea code="assistance-agent.tracking-log.form.label.resolution" path="resolution" readonly="${readonly}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish')}">
    		<jstl:if test="${draftMode == true}">
    			<acme:submit code="assistance-agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
    			<acme:submit code="assistance-agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update"/>
    			<acme:submit code="assistance-agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
    		</jstl:if>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>