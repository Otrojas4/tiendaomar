package com.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecom.dao.UserDao;
import com.ecom.entity.JwtRequest;
import com.ecom.entity.JwtResponse;
import com.ecom.entity.User;
import com.ecom.util.JwtUtil;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

   

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        UserDetails userDetails =  authenticate(userName, userPassword);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userDao.findById(userName).get();
        return new JwtResponse(user, newGeneratedToken);
    }
/**
 * Este método recibe el nombre de usuario como argumento y devuelve un objeto UserDetails 
 * que representa al usuario correspondiente. La implementación busca al usuario en la base de datos utilizando 
 * el método findById del objeto userDao. Si el usuario es encontrado, se crea un objeto 
 * UserDetails utilizando la clase org.springframework.security.core.userdetails.User, 
 * que toma el nombre de usuario, la contraseña (o su representación encriptada) 
 * y las autoridades (roles y permisos) asociadas con el usuario. Las autoridades se obtienen utilizando el método getAuthority, 
 * que crea instancias de SimpleGrantedAuthority a partir de los roles del usuario.
 * 
 * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
    /**
     * Este método se utiliza para obtener los roles del usuario y convertirlos en instancias de SimpleGrantedAuthority, 
     * que son necesarias para la creación de objetos UserDetails
     * 
     * 
     *  
     *  */

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }
/*
    private UserDetails authenticate(String userName, String userPassword) throws Exception {
    	try {
    	    Optional<User> optionalUser = userDao.findById(userName);
            
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("User not found!");
            }

            User user = optionalUser.get();

            if (!user.getUserPassword().equals(userPassword)) {
                throw new AuthenticationServiceException("Invalid credentials!");
            }


            UserDetails userDetails = loadUserByUsername(userName);

            // Crear un UserDetails completo con nombre de usuario, contraseña y autoridades
            return userDetails;
            
			
		}catch (IllegalArgumentException e) {
            System.err.println("Error generating JWT: " + e.getMessage());
            throw new Exception("Error generating JWT", e);
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.toString());
            throw new Exception("Unknown error", e);
        }
    }*/
    
    private UserDetails authenticate(String userName, String userPassword) throws Exception {
        try {
            Optional<User> optionalUser = userDao.findById(userName);

            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("User not found!");
            }

            User user = optionalUser.get();

            if (!user.getUserPassword().equals(userPassword)) {
                throw new AuthenticationServiceException("Invalid credentials!");
            }

            // Obtener UserDetails completo con nombre de usuario, contraseña y autoridades
            UserDetails userDetails = loadUserByUsername(userName);

            return userDetails;

        } catch (IllegalArgumentException e) {
            System.err.println("Error generating JWT: " + e.getMessage());
            throw new Exception("Error generating JWT", e);
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.toString());
            throw new Exception("Unknown error", e);
        }
    }

}
