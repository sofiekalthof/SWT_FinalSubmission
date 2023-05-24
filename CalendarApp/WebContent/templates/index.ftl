<#include "header.ftl">

<b>Welcome to our little demonstration on the Group Calendar Webapp</b><br><br>

<form method="POST" action="gmgui?action=SelectGID">
	<div>
		<label>GID</label>
		<input type="number" name="gid" value="1">
    </div>
	<button type="submit" value="Submit" id="SelectGID">Select!</button>
</form>

<#include "footer.ftl">