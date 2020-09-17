$(document).ready(()=>{
	$('#newApp-form').submit(search)
	getUsers();
})

const getUsers = () => {
	$("#users").empty();
	$.ajax({
    type: "GET",
    url: "rest/user",
    success: (users) => {
      users.forEach((user) => {
        $("#users").append(
          `<tr>
			   <td> ${user.id} </td>
		       <td> ${user.username} </td>
			   <td> ${user.firstName} </td>
		       <td> ${user.lastName} </td>
			   <td> ${user.gender} </td>
			   <td> ${user.role} </td>
			   <td> ${user.active == true ? "ACTIVE" : "BLOCK"} </td>
			   <td> <button onClick="changeStatus('${user.id}')" type="button" class="btn btn-info">Change status</button> </td>
		   </tr>`
        );
      });
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
}

const changeStatus = id => {
	console.log(id);
	
	$.ajax({
    type: "PUT",
    url: `rest/user/change-status/${id}`,
    success: (users) => {
      getUsers();
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
}

const search = event => {
	event.preventDefault();
	
	let username = $("#username").val();
	let gender = $("#gender option:selected").val();
	let role = $("#role option:selected").val();
	console.log(username);
	console.log(gender);
	console.log(role);
	
	
	$("#users").empty();
	$.ajax({
    type: "GET",
    url: `rest/user/search-all/admin/?username=${username}&role=${role}&gender=${gender}`,
    success: (users) => {
      users.forEach((user) => {
        $("#users").append(
          `<tr>
			   <td> ${user.id} </td>
		       <td> ${user.username} </td>
			   <td> ${user.firstName} </td>
		       <td> ${user.lastName} </td>
			   <td> ${user.gender} </td>
			   <td> ${user.role} </td>
			   <td> ${user.active == true ? "ACTIVE" : "BLOCK"} </td>
			   <td> <button onClick="changeStatus('${user.id}')" type="button" class="btn btn-info">Change status</button> </td>
		   </tr>`
        );
      });
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
}