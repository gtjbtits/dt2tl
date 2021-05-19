package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmChatNotFoundException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmTribeNotActiveException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmUserNotFoundException;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.service.TribeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmTribeService.MAX_REVIEWERS_COUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = {TlgrmContextTestConfiguration.class})
class TlgrmTribeServiceTest {

  public static final long FIRST_TEAM_ID = 1L;
  public static final long SECOND_TEAM_ID = 2L;

  @Autowired
  private TribeService<TlgrmChatContext, TlgrmUserContext> tribeService;

  @Test
  @Sql("classpath:./sql/tribe-configuration-with-full-multiple-teams.sql")
  void findReviewers_Must_FindOneReviewerFromSameTeamAndOneFromOthersIfConfigurationHasEnoughMembers() {
    final List<Fellow> reviewers = this.tribeService.findReviewers(new TlgrmUserContext(1000L, 1000L));
    assertThat(reviewers)
        .extracting("id").doesNotContain(1L);
    assertThat(reviewers).hasSize(MAX_REVIEWERS_COUNT);
    assertThat(reviewers).map(Fellow::getTeam)
        .extracting("id").containsExactly(FIRST_TEAM_ID, SECOND_TEAM_ID);
  }

  @Test
  @Sql("classpath:./sql/tribe-configuration-with-full-multiple-teams.sql")
  void findReviewers_Must_ThrowExceptionIfTribeDoesNotExistsForChat() {
    final var userContext = new TlgrmUserContext(-1L, 1000L);
    assertThatThrownBy(() -> this.tribeService.findReviewers(userContext))
        .isInstanceOf(TlgrmChatNotFoundException.class);
  }

  @Test
  @SqlGroup({
      @Sql("classpath:./sql/tribe-configuration-with-full-multiple-teams.sql"),
      @Sql(statements = {"update tribes set active = false where id = 1"})
  })
  void findReviewers_Must_ThrowExceptionIfTribeIsNotActive() {
    final var userContext = new TlgrmUserContext(1000L, 1000L);
    assertThatThrownBy(() -> this.tribeService.findReviewers(userContext))
        .isInstanceOf(TlgrmTribeNotActiveException.class);
  }

  @Test
  @Sql("classpath:./sql/tribe-configuration-with-full-multiple-teams.sql")
  void findReviewers_Must_ThrownExceptionWhenAuthorNotFound() {
    final var userContext = new TlgrmUserContext(1000L, -1L);
    assertThatThrownBy(() -> this.tribeService.findReviewers(userContext))
        .isInstanceOf(TlgrmUserNotFoundException.class);
  }
}