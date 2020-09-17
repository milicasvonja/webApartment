$(document).ready(() => {
  getApartments();
});

const getApartments = () => {
  $("#apartments").empty();
  $.ajax({
    type: "GET",
    url: "rest/apartment",
    success: (apartments) => {
      apartments.forEach((apartment) => {
        $("#apartments").append(
          `<tr>
			   <td> ${apartment.id} </td>
				<td> ${apartment.apartmentType} </td>
				<td> ${apartment.rooms} </td>
				<td> ${apartment.guests} </td>
				<td> <button onClick="getComments('${
          apartment.id
        }')" type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
						Comments
					 </button> </td>
				<td> ${apartment.checkIn} </td>
				<td> ${apartment.checkOut} </td>
			   <td> ${apartment.active == true ? "ACTIVE" : "INACTIVE"} </td>
			   <td> <button onClick="changeStatus('${
           apartment.id
         }')" type="button" class="btn btn-info">Remove</button> </td>
		   </tr>`
        );
      });
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
};

const changeStatus = (id) => {
  $.ajax({
    type: "PUT",
    url: `rest/apartment/remove/${id}`,
    success: (users) => {
      getApartments();
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
};

const getComments = (id) => {
  $("#modal-body").empty();

  $.ajax({
    type: "GET",
    url: `rest/comment/apartment/${id}/admin-host`,
    success: (comments) => {
      comments.forEach((comment) => {
        $("#modal-body").append(`<div> ${comment.text} </div>`);
      });
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
};
