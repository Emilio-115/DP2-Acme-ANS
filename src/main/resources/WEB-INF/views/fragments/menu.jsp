<%-- - menu.jsp - - Copyright (C) 2012-2025 Rafael Corchuelo. - - In keeping with the traditional purpose of furthering
  education and research, it is - the policy of the copyright owner to permit non-commercial use and redistribution of -
  this software. It has been tested carefully, but it is not guaranteed for any particular - purposes. The copyright
  owner does not offer any warranties or representations, nor do - they accept any liabilities with respect to them.
  --%>

  <%@page%>

    <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
      <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
        <%@taglib prefix="acme" uri="http://acme-framework.org/" %>

          <acme:menu-bar>
            <acme:menu-left>
              <acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
                <acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/" />
                <acme:menu-suboption code="master.menu.anonymous.student1-favourite-link"
                  action="https://eelslap.com/" />
                <acme:menu-suboption code="master.menu.anonymous.student2-favourite-link"
                  action="https://cataas.com/" />
                <acme:menu-suboption code="master.menu.anonymous.student3-favourite-link"
                  action="https://fasterthanli.me/" />
                <acme:menu-suboption code="master.menu.anonymous.student4-favourite-link" action="https://github.com" />
              </acme:menu-option>

              <acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
                <acme:menu-suboption code="master.menu.administrator.list-user-accounts"
                  action="/administrator/user-account/list" />
                <acme:menu-separator />
                <acme:menu-suboption code="master.menu.administrator.list-airports"
                  action="/administrator/airport/list" />
                <acme:menu-separator />
                <acme:menu-suboption code="master.menu.administrator.populate-db-initial"
                  action="/administrator/system/populate-initial" />
                <acme:menu-suboption code="master.menu.administrator.populate-db-sample"
                  action="/administrator/system/populate-sample" />
                <acme:menu-separator />
                <acme:menu-suboption code="master.menu.administrator.shut-system-down"
                  action="/administrator/system/shut-down" />
              </acme:menu-option>

              <acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
                <acme:menu-suboption code="master.menu.customer.booking" action="/customer/booking/list" />
                <acme:menu-suboption code="master.menu.customer.passenger" action="/customer/passenger/list-all" />
                <acme:menu-suboption code="master.menu.customer.booking-record"
                  action="/customer/booking-record/list" />
              </acme:menu-option>

              <acme:menu-option code="master.menu.flight-crew-member" access="hasRealm('FlightCrewMember')">
                <acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list-planned"
                  action="/flight-crew-member/flight-assignment/list-planned" />
                <acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list-departed"
                  action="/flight-crew-member/flight-assignment/list-departed" />
                <acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list-drafts"
                  action="/flight-crew-member/flight-assignment/list-drafts" />
                <acme:menu-separator />
                <acme:menu-suboption code="master.menu.flight-crew-member.activity-log.list-published"
                  action="/flight-crew-member/activity-log/list" />
                <acme:menu-suboption code="master.menu.flight-crew-member.activity-log.list-drafts"
                  action="/flight-crew-member/activity-log/list-drafts" />
              </acme:menu-option>

              <acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
                <acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/" />
              </acme:menu-option>

              <acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
                <acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/" />
              </acme:menu-option>

              <acme:menu-option code="master.menu.airline-manager" access="hasRealm('AirlineManager')">
                <acme:menu-suboption code="master.menu.airline-manager.my-flights"
                  action="/airline-manager/flight/list" />
              </acme:menu-option>

            </acme:menu-left>

            <acme:menu-right>
              <acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
                <acme:menu-suboption code="master.menu.user-account.general-profile"
                  action="/authenticated/user-account/update" />
                <acme:menu-suboption code="master.menu.user-account.become-provider"
                  action="/authenticated/provider/create" access="!hasRealm('Provider')" />
                <acme:menu-suboption code="master.menu.user-account.provider-profile"
                  action="/authenticated/provider/update" access="hasRealm('Provider')" />
                <acme:menu-suboption code="master.menu.user-account.become-consumer"
                  action="/authenticated/consumer/create" access="!hasRealm('Consumer')" />
                <acme:menu-suboption code="master.menu.user-account.consumer-profile"
                  action="/authenticated/consumer/update" access="hasRealm('Consumer')" />
              </acme:menu-option>
            </acme:menu-right>
          </acme:menu-bar>
