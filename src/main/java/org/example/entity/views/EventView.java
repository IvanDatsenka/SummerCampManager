package org.example.entity.views;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventView {
    Long eventId;
    Long employeeId;
    String eventName;
    Date eventDate;
    String employee;
}
