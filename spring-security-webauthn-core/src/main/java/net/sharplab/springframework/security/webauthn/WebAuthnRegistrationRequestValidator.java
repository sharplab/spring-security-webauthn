package net.sharplab.springframework.security.webauthn;

import com.webauthn4j.context.RelyingParty;
import com.webauthn4j.context.WebAuthnRegistrationContext;
import com.webauthn4j.context.validator.WebAuthnRegistrationContextValidator;
import com.webauthn4j.util.Base64UrlUtil;
import net.sharplab.springframework.security.webauthn.context.provider.RelyingPartyProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class WebAuthnRegistrationRequestValidator {

    private RelyingPartyProvider relyingPartyProvider;

    private WebAuthnRegistrationContextValidator registrationContextValidator;

    public WebAuthnRegistrationRequestValidator(WebAuthnRegistrationContextValidator registrationContextValidator, RelyingPartyProvider relyingPartyProvider) {
        this.registrationContextValidator = registrationContextValidator;
        this.relyingPartyProvider = relyingPartyProvider;
    }

    public void validate(HttpServletRequest request, HttpServletResponse response,
                         String clientDataBase64,
                         String attestationObjectBase64) {
        WebAuthnRegistrationContext registrationContext = getRegistrationContext(request, response, clientDataBase64, attestationObjectBase64);
        registrationContextValidator.validate(registrationContext);
    }

    WebAuthnRegistrationContext getRegistrationContext(HttpServletRequest request, HttpServletResponse response,
                                                       String clientDataBase64,
                                                       String attestationObjectBase64) {

        byte[] clientDataBytes = Base64UrlUtil.decode(clientDataBase64);
        byte[] attestationObjectBytes = Base64UrlUtil.decode(attestationObjectBase64);
        RelyingParty relyingParty = relyingPartyProvider.provide(request, response);

        return new WebAuthnRegistrationContext(clientDataBytes, attestationObjectBytes, relyingParty);
    }

}
