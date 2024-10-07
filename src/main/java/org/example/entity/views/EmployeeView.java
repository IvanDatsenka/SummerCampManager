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
public class EmployeeView {
    Long employeeId;
    Long jobTitleId;
    String employeeFullName;
    String jobTitleName;
}
