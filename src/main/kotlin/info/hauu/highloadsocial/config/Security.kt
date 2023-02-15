package info.hauu.highloadsocial.config

import info.hauu.highloadsocial.service.UserService
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


private val lg = KotlinLogging.logger {}

@EnableWebSecurity
@Configuration
class Security(
    @Autowired
    val userService: UserService
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: HttpSecurity?) {
        if (web == null) {
            return;
        }
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .and()
            .authenticationProvider(AuthenticationProvider(userService))
            .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter::class.java)
            .authorizeRequests()
            .requestMatchers(matcher())
            .authenticated()
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable()
    }

    private fun authenticationFilter(): Filter {
        val tokenFilter = TokenFilter(matcher())
        tokenFilter.setAuthenticationManager(authenticationManager())
        return tokenFilter
    }

    override fun configure(webSecurity: WebSecurity) {
        webSecurity.ignoring().antMatchers("/user/register")
    }

    @Bean
    fun matcher(): RequestMatcher {
        return OrRequestMatcher(
            AntPathRequestMatcher("/post/**"),
            AntPathRequestMatcher("/user/**"),
            AntPathRequestMatcher("/friend/**"),
            AntPathRequestMatcher("/dialog/**"),
        )
    }
}

class TokenFilter(auth: RequestMatcher) : AbstractAuthenticationProcessingFilter(auth) {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        lg.info("Attempted to auth: $request")

        var token: String? = request?.getHeader("AUTHORIZATION")

        if (token == null || !token.startsWith("Bearer")) {
            throw BadCredentialsException("Token $token is not recognized")
        }
        token = StringUtils.removeStart(token, "Bearer").trim()
        val requestAuthentication: Authentication = UsernamePasswordAuthenticationToken(token, token)
        return authenticationManager.authenticate(requestAuthentication)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain?.doFilter(request, response);
    }
}

@Component
class AuthenticationProvider(
    @Autowired
    val userService: UserService
) : AbstractUserDetailsAuthenticationProvider() {
    override fun additionalAuthenticationChecks(
        userDetails: UserDetails?,
        authentication: UsernamePasswordAuthenticationToken?
    ) {
        lg.debug("No additional authentication checks")
    }

    override fun retrieveUser(username: String?, authentication: UsernamePasswordAuthenticationToken?): UserDetails? {
        return with(authentication?.credentials) {
            userService.findIdByToken(this as String)
        }
            .let { token -> userService.loadUserByUsername(token) }
            .let { lg.info("Authenticated: $it"); it }
    }

}