package com.example.recruitmenthelper.model;

public class Feedback {

   private String feedback;
   private String status;
   private String userId;
   private String username;

   public Feedback() {
   }

   public Feedback(String feedback, String status, String userId, String username) {
      this.feedback = feedback;
      this.status = status;
      this.userId = userId;
      this.username = username;
   }

   public String getFeedback() {
      return feedback;
   }

   public void setFeedback(String feedback) {
      this.feedback = feedback;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }
}
