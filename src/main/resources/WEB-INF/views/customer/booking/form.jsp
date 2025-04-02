<%@page%>

  <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="acme" uri="http://acme-framework.org/" %>

      <acme:form>
        <acme:input-textbox code="customer.booking.form.label.locatorcode" path="locatorCode" />
        <acme:input-select code="customer.booking.form.label.travelclass" path="travelClass"
          choices="${travelClasses}" />
        <acme:input-textbox code="customer.booking.form.label.creditcardlastnibble" path="creditCardLastNibble" />
        <acme:input-money code="customer.booking.form.label.price" path="price"/>


        <jstl:choose>
          <jstl:when test="${acme:anyOf(_command,'show|update|delete|publish')}">
            <acme:input-moment code="customer.booking.form.label.purchasemoment" path="purchaseMoment"
              readonly="true" />
            <acme:input-select code="customer.booking.flight.form.label.tag" path="flight"
              choices="${flightTagChoices}" />
            <acme:input-textbox code="customer.booking.flight.form.label.origin" path="origin" readonly="true" />
            <acme:input-textbox code="customer.booking.flight.form.label.destination" path="destination"
              readonly="true" />
            <acme:input-moment code="customer.booking.flight.form.label.departureDate" path="departureDate"
              readonly="true" />
            <acme:input-moment code="customer.booking.flight.form.label.arrivalDate" path="arrivalDate"
              readonly="true" />
            <acme:input-checkbox code="customer.booking.flight.form.label.requiresSelfTransfer"
              path="flightSelfTransfer" readonly="true" />
            <acme:input-textbox code="customer.booking.flight.form.label.description" path="flightDescription"
              readonly="true" />
            <acme:input-textbox code="customer.booking.flight.form.label.numberOfLayovers" path="numberOfLayovers"
              readonly="true" />
            <acme:button code="customer.booking.form.label.passenger"
              action="/customer/passenger/list?bookingId=${id}" />
            <jstl:if test="${draftMode == true}">
              <acme:submit code="customer.booking.form.button.update" action="/customer/booking/update" />
              <acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete" />
              <acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish" />
            </jstl:if>
          </jstl:when>
          <jstl:when test="${_command == 'create'}">
            <acme:input-select code="customer.booking.flight.form.label.tag" path="flight"
              choices="${flightTagChoices}" />
            <acme:submit code="customer.booking.form.button.create" action="/customer/booking/create" />
          </jstl:when>
        </jstl:choose>






      </acme:form>
