/**
 * 
 */
$(document).ready(function(){
	let apartment = JSON.parse(localStorage.wantedApartment)
	var user = JSON.parse(localStorage.getItem('user'));
	console.log(apartment)
	
	let startDate ='';
	let endDate = '';
	//Get all amenities for current apartment
	apartment.amenitiesId.forEach(function (amenityId, index) {
		$.ajax({
			type: "GET",
			url: "rest/amenity/" + amenityId,
			contentType: "application/json",
			success: function(amenity){
				addAmenityToList(amenity)
			},
			error: function(error){
				console.log(error)
			}
		})
	});
	
	//GET COMMENTS for APArtment
	if(apartment.commentsId.length != 0){
		$.ajax({
			type: "GET",
			url: "rest/comment/apartment/" + apartment.id +"/guest",
			contentType: "application/json",
			success: function(comments){
				for(let comm of comments){
					addCommentToTable(comm)
				}
			},
			error: function(error){
				console.log(error)
			}
		})
	}
	
	
	addAppartmentToTable(apartment)
	$('#type').append(`<p>${apartment.apartmentType}</p>`)
	
	//Parsing dates and setting to date picker
	let firstDate = apartment.dates[0]
	let lastDate = apartment.dates[apartment.dates.length-1]
	var date1 = new Date(firstDate); //convert miliseconds to date
	var date2 = new Date(lastDate);
	let date3 = moment(date1, date1.toString()).format("YY/MM/DD")
	let date4 = moment(date2, date2.toString()).format("YY/MM/DD")
	console.log(date3)
	console.log(date4)

	$('#fromDate').text("Start date: " + date3)
	$('#toDate').text("End date: "  + date4)
	console.log(date1.toString() + " " + date2.toString(0))
	$('#gallery').append(`<img src="http://localhost:8080/WebProject/rest/apartment/${apartment.id}/image" width="800" height="500" class="img-fluid img-thumbnail" alt="Picture"></td>`)
	

	$(function() {
		  $('input[name="daterange"]').daterangepicker({
		    opens: 'left',
		    startDate: date3,
		    endDate: date4,
		    locale: {
		      format: 'YYYY/MM/DD'
		    }
		  }, function(start, end, label) {
		    if(start < date1 || end > date2){
		    	alert('pick a propriete date!')
		    	return
		    }
		    startDate = start.format('YYYY-MM-DD')
		    endDate = end.format('YYYY-MM-DD')
		    console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
		  });
	});
	
	$('form#reservation').submit(function(event){
		event.preventDefault()
		let apartmentId = apartment.id
		console.log(startDate)
		//console.log(endDate)
		var date = new Date(startDate)
		let nights = $('#nightsNumber').val()
		let nightsInt = parseInt(nights)
		let price = apartment.price
		let message = $('#message').val()
		let guestId = user.id
		if(startDate == '' || endDate == ''){
			alert('Pick date!')
			return
		}
		//Sending ajax request to create reservation
		$.ajax({
			type:"POST",
			url:"rest/reservation",
			contentType: "application/json",
			data: JSON.stringify({
				apartmentId: apartmentId,
				startDate: date,
				nights: nights,
				price: price,
				message: message,
				guestId: user.id
			}),
			success: function(reservation){
				alert('Successfully created reservation with ID : ' + reservation.id)
				window.location.href="homepage.html"
			},
			error: function(error){
				console.log(error)
			}
			
		})
	})
		
})



function addAppartmentToTable(aprt){
	let tr = $('<tr></tr>')
	//let tdImage = $('<td class="w-25"><img src="${aprt.images[0]}" class="img-fluid img-thumbnail" alt="Sheep"></td>')
	let tdType = $('<td>' + aprt.apartmentType +'</td>')
	let tdRooms = $('<td>' + aprt.rooms + '</td>')
	let tdGuests = $('<td>' + aprt.guests + '</td>')
	let tdLoc = $('<td>' + aprt.location.address.street +" " +aprt.location.address.number + " " + aprt.location.address.city  +'</td>')
	let tdCheckIn = $('<td>' + aprt.CheckIn + '</td>')
	let tdCheckOut = $('<td>' + aprt.CheckOut + '</td>')
	let dtprice = $('<td>' + aprt.price + '</td>')
	tr.append(tdType).append(tdRooms).append(tdGuests).append(tdLoc).append(dtprice)
	$('#apartment').append(tr)
//	tr.click(clickClosure(aprt));
}

function addAmenityToList(amenity){
	console.log(amenity)
	let li = $('<li class="list-group-item>' + amenity.name + '</li>')
	$('#amenities').append(`<li class="list-group-item">${amenity.name}</li>`)
}

function addCommentToTable(comment){
	//get reviewer
	let reviewer = ''
	let tdReviewer = $('<td></td>')

	$.ajax({
		type:"GET",
		url:'rest/user/' + comment.reviewerId,
		contentType: "application/json",
		success:function(user){
			console.log(user.username)
			tdReviewer.append(user.username)
		},
		error: function(error){
			console.log(error)
		}
	})
	//Add fields to table
	let tr = $('<tr></tr>')
	let tdComment = $('<td>' + comment.text + '</td>')
	let tdRate = $('<td>' + comment.rate + '</td>')
	tr.append(tdReviewer).append(tdComment).append(tdRate)
	$('#comments tbody').append(tr)
	
}

