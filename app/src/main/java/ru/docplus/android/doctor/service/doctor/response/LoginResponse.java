package ru.docplus.android.doctor.service.doctor.response;

import java.util.List;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class LoginResponse {

    private boolean success;
    private Data data;

    public boolean getSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    public class Data {

        public String token;
        public Doctor doctor;
        public Address address;

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public Doctor getDoctor() {
            return doctor;
        }
        public void setDoctor(Doctor doctor) {
            this.doctor = doctor;
        }
        public Address getAddress() {
            return address;
        }
        public void setAddress(Address address) {
            this.address = address;
        }

    }

    public class Doctor {

        private int doctorId;
        private String name;
        private String middleName;
        private String surname;
        private float rating;
        private String description;
        private List<Specialization> specializations;
        private List<Image> images;
        private List<Image> images_square;
        private Address address;
        private int typeOfEmployment;
        public boolean docTimeworkId;

        public int getDoctorId() {
            return doctorId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public float getRating() {
            return rating;
        }

        public String getDescription() {
            return description;
        }

        public List<Specialization> getSpecializations() {
            return specializations;
        }

        public Address getAddress() { return this.address; }
        public void setAddress(Address address) { this.address = address; }
        public List<Image> getImages() {
            return images;
        }
        public List<Image> getImages_square() {
            return images_square;
        }
        public String getMiddleName() {
            return middleName;
        }
        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public boolean isNurse() {
            int i = 0;
            boolean isNurse = false;

            if (getSpecializations() != null) {
                while (!isNurse && i < getSpecializations().size()) {
                    isNurse = (getSpecializations().get(i).getId() == 2138);
                    i++;
                }
            }

            return isNurse;
        }

    }

    public class Specialization {

        public int id;
        public String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Address {

        transient public int id;
        transient public String address;

        public String city;
        public String street;
        public String house;
        public String structure;
        public String building;
        public String porch;
        public String intercom;
        public String floor;
        public String flat;
        public double latitude;
        public double longitude;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getAddress() { return address; }
        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getStreet() { return street; }
        public void setStreet(String street) {
            this.street = street;
        }

        public String getHouse() { return house; }
        public void setHouse(String house) {
            this.house = house;
        }

        public String getStructure() { return structure; }
        public void setStructure(String structure) {
            this.structure = structure;
        }

        public String getBuilding() { return building; }
        public void setBuilding(String building) {
            this.building = building;
        }

        public String getPorch() { return porch; }
        public void setPorch(String porch) {
            this.porch = porch;
        }

        public String getIntercom() { return intercom; }
        public void setIntercom(String intercom) {
            this.intercom = intercom;
        }

        public String getFloor() { return floor; }
        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getFlat() { return flat; }
        public void setFlat(String flat) {
            this.flat = flat;
        }

        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

    }

    public class Image {

        public String size;
        public String path;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
