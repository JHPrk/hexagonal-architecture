package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void equality() {
    Email email1 = new Email("jhp@splearn.app");
    Email email2 = new Email("jhp@splearn.app");

    assertThat(email1).isEqualTo(email2);

  }


}