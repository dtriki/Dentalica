package dentalica.models;

public class Fee {

    private Integer patientId;
    private String patientName;
    private String patientSurname;
    private String patientNumber;
    private Integer debt;

    public Fee(Integer patientId, String patientName, String patientSurname, String patientNumber, Integer debt) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.patientNumber = patientNumber;
        this.debt = debt;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public Integer getDebt() {
        return debt;
    }

    public void setDebt(Integer debt) {
        this.debt = debt;
    }

    @Override
    public String toString() {
        return "Fee{" +
                "patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", patientSurname='" + patientSurname + '\'' +
                ", patientNumber='" + patientNumber + '\'' +
                ", debt=" + debt +
                '}';
    }

}
