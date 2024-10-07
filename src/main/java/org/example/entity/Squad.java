package org.example.entity;

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
public class Squad {
    Long id;
    Long employeeId;
    Long shiftId;
    String name;
    Long buildingId;
}
