$(document).ready(function(){
	//Get all apartments types and amenities
	
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

	$('#newApp-form').submit(function(event){
		event.preventDefault()
		let appType = $('#appartmentType').val()
		let roomsNumber = $('#rooms').val()
		let guests = $('#guests').val()
		let street = $('#street').val()
		let number = $('#number').val()
		let city = $('#city').val()
		let postal = $('#postal').val()
		let latitute = $('#latitude').val()
		let longitute = $('#longitude').val()
		let startDate = $('#startDate').val()
		let endDate = $('#endDate').val()
		let price = $('#price').val()
		if(roomsNumber == "0" || guests == "0" || street== "" || city == "" || startDate=="" || endDate ==""){
			alert('Fill fields...')
			return
		}
		//Convert to int
		let postalInt = 0
		if(postal != ""){
			postalInt = parseInt(postal)
		}


		//Create Adress object
		var address = {}
		address= {
			street: street,
			number: number,
			city:   city,
			postal: postalInt
		}

		var location = {
			longitude: parseFloat(longitute),
			latitude: parseFloat(latitute),
			address: address
		}

		// Check which amenities are selected
		var amenities = []
		var $boxes = $('input[name=amenities]:checked');
		$boxes.each(function(){
			amenities.push($(this).val())
		})
		console.log('all amenities: ' + amenities)
		//Get dates included between start and endDate
		var dateFrom = Date.parse(startDate)
		var dateTo = Date.parse(endDate)
		var validDates = getDates(dateFrom, dateTo)
		console.log(validDates)

		//convert price to double
		let priceDouble = parseFloat(price)
		
		$.ajax({
			type:"POST",
			url: "rest/apartment",
			data: JSON.stringify({
				apartmentType: appType,
				rooms: parseInt(roomsNumber),
				guests: parseInt(guests),
				location: location,
				dates: validDates,
				amenitiesId: amenities,
				price: priceDouble,
			}),
			contentType:"application/json",
			success: function(apartment){
				alert('crated sucessfully..')
				
//				$.ajax({
//					type:"POST",
//					url:"rest/apartment/" + apartment.id +"/image",
//					success:function(){
//					
//					},
//					error: function(){}
//					
//				})
				//////////////
				var file = document.getElementById("picture").files[0];
	            var extension = file.name.split(".").pop();
	            var type;
	            if (extension === "jpg" || extension === "jpeg" ||
	                extension === "JPG" || extension === "JPEG") {
	                type = "image/jpeg";
	            } else if (extension === "png" || extension === "PNG") {
	                type = "image/png";
	            } else {
	                alert("Invalid file type");
	                return;
	            }
	            var request = new XMLHttpRequest();
	            request.open("POST", "rest/apartment/" + apartment.id +"/image");
	            request.setRequestHeader("Content-Type", type);
	            request.setRequestHeader("Image-Name", name);
	            request.send(file);
				//////////////////
				window.location.href="homepage.html"
			},
			error: function(error){
				console.log(error)
				alert('Some error occurred...')
			}
		})
	})
	
	
	map.on('click', function(evt){
	    console.info(evt.pixel);
	    console.info(map.getPixelFromCoordinate(evt.coordinate));
	    console.info(ol.proj.toLonLat(evt.coordinate));
	    var coords = ol.proj.toLonLat(evt.coordinate);
	    var lat = coords[1];
	    var lon = coords[0];
	    var locTxt = "Latitude: " + lat + " Longitude: " + lon;
	    // coords is a div in HTML below the map to display
	    $('#latitude').val(lat) 
	    $('#longitude').val(lon) 
	});

})

function addAmenity(amenity){
	$("#checkboxes").append(`
	<div class="form-check">
			<input type="checkbox" class="form-check-input" id="${amenity.id}" name="amenities" value="${amenity.id}" >
			<label class="form-check-label" for="${amenity.id}">${amenity.name}</label>
	</div>
	`);
}



function getDates(startDate, stopDate) {
    var dateArray = new Array();
    var currentDate = startDate;
    while (currentDate <= stopDate) {
        dateArray.push(new Date (currentDate));
		let date = new Date(currentDate);
		date.setDate(date.getDate() + 1);
        currentDate = date;
    }
    return dateArray;
}

const test = () => {
	let x = $("input[name=amenities]:checked");
	x.each(function(){
		console.log($(this).val())
	})
}