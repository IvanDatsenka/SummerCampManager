package org.example.entity;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Child {
    private Long id;
    private String firstName;
    private String secondName;
    private Long squadId;
    private Long sportSectionId;
    private Long roomId;
}
