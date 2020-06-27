package mona.project.guideme.data;

public class UserModel
{
    String id,name,email, phone,gender,imageUrl;

    public UserModel() {
    }

    public UserModel(String id, String name, String email ,String mobile, String gender, String imageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = mobile;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
