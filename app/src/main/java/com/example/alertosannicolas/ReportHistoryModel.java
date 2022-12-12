package com.example.alertosannicolas;

public class ReportHistoryModel {
    String emergencyType;
    String userId;
    String proofUrl;
    String neededHelp;
    String status;
    Long date;


    public ReportHistoryModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public ReportHistoryModel(String emergencyType, String userId, String proofUrl, String neededHelp, String status, Long date) {
        this.emergencyType = emergencyType;
        this.userId = userId;
        this.proofUrl = proofUrl;
        this.neededHelp = neededHelp;
        this.status = status;
        this.date = date;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    public String getNeededHelp() {
        return neededHelp;
    }

    public void setNeededHelp(String neededHelp) {
        this.neededHelp = neededHelp;
    }
}
