package org.example.entity.views;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomView {
    Long roomId;
    Long buildingId;
    String roomNumber;
    String buildingNumber;
}
