<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.lastUpdateMoment" path= "lastUpdateMoment" width="20%"/>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.resolutionPercentage" path= "resolutionPercentage" width="20%"/>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.status" path= "status" width="20%"/>
	<acme:list-column code = "assistance-agent.tracking-log.list.label.reclaim" path= "reclaim" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${!finish}">
	<acme:button code="assistance-agent.tracking-log.list.label.create" action="/assistance-agent/tracking-log/create?claimId=${claimId}"/>
</jstl:if>
<jstl:if test="${finish && published}">
	<acme:button code="assistance-agent.tracking-log.list.label.reclaim" action="/assistance-agent/tracking-log/reclaim?claimId=${claimId}"/>
</jstl:if>