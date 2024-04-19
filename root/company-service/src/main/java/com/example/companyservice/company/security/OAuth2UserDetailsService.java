package com.example.companyservice.company.security;

import com.example.companyservice.company.common.exception.ApiException;
import com.example.companyservice.company.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.entity.Role;
import com.example.companyservice.company.entity.CreateStatusEnum;
import com.example.companyservice.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("------------------------------");
        log.info("userRequest : {}", userRequest);

        String provider = userRequest.getClientRegistration().getClientName();

        log.info("provider : {}", provider);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==============================");
        oAuth2User.getAttributes().forEach((k,v) -> log.info("{} : {}", k, v));

        String providerId;
        String email;
        if (provider.equals("Kakao")) {
            providerId = String.valueOf(oAuth2User.getAttributes().get("id"));
            HashMap<String, String> map = oAuth2User.getAttribute("kakao_account");
            email = map.get("email");
        } else if (provider.equals("Naver")) {
            HashMap<String, String> map = oAuth2User.getAttribute("response");
            providerId = map.get("id");
            email = map.get("email");
        } else if (provider.equals("Google")) {
            providerId = oAuth2User.getAttribute("sub");
            email = oAuth2User.getAttribute("email");
        } else {
            throw new ApiException(ExceptionEnum.NOT_SUPPORT_PROVIDER_TYPE);
        }

        log.info("providerId : {}", providerId);
        log.info("EMAIL : {}", email);

        Company company = saveSocialMember(email, provider, providerId);

        OAuthUserDetails companyDetails = new OAuthUserDetails(
                company.getEmail(),
                company.getPassword(),
                company.getRoleSet()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(toList()),
                oAuth2User.getAttributes()
        );
        companyDetails.setName(company.getRepresentativeName());

        return companyDetails;

    }

    private Company saveSocialMember(String email, String provider, String providerId) {

        Optional<Company> result = companyRepository.findByEmail(email);

        if (result.isPresent()) return result.get();

        String password = UUID.randomUUID().toString();

        Company company = Company.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .provider(provider)
                .providerId(providerId)
                .isDelete(false)
                .createStatus(CreateStatusEnum.WAIT.getTypeValue())
                .build();

        company.addRole(Role.COMPANY);

        companyRepository.save(company);

        return company;
    }

}