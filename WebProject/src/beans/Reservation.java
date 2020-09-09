package beans;

import java.util.Date;
import java.util.UUID;

public class Reservation {
	
	private String id;
	private String apartmentId;
	private Date startDate;
	private int nights;
	private double price;
	private String message;
	private String guestId;
	private ReservationStatus status;
	
	public Reservation() {
		this.id = UUID.randomUUID().toString();
	}
	
	public Reservation(String apartmentId, Date startDate, int nights, double price, String message, String guestId,
			ReservationStatus status) {
		super();
		this.id = UUID.randomUUID().toString();
		this.apartmentId = apartmentId;
		this.startDate = startDate;
		this.nights = nights;
		this.price = price;
		this.message = message;
		this.guestId = guestId;
		this.status = status;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getNights() {
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", apartmentId=" + apartmentId + ", startDate=" + startDate + ", nights="
				+ nights + ", price=" + price + ", message=" + message + ", guestId=" + guestId + ", status=" + status
				+ "]";
	}
	
	
	
}
