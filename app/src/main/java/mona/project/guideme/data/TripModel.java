package mona.project.guideme.data;

public class TripModel {
    String id, driverName,  driverPhone,  busId,  noSeats, availableSeats,noPassengers,day,time,  destination,  assemblyPoint,  destinationPoint, imageurl;

    public TripModel() {
    }

    public TripModel(String id, String driverName, String driverPhone, String busId, String noSeats, String availableSeats, String noPassengers, String day, String time, String destination, String assemblyPoint, String destinationPoint, String imageurl) {
        this.id = id;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.busId = busId;
        this.noSeats = noSeats;
        this.availableSeats = availableSeats;
        this.noPassengers = noPassengers;
        this.day = day;
        this.time = time;
        this.destination = destination;
        this.assemblyPoint = assemblyPoint;
        this.destinationPoint = destinationPoint;
        this.imageurl = imageurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(String noSeats) {
        this.noSeats = noSeats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAssemblyPoint() {
        return assemblyPoint;
    }

    public void setAssemblyPoint(String assemblyPoint) {
        this.assemblyPoint = assemblyPoint;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getNoPassengers() {
        return noPassengers;
    }

    public void setNoPassengers(String noPassengers) {
        this.noPassengers = noPassengers;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}