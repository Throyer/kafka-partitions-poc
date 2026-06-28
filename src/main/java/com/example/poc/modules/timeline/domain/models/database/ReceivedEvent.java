package com.example.poc.modules.timeline.domain.models.database;

import java.time.LocalDateTime;

import com.example.poc.modules.aftersale.domain.models.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "received_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedEvent {
  @Id
  private String id;

  @Indexed
  private String orderNumber;

  private Event event;

  private LocalDateTime createdAt;

  public ReceivedEvent(Event event) {
    this.orderNumber = event.getOrderNumber();
    this.event = event;
    this.createdAt = LocalDateTime.now();
  }
}
