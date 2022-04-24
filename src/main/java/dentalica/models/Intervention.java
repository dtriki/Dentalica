package dentalica.models;

import java.time.Instant;

public class Intervention {

    private Integer id;
    private Integer patientId;
    private String type;
    private String teeth;
    private String price;
    private Instant intervenedAt;
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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

    public Instant getIntervenedAt() {
        return intervenedAt;
    }

    public void setIntervenedAt(Instant intervenedAt) {
        this.intervenedAt = intervenedAt;
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
                ", patientId=" + patientId +
                ", type='" + type + '\'' +
                ", teeth='" + teeth + '\'' +
                ", price='" + price + '\'' +
                ", intervenedAt=" + intervenedAt +
                ", createdAt=" + createdAt +
                '}';
    }

}
