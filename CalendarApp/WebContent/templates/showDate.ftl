<#include "header.ftl">

<b>Welcome to our little demonstration on the Calendar Webapp</b><br><br>

	
        <p>Please enter your preferred date.</p>
        
        <form method="POST" action="gmgui?action=createDate&gid=${gid}&aid=${aid}">
			<div>
				<label>Date</label>
				<input type="text" name="date" value="31/01/2021 12:00" placeholder="dd/MM/yyyy HH:mm">
		    </div>
			<button type="submit" value="Submit" id="createDote">Submit date!</button>
		</form>
		<a href="gmgui?gid=${gid}"><button>Cancel</button></a>
<#include "footer.ftl">