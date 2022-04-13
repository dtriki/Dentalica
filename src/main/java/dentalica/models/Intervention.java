package dentalica.models;

import java.time.Instant;

public class Intervention {

    private Integer id;
    private Integer userId;
    private String type;
    private String teeth;
    private String price;
    private Instant date;
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeeth() {
        return teeth;
    }

    public void setTeeth(String teeth) {
        this.teeth = teeth;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Intervention{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", teeth='" + teeth + '\'' +
                ", price='" + price + '\'' +
                ", date=" + date +
                ", createdAt=" + createdAt +
                '}';
    }

}
