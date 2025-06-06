<%@page %>

  <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="acme" uri="http://acme-framework.org/" %>

      <acme:list>
        <acme:list-column code="customer.booking.list.label.locatorcode" path="locatorCode" />
        <acme:list-column code="customer.booking.list.label.purchasemoment" path="purchaseMoment" />
        <acme:list-column code="customer.booking.list.label.travelclass" path="travelClass" />
        <acme:list-column code="customer.booking.flight.list.label.destination" path="destination" />
        <acme:list-payload path="payload" />
      </acme:list>

      <acme:button code="customer.booking.list.label.create" action="/customer/booking/create" />
