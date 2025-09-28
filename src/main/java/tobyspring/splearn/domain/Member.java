package tobyspring.splearn.domain;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import lombok.Getter;
import lombok.ToString;

// 상태의 변경이 일어나면 한번에 변경하는게 좋음. 세터보다는 한번에 변경
@Getter
@ToString
public class Member {
  private Email email;

  private String passwordHash;

  private String nickname;

  private MemberStatus status;

  private Member() {
  }

  public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
    Member member = new Member();

    member.email = new Email(registerRequest.email());
    member.nickname = requireNonNull(registerRequest.nickname());
    member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));

    member.status = MemberStatus.PENDING;

    return member;
  }

  public void activate() {
    state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

    this.status = MemberStatus.ACTIVE;
  }

  public void deactivate() {
    state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

    this.status = MemberStatus.DEACTIVATED;
  }

  public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, this.passwordHash);
  }

  public void changeNickname(String nickname) {
    this.nickname = requireNonNull(nickname);
  }

  public void changePassword(String password, PasswordEncoder passwordEncoder) {
    this.passwordHash = passwordEncoder.encode(requireNonNull(password));
  }

  public boolean isActive() {
    return status == MemberStatus.ACTIVE;
  }
}
