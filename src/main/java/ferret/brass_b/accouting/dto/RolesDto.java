package ferret.brass_b.accouting.dto;

import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesDto {
    String login;
    Set<String> roles;
}
