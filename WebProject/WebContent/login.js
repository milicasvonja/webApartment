/**
 * 
 */
$(document).ready(function(){
	
	
	$('#login-form').submit(function(event){
		event.preventDefault()
		let username = $('#username').val()
		let password = $('#password').val()
		if(username=="" || password==""){
			alert('fill all fields!')
			return;
		}
		//sending ajax request
		$.ajax({
			type:"POST",
			url:"rest/user/login",
			data: JSON.stringify({
				username: username,
				password: password
			}),
			contentType: "application/json",
			success: function(user){
				localStorage.setItem("user", JSON.stringify(user));
				window.location.href = "homepage.html";
			},
			error: function(){
				console.log('something bad happened...')
				$('#error').text('opps.. try again!')
			}
		})
	})
	
	
})