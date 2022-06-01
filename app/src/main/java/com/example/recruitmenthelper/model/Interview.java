package com.example.recruitmenthelper.model;

import java.time.LocalDateTime;
import java.util.List;

public class Interview {

   private int interviewId;
   private LocalDateTime dateTime;
   private String location;
   private String candidateName;
   private List<String> interviewers;

   public Interview(int interviewId, LocalDateTime dateTime, String location, String candidateName, List<String> interviewers) {
      this.interviewId = interviewId;
      this.dateTime = dateTime;
      this.location = location;
      this.candidateName = candidateName;
      this.interviewers = interviewers;
   }

   public Interview(){}

   public int getInterviewId() {
      return interviewId;
   }

   public void setInterviewId(int interviewId) {
      this.interviewId = interviewId;
   }

   public LocalDateTime getDateTime() {
      return dateTime;
   }

   public void setDateTime(LocalDateTime dateTime) {
      this.dateTime = dateTime;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public String getCandidateName() {
      return candidateName;
   }

   public void setCandidateName(String candidateName) {
      this.candidateName = candidateName;
   }

   public List<String> getInterviewers() {
      return interviewers;
   }

   public void setInterviewers(List<String> interviewers) {
      this.interviewers = interviewers;
   }

   @Override
   public String toString() {
      return "Interview{" +
              "interviewId=" + interviewId +
              ", dateTime=" + dateTime +
              ", location='" + location + '\'' +
              ", candidate=" + candidateName +
              ", interviewers=" + interviewers +
              '}';
   }
}
