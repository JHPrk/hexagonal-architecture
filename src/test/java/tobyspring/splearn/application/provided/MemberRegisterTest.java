package tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

  @Test
  void register() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

    assertThat(member.getId()).isNotNull();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void duplicateEmailFail() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

    assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
        .isInstanceOf(DuplicateEmailException.class);
  }

  @Test
  void activate() {
    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
    entityManager.flush();
    entityManager.clear();

    member = memberRegister.activate(member.getId());

    entityManager.flush();
    assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);

  }


  @Test
  void memberRegisterRequestFail() {
    invalidRequest(new MemberRegisterRequest("toby@spelearn.app", "Toby", "longsecret"));
    invalidRequest(new MemberRegisterRequest("toby@spelearn.app", "Charlie___________________________________________", "longsecret"));
    invalidRequest(new MemberRegisterRequest("tobyspelearn.app", "Charlie", "longsecret"));
    invalidRequest(new MemberRegisterRequest("toby@spelearn.app", "Charlie", "short"));
  }

  private void invalidRequest(MemberRegisterRequest invalid) {
    assertThatThrownBy(() -> memberRegister.register(invalid))
        .isInstanceOf(ConstraintViolationException.class);
  }
}
