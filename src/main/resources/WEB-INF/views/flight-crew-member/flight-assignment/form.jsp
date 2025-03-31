<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${_command == 'show' && draftMode == false}">
		<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.flight-crew-member" path="flightCrewMember" readonly="true"/>
	</jstl:if>
	
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices="${legs}" readonly="${draftMode==false}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${duties}" readonly="${draftMode==false}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices="${statuses}" readonly="${draftMode==false}"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks" readonly="${draftMode==false}"/>
	
	<jstl:choose>	 
		<jstl:when test="${((_command == 'show' && draftMode == false && status == 'PENDING') || acme:anyOf(_command, 'confirm|cancel')) && canEdit == true}">		
			<acme:submit code="flight-crew-member.flight-assignment.form.button.confirm" action="/flight-crew-member/flight-assignment/confirm"/>	
			<acme:submit code="flight-crew-member.flight-assignment.form.button.cancel" action="/flight-crew-member/flight-assignment/cancel"/>	
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>