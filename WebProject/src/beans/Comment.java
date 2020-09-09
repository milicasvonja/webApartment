package beans;

import java.util.UUID;

public class Comment {

	private String id;
	private String reviewerId;
	private String apartmentId;
	private String text;
	private int rate;
	private boolean approved;
	
	public Comment() {
		this.id = UUID.randomUUID().toString();
		approved = false;
	}
	
	
	public Comment(String reviewerId, String apartmentId, String text, int rate, boolean approved) {
		super();
		this.id = UUID.randomUUID().toString();
		this.reviewerId = reviewerId;
		this.apartmentId = apartmentId;
		this.text = text;
		this.rate = rate;
		this.approved = approved;
	}

	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getReviewerId() {
		return reviewerId;
	}


	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}


	public String getApartmentId() {
		return apartmentId;
	}


	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getRate() {
		return rate;
	}


	public void setRate(int rate) {
		this.rate = rate;
	}


	public boolean isApproved() {
		return approved;
	}


	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	
	
	
}
