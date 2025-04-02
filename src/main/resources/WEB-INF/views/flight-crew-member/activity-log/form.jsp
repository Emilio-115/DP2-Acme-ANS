<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${_command == 'show' && draftMode == false}">
		<acme:input-moment code="flight-crew-member.activity-log.form.label.registered-at" path="registeredAt" readonly="true"/>
	</jstl:if>
	
	<acme:input-select code="flight-crew-member.activity-log.form.label.registering-assignment" path="registeringAssignment" choices="${registeringAssignments}"/>
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.incident-type" path="incidentType"/>
	<acme:input-textarea code="flight-crew-member.activity-log.form.label.description" path="description"/>
	<acme:input-integer code="flight-crew-member.activity-log.form.label.severity-level" path="severityLevel" />
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="flight-crew-member.activity-log.form.button.update" action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.activity-log.form.button.create" action="/flight-crew-member/activity-log/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>