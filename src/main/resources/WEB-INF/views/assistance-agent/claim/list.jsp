<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "assistance-agent.claim.list.label.registrationMoment" path= "registrationMoment" width="25%"/>
	<acme:list-column code = "assistance-agent.claim.list.label.isAccepted" path= "isAccepted" width="20%"/>
	<acme:list-column code = "assistance-agent.claim.list.label.type" path= "type" width="20%"/>
</acme:list>>
