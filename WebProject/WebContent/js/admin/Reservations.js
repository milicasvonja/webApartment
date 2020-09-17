/**
 * 
 */
$(document).ready(function(){
	//Get all reservations
	$.ajax({
		type: "GET",
		url: "rest/reservation",
		contentType: "application/json",
		success: function(reservations){
			for(let res of reservations){
				addReservationToTable(res)
			}
		},
		error: function(error){
			console.log('error getting reservations')
		}
	})
	
	$('form#search').submit(function(event){
		event.preventDefault()
		$('#reservations tbody').empty()
		let username = $('#username').val()
		let status = $('#status').val()
		console.log(username + status)
		$.ajax({
			type:"GET",
			url: "rest/reservation/search?username="+username+"&status="+status,
			contentType:"application/json",
			success: function(reservations){
				for(let res of reservations){
					addReservationToTable(res)
				}
			},
			error: function(error){
				console.log(error)
				alert('error searching reservations')
			}
		})
	})
})

function addReservationToTable(reservation){
	//Get apartment by id for all informations
	$.ajax({
		type:"GET",
		url: "rest/apartment/" + reservation.apartmentId,
		contentType:"application/json",
		success: function(apart){
			addToTable(apart,reservation)
		},
		error: function(error){
			console.log(error)
		}
	})	
}

function addToTable(apartment, reservation){
	let users = {}
	getHostAndGuest(reservation, apartment)
	let guest = localStorage.getItem("guest")
	let host = localStorage.getItem("host")
//	localStorage.removeItem("guest")
//	localStorage.removeItem("host")
	console.log(guest + " " + host)
	
	let tr = $('<tr></tr>')
	let tdLocation = $('<td>' + apartment.location.address.street + " " + apartment.location.address.city + " " + apartment.location.address.number +'</td>')
	let tdType = $('<td>' + apartment.apartmentType + '</td>')
	var d = new Date(reservation.startDate);
	let tdStartDate = $('<td>' + d.toString() + '</td>')
	let tdNights = $('<td>' + reservation.nights + '</td>')
	let tdStatus = $('<td>' + reservation.status + '</td>')
	if(reservation.status == "REJECTED"){
		tdStatus.css("background-color","red")
	}
	if(reservation.status == "ACCEPTED"){
		tdStatus.css("background-color","green")
	}
	
	
	let tdGuest = $('<td>' + guest + '</td>')
	let tdHost = $('<td>' + host + '</td>')
	
	
	tr.append(tdLocation).append(tdType).append(tdStartDate).append(tdNights).append(tdStatus).append(tdGuest).append(tdHost)
	$('#reservations tbody').append(tr)
}

function getHostAndGuest(reservation, apartment){
	//get host
	$.ajax({
		type:"GET",
		url: "rest/user/" + reservation.guestId,
		contentType:"application/json",
		success: function(user){
			localStorage.setItem("guest", user.username)
		},
		error:function(){
			alert('error getting guest')
		}
	})
	
	$.ajax({
		type:"GET",
		url: "rest/user/" + apartment.hostId,
		contentType:"application/json",
		success: function(user){
			localStorage.setItem("host", user.username)
		},
		error:function(){
			alert('error getting host')
		}
	})
}