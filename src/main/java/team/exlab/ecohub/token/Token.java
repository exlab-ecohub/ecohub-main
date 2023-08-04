package team.exlab.ecohub.token;


import lombok.*;
import team.exlab.ecohub.user.model.User;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String refreshToken;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    @Column(name = "token_purpose")
    @Enumerated(EnumType.STRING)
    private TokenPurpose tokenPurpose;
    @Column(name = "remember_me")
    private boolean rememberMe;
    private boolean revoked;

    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
