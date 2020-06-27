package mona.project.guideme.data;

public class ReservationModel {
    String tripID, userID;
    int totalSeats, reservedSeats, availableSeats;

    public ReservationModel() {
    }

    public ReservationModel(String tripID, String userID, int totalSeats, int reservedSeats, int availableSeats) {
        this.tripID = tripID;
        this.userID = userID;
        this.totalSeats = totalSeats;
        this.reservedSeats = reservedSeats;
        this.availableSeats = availableSeats;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
