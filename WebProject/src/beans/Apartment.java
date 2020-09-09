package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Apartment {

	private String id;
	private ApartmentType apartmentType;
	private int rooms;
	private int guests;
	private Location location;
	private ArrayList<Date> dates;
	private String hostId;
	private ArrayList<String> commentsId;
	private ArrayList<String> images;
	private double price;
	private Date checkIn;
	private Date checkOut;
	private ArrayList<String> amenitiesId;
	private ArrayList<String> reservationsId;
	private boolean isActive;
	
	public Apartment() {
		this.id = UUID.randomUUID().toString();
	}
	
	public Apartment(ApartmentType apartmentType, int rooms, int guests, Location location, ArrayList<Date> dates,
			String hostId, ArrayList<String> commentsId, ArrayList<String> images, double price, Date checkIn,
			Date checkOut, ArrayList<String> amenitiesId, ArrayList<String> reservationsId, boolean isActive) {
		super();
		this.id = UUID.randomUUID().toString();
		this.apartmentType = apartmentType;
		this.rooms = rooms;
		this.guests = guests;
		this.location = location;
		this.dates = dates;
		this.hostId = hostId;
		this.commentsId = commentsId;
		this.images = images;
		this.price = price;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.amenitiesId = amenitiesId;
		this.reservationsId = reservationsId;
		this.isActive = isActive;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ApartmentType getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(ApartmentType apartmentType) {
		this.apartmentType = apartmentType;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getGuests() {
		return guests;
	}

	public void setGuests(int guests) {
		this.guests = guests;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<Date> getDates() {
		return dates;
	}

	public void setDates(ArrayList<Date> dates) {
		this.dates = dates;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public ArrayList<String> getCommentsId() {
		return commentsId;
	}

	public void setCommentsId(ArrayList<String> commentsId) {
		this.commentsId = commentsId;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setChekOut(Date chekOut) {
		this.checkOut = chekOut;
	}

	public ArrayList<String> getAmenitiesId() {
		return amenitiesId;
	}

	public void setAmenitiesId(ArrayList<String> amenitiesId) {
		this.amenitiesId = amenitiesId;
	}

	public ArrayList<String> getReservationsId() {
		return reservationsId;
	}

	public void setReservationsId(ArrayList<String> reservationsId) {
		this.reservationsId = reservationsId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Apartment [id=" + id + ", apartmentType=" + apartmentType + ", rooms=" + rooms + ", guests=" + guests
				+ ", location=" + location + ", dates=" + dates + ", hostId=" + hostId + ", commentsId=" + commentsId
				+ ", images=" + images + ", price=" + price + ", checkIn=" + checkIn + ", chekOut=" + checkOut
				+ ", amenitiesId=" + amenitiesId + ", reservationsId=" + reservationsId + ", isActive=" + isActive
				+ "]";
	}
	
	
	
}
