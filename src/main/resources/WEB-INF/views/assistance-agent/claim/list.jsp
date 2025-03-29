<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "assistance-agent.claim.list.label.registrationmoment" path= "registrationMoment" width="25%"/>
	<acme:list-column code = "assistance-agent.claim.list.label.isaccepted" path= "isAccepted" width="25%"/>
	<acme:list-column code = "assistance-agent.claim.list.label.type" path= "type" width="25%"/>
	<acme:list-column code = "assistance-agent.claim.list.label.leg" path= "leg" width="25%"/>
</acme:list>

<acme:button code="assistance-agent.claim.list.label.create" action="/assistance-agent/claim/create"/>