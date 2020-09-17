/**
 * 
 */
$(document).ready(function(){
	//Check locale storage 
	var user = JSON.parse(localStorage.getItem('user'));
	console.log(user)
	
	$('#loggedIn').text(user.role+" : " +user.username)
	if(user.role!=0){
		$('#users').hide()
	}
	
	$('#check').text(user.username)
	
	if(user.role === "HOST"){
		$('#mainTable').hide()
		$('#hostActiveTable').show()
		$('#hostInactiveTable').show()
	}else{
		$('#mainTable').show()	
		$('#hostActiveTable').hide()
		$('#hostInactiveTable').hide()
	}
	
	//update profile
	

	$('form#search').submit(function(event){
		event.preventDefault()
		$('#hostInactiveAp tbody').empty()
		$('#result tbody').empty()
		$('#hostActiveTable tbody').empty()
		
		let location = $('#location').val()
		let sorting = $('#sort').val()
		let url = ""
		if(user.role == "HOST"){
			url = "rest/apartment/search/host?value="+location+"&sort="+sorting
			console.log(url)
			SearchAsHost(url, user)
		}else{
			url = "rest/apartment/search?value="+location+"&sort="+sorting
			console.log(url)
			SearchAsGuest(url)
		}
	})
	
	getAmenities()

	
	$('form#advancedSearch').submit(function(event){
		event.preventDefault()
		let startDate = null
		let endDate = null
		let city = null
		let pricemin = null
		let pricemax = null
		let roomsmin = null
		let roomsmax = null
		let guestmin = null
		let guestmax = null
		let amenities = []
		var $boxes = $('input[name=amenities]:checked');
		$boxes.each(function(){
			amenities.push($(this).val())
		})
		console.log('all amenities: ' + amenities)
		
		if($('#endDate').val() != ""){
			endDate = $('#endDate').val()
		}
		if($('#startDate').val() != ""){
			startDate = $('#startDate').val()
		}
		if($('#city').val() != ""){
			city = $('#city').val()
		}
		if($('#pricemin').val() != ""){
			pricemin = $('#pricemin').val()
		}
		if($('#pricemax').val() != ""){
			pricemax = $('#pricemax').val()
		}
		if($('#roomsmin').val() != ""){
			roomsmin = $('#roomsmin').val()
		}
		if($('#roomsmax').val() != ""){
			roomsmax = $('#roomsmax').val()
		}
		if($('#guestmin').val() != ""){
			guestmin = $('#guestmin').val()
		}
		if($('#guestmax').val() != ""){
			guestmax = $('#guestmax').val()
		}
		
		let sort = $('#sort').val()
		
		console.log(endDate + startDate+city+pricemin+pricemax+roomsmin+roomsmax+guestmin+guestmax+sort)
		$.ajax({
			type:"POST",
			url: "rest/apartment/filter?sort=" + sort,
			contentType: "application/json",
			data: JSON.stringify({
				startDate: Date.parse(startDate),
				endDate: Date.parse(endDate),
				city: city,
				priceMin: parseFloat(pricemin),
				priceMax: parseFloat(pricemax),
				roomsMin: parseInt(roomsmin),
				roomsMax: parseInt(roomsmax),
				guestsMin: parseInt(guestmin),
				guestsMax: parseInt(guestmax),
				amenitiesId: amenities
			}),
			success: function(apartments){
				$('#result tbody').empty()
//				if(apartments && !apartments.length){
//					console.log('prazno')
//					$('#mainTable').hide()
//				}
				for(let ap of apartments){
					addAppartmentToTable(ap)
					$('#mainTable').show()
				}
			},
			error: function(){
				alert("Something bad happened")
			}
		})
	})
	
})

function addAppartmentToTable(aprt){
	
    let tr = $('<tr></tr>')
	let tdImage = $(`<td class="w-25"><img src="http://localhost:8080/WebProject/rest/apartment/${aprt.id}/image" class="img-fluid img-thumbnail" alt="Picture"></td>`)
	let tdType = $('<td>' + aprt.apartmentType +'</td>')
	let tdLoc = $('<td>' + aprt.location.address.street +" " +aprt.location.address.number + " " + aprt.location.address.city  +'</td>')
	let tdRooms = $('<td>' + aprt.rooms + '</td>')
	let tdGuests = $('<td>' + aprt.guests + '</td>')
	let dtprice = $('<td>' + aprt.price + '</td>')
	tr.append(tdImage).append(tdType).append(tdLoc).append(tdRooms).append(tdGuests).append(dtprice)
	$('#result tbody').append(tr)
	
	tr.click(clickClosure(aprt));
	
}

