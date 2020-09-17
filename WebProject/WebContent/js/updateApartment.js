/**
 * 
 */
$(document).ready(function(){
	let apartment = JSON.parse(localStorage.wantedApartment)
	var user = JSON.parse(localStorage.getItem('user'));
	console.log(apartment)
	//popunjavanje polja
	let selectApartment = $('<option value="APARTMENT">APARTMENT</option>')
	let selectRoom = $('<option value="ROOM">ROOM</option>')
	if(apartment.apartmentType == "APARTMENT"){
		selectApartment.attr("selected", true)
	}else{
		selectRoom.attr("selected", true)
	}
	$('#appartmentType').append(selectApartment).append(selectRoom)
	
	let inputRooms = $('<input type="number" id="rooms" class="form-control" value="' + apartment.rooms + '"/>')
	$('#roomNumbersDiv').append(inputRooms)
	let inputGuests = $('<input type="number" id="guests" class="form-control" value="' + apartment.guests + '"/>')
	$('#questNumberDiv').append(inputGuests)
	
//	let inputStreet = $('<input type="text" id="street" class="form-control" value="' + apartment.location.address.street + '" required/>')
//	$('#locationDiv').append(inputStreet)
//	let inputNumber = $('<input type="text" id="number" class="form-control" value="' + apartment.location.address.number + '" required/>')
//	$('#numberDiv').append(inputNumber)
//	let inputCity = $('<input type="text" id="city" class="form-control" value="' + apartment.location.address.city + '" required/>')
//	$('#cityDiv').append(inputCity)
//	let inputPostal = $('<input type="number" id="postal" class="form-control" value="' + apartment.location.address.postal + '" required/>')
//	$('#postalDiv').append(inputPostal)
//	let inputLatitute = $('<input type="number" id="latitude" class="form-control" value="' + apartment.location.latitude + '" required/>')
//	$('#coordinatesDiv').append(inputLatitute)
//	let inputLongitute = $('<input type="number" id="longitude" class="form-control" value="' + apartment.location.longitude + '" required/>')
//	$('#coordinates2Div').append(inputLongitute)
//	//Parse first element of dates array to string
//	var time = apartment.dates[0]
//	var date = new Date(time)
//	console.log(date.toString())
//	var time2 = apartment.dates[apartment.dates.length - 1]
//	var date2 = new Date(time2)
//	console.log(date.toString())
//	console.log(date2.toString())
//	let labelDate = $('<label>Current start date: <b>' + date.toString() + ' </b>, pick a new one</label>')
//	let inputDate = $('<input type="date" id="startDate" name="startDate" />')
//	$('#startDateDiv').append(labelDate).append(inputDate)
//	
//	let labelDate2 = $('<label>Current end date: <b>' + date2.toString() + ' </b>, pick a new one</label>')
//	let inputDate2 = $('<input type="date" id="endDate" name="endDate" />')
//	$('#endDateDiv').append(labelDate2).append(inputDate2)
//	
	let inputPrice = $('<input type="number" class="form-control" name="price" id="price" value="' + apartment.price + '" required/>')
	$('#priceDiv').append(inputPrice)
	
	
	//GET ALL AMENITITES AND CHECK THOSE CONTAINED IN APARTMENT
	let amenities = [] 
	$.ajax({
		type:"GET",
		url:"rest/amenity",
		contentType:"application/json",
		success:function(amenitiess){
			console.log(amenitiess)
			amenities = amenitiess
			for(let amenity of amenitiess){
				addAmenity(amenity, apartment)
			}
		},
		error:function(){
			alert('error getting amenities')
		}
	})
	
	//Submiting form
	$('form#update').submit(function(event){
		event.preventDefault()
		let apType = $('#appartmentType').val()
		let roomsNumber = $('#rooms').val()
		let guests = $('#guests').val()
		let price = $('#price').val()
		if(roomsNumber == "0" || guests == "0" || price == "0"){
			alert('Fill fields...')
			return
		}
		
		var amenities = []
		var $boxes = $('input[name=amenities]:checked');
		$boxes.each(function(){
			amenities.push($(this).val())
		})
		console.log('all amenities: ' + amenities)
		
		
		let priceDouble = parseFloat(price)
		$.ajax({
			type: "PUT",
			url: "rest/apartment/edit/" + apartment.id,
			contentType: "application/json",
			data: JSON.stringify({
				apartmentType: apType,
				rooms: parseInt(roomsNumber),
				guests: parseInt(guests),
				price: priceDouble,
				amenitiesId: amenities
			}),
			success: function(){
				alert('Successfully updated apartment')
				window.location.href="homepage.html"
			},
			error: function(){
				alert('Something bad happened...')
			}
		})
	})
	
	
	
})


function addAmenity(amenity, apartment){
	let div = $('<div class="form-check"></div>')
	let input = $('<input type="checkbox" class="form-check-input" id="' + amenity.id + '" name="amenities" value="' + amenity.id + '" >')
	let label =  $('<label class="form-check-label" for="' + amenity.id + '">' + amenity.name + '</label>')
	for(let id of apartment.amenitiesId){
		if(id == amenity.id){
			input.attr('checked', 'checked');
		}
	}
	div.append(input).append(label)
	$("#checkboxes").append(div)
}