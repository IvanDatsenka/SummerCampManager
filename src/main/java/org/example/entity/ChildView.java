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
public class ChildView {
    Long childId;
    String firstName;
    String secondName;
    String squadName;
    String sportSectionName;
    String roomNumber;
}
