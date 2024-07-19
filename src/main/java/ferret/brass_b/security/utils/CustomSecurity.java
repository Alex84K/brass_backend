package ferret.brass_b.security.utils;

import ferret.brass_b.accouting.dao.UserRepository;
import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.materials.dao.MaterialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CustomSecurity {

    private final UserRepository userRepository;

    public boolean hasUserAccessToUserId(String userName, String userId) {
        UserAccount userAccount = userRepository.findByUsernameIgnoreCase(userName).orElse(null);
        return userAccount != null && userAccount.getId().equals(userId);
    }

    public boolean isUserAdmin(Supplier<Authentication> authentication) {
        return authentication.get().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
    }
}
