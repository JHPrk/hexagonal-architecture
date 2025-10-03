package tobyspring.splearn.application.required;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;

/**
 * 회원 정보를 저장하거나 조회한다.
 * 복잡해지면 읽기용 쓰기용 분리할 수 있음
 */
public interface MemberRepository extends Repository<Member, Long> {
  Member save(Member member);

  Optional<Member> findByEmail(Email email);

  Optional<Member> findById(Long memberId);
}
