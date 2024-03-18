// package individ.site;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// // import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
// // import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
// // import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// // import org.springframework.security.core.userdetails.User;
// // import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// // import org.springframework.security.provisioning.JdbcUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;
// // import individ.site.repo.UserRepository;
// import static org.springframework.security.config.Customizer.*;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



// // import javax.sql.DataSource;
// // import javax.xml.crypto.Data;
// // import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfiguration {

//     @Autowired
//     UserDetailsService userDetailsService;

//     @Autowired
//     public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//         auth.userDetailsService(userDetailsService);
//     }

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(authorizeRequests ->
//                 authorizeRequests
//                     .requestMatchers("/admin").hasAuthority("ADMIN")
//                     .requestMatchers("/user").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/emperial").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/employees**", "/employee**", "/employee/**", "/employee**/**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/employees/**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/employees**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/employees/add").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/employees/filter").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/employee**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/").permitAll()
//                     .requestMatchers("/department/**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/position**/**").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/position**").hasAnyAuthority("ADMIN", "User")  
//                     .requestMatchers("/payroll**").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/payroll/**").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/payrolls**").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/payrolls/**").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/payrolls/add").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/taxes/**").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/taxes/list").hasAnyAuthority("ADMIN", "User") 
//                     .requestMatchers("/departments/**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/positions/**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/tax**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/tax**/**").hasAnyAuthority("ADMIN", "User")
//                     .requestMatchers("/about").hasAnyAuthority("ADMIN", "User")
//                     // Allow the error page to be accessed by anyone
//                     .requestMatchers("/error").permitAll()
//                     .requestMatchers("/test").permitAll()

//             )
//             .formLogin(form -> form
//             .loginPage("/login") 
//             .defaultSuccessUrl("/employees", true) // Redirect after successful login
//             .failureUrl("/login?error=true") // Redirect on failed login 
//             .permitAll() 
//             );
//         return http.build();
//     }

//     // Password encoding is not needed in this configuration.
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return NoOpPasswordEncoder.getInstance();
//     }

// }

// // 
// // http
// // .csrf(csrf -> csrf.disable())
// // .authorizeHttpRequests(authorizeRequests ->
// //     authorizeRequests
// //         .requestMatchers("/**").permitAll()
// // )
// // .formLogin(withDefaults());
// // return http.build();
