package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.exception.ErrorCode;
import com.example.healthgenie.boundedContext.auth.dto.*;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = getOAuth2Response(registrationId, oAuth2User);

        User user = userRepository.findByEmail(oAuth2Response.getEmail())
                .orElseGet(() -> userService.signUp(oAuth2Response.getEmail(), oAuth2Response.getName(), AuthProvider.findByCode(oAuth2Response.getProvider())));

        user.updateEmail(oAuth2Response.getEmail());
        user.updateName(oAuth2Response.getName());

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole().getCode());

        return new CustomOAuth2User(userDto);
    }

    private OAuth2Response getOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        OAuth2Response oAuth2Response;
        if(registrationId.equals(AuthProvider.KAKAO.getAuthProvider())) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if(registrationId.equals(AuthProvider.GOOGLE.getAuthProvider())) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            throw new CustomException(ErrorCode.NOT_VALID_FIELD);
        }
        return oAuth2Response;
    }
}
