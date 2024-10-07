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
public class ParticipateView {
    Long participateId;
    Long eventId;
    String eventName;
    Date eventDate;
    String squadName;
    Long squadId;
}
