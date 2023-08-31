package com.soulcode.goserviceapp.event;

import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.domain.UsuarioLog;
<<<<<<< HEAD
=======
import com.soulcode.goserviceapp.repository.UsuarioRepository;
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5
import com.soulcode.goserviceapp.service.UsuarioLogService;
import com.soulcode.goserviceapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioLogService usuarioLogService;

    @EventListener
<<<<<<< HEAD
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        try {
=======
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        try{
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5
            String email = event.getAuthentication().getName();
            Usuario usuario = usuarioService.findByEmail(email);
            UsuarioLog usuarioLog = new UsuarioLog(usuario);
            usuarioLogService.create(usuarioLog);
<<<<<<< HEAD
        }catch (Exception ex){
            ex.printStackTrace();

        }
    }

=======
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
>>>>>>> 4fd336c9acba17650f0de7db8936f954fa0495f5
}
