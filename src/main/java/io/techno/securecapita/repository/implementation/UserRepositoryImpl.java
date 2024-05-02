package io.techno.securecapita.repository.implementation;

import io.techno.securecapita.domain.Role;
import io.techno.securecapita.domain.User;
import io.techno.securecapita.exception.ApiException;
import io.techno.securecapita.repository.RoleRepository;
import io.techno.securecapita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static io.techno.securecapita.enumeration.RoleType.ROLE_USER;
import static io.techno.securecapita.enumeration.VerificationType.ACCOUNT;
import static io.techno.securecapita.query.UserQuery.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;
    @Override
    public User create(User user) {
        // check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already in use . Please use a different email and try again.");
        // save new user
        try{
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameter = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY , parameter , holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
            // add role to the user
            roleRepository.addRoleToUser(user.getId() , ROLE_USER.name());
            // send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString() , ACCOUNT.getType());
            // Save URL in verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY , Map.of("userId" , user.getId() , "url" , verificationUrl));
            // Send email to user with  verification URL
            //emailService.sendVerificationUrl(user.getFirstName() , user.getEmail() , verificationUrl , ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);
            // Return the newly created user
            return user;
            // if any errors, throw exception with proper message
        /*} catch (EmptyResultDataAccessException exception){
            throw new ApiException("No role found by name :" + ROLE_USER.name());*/
        } catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occured . Please try again.");
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return true;
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY , Map.of("email", email ) , Integer.class);
    }
    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName" , user.getFirstName())
                .addValue("lastName" , user.getLastName())
                .addValue("email" , user.getEmail())
                .addValue("password" , encoder.encode(user.getPassword()) );

    }
    private String getVerificationUrl( String key , String type)
    {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path( "/user/verify/" + type + "/" + key).toUriString();
    }
}
