package com.typeface.notification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.JSONException;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import org.json.JSONObject;

@Builder
@Getter
@Setter
public class UserRequestDetail extends AbstractUserRequestDetails {

    @NonNull
    private String email;

    private Long userID;

    @Override
    public String getEmail() {
        return validateAndGetData(email, "emailNotFound");
    }

    @Override
    public Long getUserID() {
        return validateAndGetData(userID, "userNotFound");
    }

    private<T> T validateAndGetData(T data, String key) {
        if (data == null) {
            throw new AccessDeniedException("Invalid Data");
        }
        return data;
    }

}

