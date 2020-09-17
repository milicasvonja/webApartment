$(document).ready(() => {
  let user = JSON.parse(localStorage.getItem("user"));

  let adminLinks = `
    

    <li class="nav-item">
      <a class="nav-link" href="newAmenity.html">Amenities</a>
    </li>
	 
	<li class="nav-item">
      <a class="nav-link" href="users.html">Users</a>
    </li>

	<li class="nav-item">
      <a class="nav-link" href="apartments.html">Apartments</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="AdminReservations.html">Reservations</a>
    </li>
`;
  let hostLinks = `
    <li class="nav-item">
      <a class="nav-link" href="newAppartment.html">New Apartment</a>
    </li>
     <li class="nav-item">
      <a class="nav-link" href="hostReservations.html">Reservations</a>
    </li>
`;
  
 let guestLink = `
	 <li class="nav-item">
	 	<a class="nav-link" href="guestReservations.html">My Reservations</a>
	 </li>
 `

	let links = "";
	if (user.role == "ADMIN")
		links = adminLinks;
	if (user.role == "HOST")
		links = hostLinks;
	if(user.role == "GUEST")
		links = guestLink


  let navbarContent = `<div class="navbar navbar-expand-lg navbar-light bg-green">
    <a class="navbar-brand" href="updateProfile.html">
          <h5>Welcome ${user.username}</h5>
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarResponsive">
      <ul class="navbar-nav ml-auto">
        <li class="nav-item active">
          <a class="nav-link" href="homepage.html">Home
                <span class="sr-only">(current)</span>
	  	  </a>
        </li>
		${links}
        <li class="nav-item">
          <a class="nav-link" href="updateProfile.html">Profile</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#" id="logout">Logout</a>
        </li>
      </ul>
    </div>
  </div>`;

  $("nav").append(navbarContent);

  $("#logout").click(function () {
    //Ajax poziv za logout
    $.ajax({
      type: "POST",
      url: "rest/user/logout",
      success: function () {
        window.location.href = "login.html";
      },
    });
  });
});
