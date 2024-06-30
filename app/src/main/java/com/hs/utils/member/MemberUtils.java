package com.hs.utils.member;

import com.hs.application.member.model.MemberUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberUtils {

    private static final String ANNONYMOUS_USER = "anonymousUser";

    public static MemberUserDetails getMemberUserDetails() {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(ANNONYMOUS_USER))
            return null;
        return (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
