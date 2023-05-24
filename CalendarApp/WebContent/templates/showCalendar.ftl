<#include "header.ftl">

<b>Welcome to our little demonstration on the Group Calendar Webapp</b><br><br>
<h2>Appointments</h2>

<table class="datatable" id="table">
	<tr>
		<th>AID</th>
		<th>Name</th>
		<th>Description</th>
		<th>Hand To</th>
		<th>Street</th>
		<th>Town</th>
		<th>ZIP</th>
		<th>Country</th>
		<th>Duration</th>
		<th>Date</th>
		<th>Deadline</th>

<#list appointmentRequests as ar>
                <tr>
                	<td>${ar.getId()}</td>
                    <td>${ar.getName()}</td>
                    <td>${ar.getDescription()}</td>
                    <td>${ar.location.getHandTo()}</td>
                    <td>${ar.location.getStreet()}</td>
                    <td>${ar.location.getTown()}</td>
                    <td>${ar.location.getZip()}</td>
                    <td>${ar.location.getCountry()}</td>
                    <td>${ar.getDuration()}</td>
                    <td>${(ar.getDateData().getDate()?datetime)?string("dd/MM/yyyy HH:mm")}</td>
                    <td>${(ar.getDeadline()?datetime)?string("dd/MM/yyyy HH:mm")}</td>
                </tr>
            </#list>
<a href="gmgui?gid=${gid}"><button>Back</button></a>

<#include "footer.ftl">