function addAppartmentToHostActiveTable(aprt){
	let tr = $('<tr></tr>')
	let th = $('<th scope="row">#</th>')
	let tdImage = $(`<td class="w-25"><img src="http://localhost:8080/WebProject/rest/apartment/${aprt.id}/image" class="img-fluid img-thumbnail" alt="Picture"></td>`)
	let tdType = $('<td>' + aprt.apartmentType +'</td>')
	let tdLoc = $('<td>' + aprt.location.address.street +" " +aprt.location.address.number + " " + aprt.location.address.city  +'</td>')
	let tdRooms = $('<td>' + aprt.rooms + '</td>')
	let tdGuests = $('<td>' + aprt.guests + '</td>')
	let dtprice = $('<td>' + aprt.price + '</td>')
	let tdRemove = $('<td><a href="#" class="izbrisi" data-id="' + aprt.id +'">Remove</a></td>')
	$(document).on("click", ".izbrisi", function (){
		let id = $(this).attr("data-id")
		//Ajax request for activating apartment
		$.ajax({
			type:"PUT",
			url: "rest/apartment/remove/" + id,
			success:function(){
				alert('Apartment successfully removed')
				window.location.reload()
			},
			error: function(error){
				console.log(error)
			}
		})
	})
	let tdUpdate = $('<td>Update</td>')
	tr.append(th).append(tdImage).append(tdType).append(tdLoc).append(tdRooms).append(tdGuests).append(dtprice).append(tdRemove).append(tdUpdate)
	$('#hostAp tbody').append(tr)
	
	tdUpdate.click(clickClosureHost(aprt));
}

function addAppartmentToHostInactiveTable(aprt){
	let tr = $('<tr></tr>')
	let th = $('<th scope="row">#</th>')
	let tdImage = $(`<td class="w-25"><img src="http://localhost:8080/WebProject/rest/apartment/${aprt.id}/image" class="img-fluid img-thumbnail" alt="Picture"></td>`)
	let tdType = $('<td>' + aprt.apartmentType +'</td>')
	let tdLoc = $('<td>' + aprt.location.address.street +" " +aprt.location.address.number + " " + aprt.location.address.city  +'</td>')
	let tdRooms = $('<td>' + aprt.rooms + '</td>')
	let tdGuests = $('<td>' + aprt.guests + '</td>')
	let dtprice = $('<td>' + aprt.price + '</td>')
	let tdActivate = $('<td><a href="#" class="aktiviraj" data-id="' + aprt.id +'">Activate</a></td>')
	$(document).on("click", ".aktiviraj", function () {
		let id = $(this).attr("data-id")
		//Ajax request for activating apartment
		$.ajax({
			type:"PUT",
			url: "rest/apartment/activate/" + id,
			success:function(){
				alert('Apartment successfully activated')
				window.location.reload()
			},
			error: function(error){
				console.log(error)
			}
		})
	})
	
	let tdRemove = $('<td><a href="#" class="izbrisi" data-id="' + aprt.id +'">Remove</a></td>')
	
	$(document).on("click", ".izbrisi", function (){
		let id = $(this).attr("data-id")
		//Ajax request for activating apartment
		$.ajax({
			type:"PUT",
			url: "rest/apartment/remove/" + id,
			success:function(){
				alert('Apartment successfully removed')
			},
			error: function(error){
				console.log(error)
			}
		})
	})
	
	tr.append(th).append(tdImage).append(tdType).append(tdLoc).append(tdRooms).append(tdGuests).append(dtprice).append(tdActivate).append(tdRemove)
	$('#hostInactiveAp tbody').append(tr)
	
	//tr.click(clickClosure(aprt));
}

function clickClosure(aprt){
	return function() {
		// Parametar product prosleđen u gornju funkciju će biti vidljiv u ovoj
		// Ovo znači da je funkcija "zapamtila" za koji je apartman vezana
		$('tr.selected').removeClass('selected');
		$(this).addClass('selected');
		console.log(aprt)
		localStorage.wantedApartment = JSON.stringify(aprt)
		window.location.href= "ViewOneApartment.html"
	};
}

function clickClosureHost(aprt){
	return function() {
		// Parametar product prosleđen u gornju funkciju će biti vidljiv u ovoj
		// Ovo znači da je funkcija "zapamtila" za koji je apartman vezana
		$('tr.selected').removeClass('selected');
		$(this).addClass('selected');
		console.log(aprt)
		localStorage.wantedApartment = JSON.stringify(aprt)
		window.location.href= "UpdateApartment.html"
	};
}

function SearchAsGuest(url){
	$.ajax({
		type:"GET",
		url:url,
		contentType:"application/json",
		success:function(apartments){
			for(let ap of apartments){
				
				addAppartmentToTable(ap)
			}
		},
		error:function(error){
			alert(error)
		}
	})
}

function SearchAsHost(url,user){
	$.ajax({
		type:"GET",
		url:url,
		contentType:"application/json",
		success:function(apartments){
			for(let ap of apartments){
				if(ap.hostId == user.id){
					if(!ap.active){
						addAppartmentToHostInactiveTable(ap)
					}else{
						addAppartmentToHostActiveTable(ap)
					}
				}
			}
		},
		error:function(error){
			alert(error)
		}
	})
}

function getAmenities(){
	$.ajax({
		type:"GET",
		url:"rest/amenity",
		contentType:"application/json",
		success:function(amenities){
			console.log(amenities)
			for(let amenity of amenities){
				addAmenity(amenity)
			}
		},
		error:function(){
			alert('error getting amenities')
		}
	})
}

function addAmenity(amenity){
	$("#checkboxes").append(`
	<div class="form-check">
			<input type="checkbox" class="form-check-input" id="${amenity.id}" name="amenities" value="${amenity.id}" >
			<label class="form-check-label" for="${amenity.id}">${amenity.name}</label>
	</div>
	`);
}
