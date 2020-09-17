/**
 * 
 */
$(document).ready(function(){
	var user = JSON.parse(localStorage.getItem('user'));
	console.log(user.username)
	$('#name').val(user.firstName)
	$('#surname').val(user.lastName)
	$('#username').val(user.username)
	
	$('form#update-form').submit(function(event){
		event.preventDefault()
		let firstName= $('#name').val()
		let lastName = $('#surname').val()
		let username = $('#username').val()
		let pass = $('#password').val()
		let confirm = $('#newPass').val()
		
		if(pass == "" && confirm == ""){
			alert('passwords...')
			return
		}else{
			if(pass !== confirm){
				alert('passwords not the same...')
				return
			}
		}
		
	
		//Send request for update
		$.ajax({
			type: "PUT",
			url: "rest/user",
			data: JSON.stringify({
				id: user.id,
				username: username,
				password: pass,
				firstName: firstName,
				lastName: lastName
			}),
			contentType: "application/json",
			success: function(user){
				localStorage.setItem("user", JSON.stringify(user));
				window.location.href = "homepage.html";
			},
			error: function(){
				alert('oops.. try again')
			}
		
		})
	})
})