/**
 * 
 */
$(document).ready(function(){
	
	console.log('mounted...')
	$('form#register-form').submit(function(event){
		event.preventDefault()
		let firstName = $('#name').val()
		let lastName = $('#surname').val()
		let username = $('#username').val()
		let password = $('#password').val()
		let confirm = $('#confirm').val()
		let genderString = $('#gender').val()
		let gender = parseInt(genderString)
		let roleString = $('#role').val()
		let role = parseInt(roleString)
		console.log(role)
		if(username=="" || password=="" ||firstName=="" || password=="" || roleString == ""){
			alert('fill all fields!')
			return;
		}
		
		if(password !== confirm ){
			alert('Passwords did not match')
			return;
		}
		//sending ajax request
		$.ajax({
			type:"POST",
			url:"rest/user",
			data: JSON.stringify({
				firstName: firstName,
				lastName: lastName,
				username: username,
				password: password,
				gender: gender,
				role: role,
			}),
			contentType: "application/json",
			success: function(user){
				localStorage.setItem("user", JSON.stringify(user));
				window.location.href = "homepage.html";
			},
			error: function(){
				console.log('something bad happened...')
			}
		})
	})
	
	
})