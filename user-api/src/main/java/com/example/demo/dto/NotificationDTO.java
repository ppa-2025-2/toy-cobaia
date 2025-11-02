package com.example.demo.dto;

import java.util.List;

public record NotificationDTO(
    String recipient,
    String title,
    String body,
        // ex.: sms, mail, call, discord,whatsapp
    List<String> media 
) {

    public static class Builder {
        private String recipient;
        private String title;
        private String body;
        private List<String> media;

        public Builder recipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder media(List<String> media) {
            this.media = media;
            return this;
        }

        public NotificationDTO build() {
            return new NotificationDTO(recipient, title, body, media);
        }
    }
}
