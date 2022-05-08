package dentalica.models;

import java.time.Instant;
import java.time.LocalDate;

public class Intervention {

    private Integer id;
    private Integer patientId;
    private String type;
    private String teeth;
    private Integer price;
    private LocalDate intervenedAt;
    private String payed;
    private Instant createdAt;

    public Intervention(Integer id, String type, String teeth, Integer price, LocalDate intervenedAt, String payed) {
        this.id = id;
        this.type = type;
        this.teeth = teeth;
        this.price = price;
        this.intervenedAt = intervenedAt;
        this.payed = payed;
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getIntervenedAt() {
        return intervenedAt;
    }

    public void setIntervenedAt(LocalDate intervenedAt) {
        this.intervenedAt = intervenedAt;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
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
                ", payed=" + payed +
                ", createdAt=" + createdAt +
                '}';
    }

}
