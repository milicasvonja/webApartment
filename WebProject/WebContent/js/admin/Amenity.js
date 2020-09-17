$(document).ready(() => {
  getAmenities();
  $("#new-amenity-form").submit(addAmenity);
  $("#edit-amenity-form").submit(editAmenitySubmit);
});

const removeAmenity = (id) => {
  $.ajax({
    type: "DELETE",
    url: `rest/amenity/${id}`,
    success: (amenity) => {
      getAmenities();
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
};

const addAmenity = (event) => {
  event.preventDefault();
  let amenity = $("#amenity").val();

  if (amenity == "") {
    alert("Fill all fields!");
    return;
  }
  //sending ajax request
  $.ajax({
    type: "POST",
    url: "rest/amenity",
    data: JSON.stringify({
      name: amenity,
    }),
    contentType: "application/json",
    success: (amenity) => {
      getAmenities();
      $("#amenity").val("");
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
};

const getAmenities = () => {
  $("#amenities").empty();
  $("#amenities").append("<h1> Amenities </h1>");

  $.ajax({
    type: "GET",
    url: "rest/amenity",
    success: (amenities) => {
      amenities.forEach((amenity) => {
        $("#amenities").append(
          ` <h3 style="display: inline; text-align: center"> <span onClick="editAmenity('${amenity.id}', '${amenity.name}')" class="badge badge-pill badge-light">${amenity.name} <span onClick="removeAmenity('${amenity.id}')" aria-hidden="true">&times;</span> </span>  </h3> `
        );
      });
    },
    error: function () {
      console.log("something bad happened...");
    },
  });
};

const editAmenity = (id, name) => {
  $("#amenity-id").val(id);
  $("#edit-amenity").val(name);
};

const editAmenitySubmit = (event) => {
  event.preventDefault();
  let amenityId = $("#amenity-id").val();
  let amenityName = $("#edit-amenity").val();

  $.ajax({
    type: "PUT",
    url: `rest/amenity/${amenityId}`,
    data: JSON.stringify({
      name: amenityName,
    }),
    contentType: "application/json",
    success: (amenity) => {
      getAmenities();
      $("#amenity-id").val("");
      $("#edit-amenity").val("");
    },
    error: error => {
      alert("Amenity not found")
    },
  });
};
