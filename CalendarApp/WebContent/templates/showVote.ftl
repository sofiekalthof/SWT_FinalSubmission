<#include "header.ftl">

<b>Welcome to our little demonstration on the Calendar Webapp</b><br><br>

	
        <p style="font-weight=bold;">Are you sure that you want to vote for this date? Your vote will be final!</p>
        
        <form method="POST" action="gmgui?action=createVote&gid=${gid}&did=${did}">
			<div>
				
		    </div>
			<button type="submit" value="Submit" id="createVote">Vote!</button>
		</form>
		<a href="gmgui?gid=${gid}"><button>Cancel</button></a>

<#include "footer.ftl">