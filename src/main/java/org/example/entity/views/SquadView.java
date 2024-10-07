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
public class SquadView {
    Long squadId;
    Long employeeId;
    Long buildingId;
    Long shiftId;
    String squadName;
    String counselor;
    String buildingName;
    String shiftName;
}
