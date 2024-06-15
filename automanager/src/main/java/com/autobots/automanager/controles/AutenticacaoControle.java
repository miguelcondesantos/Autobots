package com.autobots.automanager.controles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.configuracao.LoginResponse;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.jwt.ProvedorJwt;

@RestController
@RequestMapping("/auth")
public class AutenticacaoControle {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;
    
    @Autowired
    private UserDetailsServiceImpl servico;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialUsuarioSenha credenciais) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credenciais.getNomeUsuario(), credenciais.getSenha()));           
            String jwt = provedorJwt.proverJwt(credenciais.getNomeUsuario());
            Usuario usuario = (servico).obterPorNome(credenciais.getNomeUsuario());
            LoginResponse response = new LoginResponse("Bearer " + jwt, usuario.getPerfis());
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + ex.getMessage());
        }
    }

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        return new ResponseEntity<>("Logout realizado com sucesso", HttpStatus.OK);
    }
}
