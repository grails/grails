package org.grails.ft.realms;

import org.jsecurity.authc.AuthenticationInfo;
import org.jsecurity.authc.AuthenticationToken;
import org.jsecurity.grails.RealmWrapper;

public class CustomRealm extends RealmWrapper {
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) {
        return null;
    }

    public boolean supports(AuthenticationToken token) {
        return false;
    }
}
