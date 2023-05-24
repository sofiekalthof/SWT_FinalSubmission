<#include "header.ftl">

<b>Welcome to our little demonstration on the Group Calendar Webapp</b><br><br>
<a href="gmgui?gid=${gid}"><button>Back</button></a>
<form method="POST" action="gmgui?action=createAR&gid=${gid}">
	<fieldset id="addAppointment">
		<legend>Required Information</legend>
		<div>
			<label>Name</label>
			<input type="text" name="name" value="test">
	    </div>

		<div>
			<label>Description</label>
			<input type="text" name="description" value="test">
	    </div>
		<div>
			<div>
				<label>Hand to</label>
				<input type="text" name="locationHandTo" value="test">
			</div>
			<div>
				<label>Street</label>
				<input type="text" name="locationStreet" value="test">
			</div>
			<div>
				<label>Town</label>
				<input type="text" name="locationTown" value="test">
			</div>
			<div>
				<label>ZIP-Code</label>
				<input type="text" name="locationZip" value="test">
			</div>
			<div>
				<label>Country</label>
				<input type="text" name="locationCountry" value="test">
			</div>
	    </div>
	    <div>
	    	<label>Duration</label>
	    	<input type="number" name="duration" value="123" placeholder="in Minutes">
	    </div>
	    <div>
	    	<label>Date</label>
	    	<input type="text" name="date" value="31/01/2021 12:00" placeholder="dd/MM/yyyy HH:mm">
	    </div>
	    <div>
	    	<label>Deadline</label>
	    	<input type="text" name="deadline" value="30/01/2021 18:00" placeholder="dd/MM/yyyy HH:mm">
	    </div>
	    <div>
	    	<label>Participant-IDs</label>
	    	<input type="text" name="participants" value="2">
    	</div>
	</fieldset>
	<button type="submit" id="submit">Submit</button>
</form>
<#include "footer.ftl">