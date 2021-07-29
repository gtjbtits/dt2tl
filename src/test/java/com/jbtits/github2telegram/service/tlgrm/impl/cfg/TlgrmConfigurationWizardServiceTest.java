package com.jbtits.github2telegram.service.tlgrm.impl.cfg;

import com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.service.cfg.ConfigurationWizardService;
import com.jbtits.github2telegram.service.tlgrm.impl.TlgrmCachedMetaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_ID_WITH_EMPTY_TITLE;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_ID_WITH_NULL_TITLE;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_ID_WITH_TITLE;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
    TlgrmContextTestConfiguration.class,
    TlgrmConfigurationWizardService.class,
    TlgrmCachedMetaService.class})
class TlgrmConfigurationWizardServiceTest {

  @Autowired
  private ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> tlgrmConfigurationWizardService;

  @Test
  void generateEmptyConfiguration_Must_generateTribeConfigWithChatTitleIfChatTitleIsDefined() {
    final TlgrmChatContext tlgrmChatContext = new TlgrmChatContext(TLGRM_TEST_CHAT_ID_WITH_TITLE);
    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> generatedTribeConfiguration =
        tlgrmConfigurationWizardService.generateEmptyConfiguration(tlgrmChatContext);

    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> expectedTribeConfiguration =
        new TribeConfiguration<>(tlgrmChatContext, TLGRM_TEST_CHAT_TITLE);
    final TeamConfiguration<TlgrmUserContext> expectedTeamConfiguration =
        new TeamConfiguration<>(TeamConfiguration.DEFAULT_TEAM_NAME);
    expectedTribeConfiguration.getTeams().add(expectedTeamConfiguration);
    assertThat(generatedTribeConfiguration).isEqualTo(expectedTribeConfiguration);
  }

  @Test
  void generateEmptyConfiguration_Must_generateTribeConfigurationWithDefaultNameIfChatTitleIsEmpty() {
    final TlgrmChatContext tlgrmChatContext = new TlgrmChatContext(TLGRM_TEST_CHAT_ID_WITH_EMPTY_TITLE);
    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> generatedTribeConfiguration =
        tlgrmConfigurationWizardService.generateEmptyConfiguration(tlgrmChatContext);

    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> expectedTribeConfiguration =
        new TribeConfiguration<>(tlgrmChatContext, TribeConfiguration.DEFAULT_TRIBE_NAME);
    final TeamConfiguration<TlgrmUserContext> expectedTeamConfiguration =
        new TeamConfiguration<>(TeamConfiguration.DEFAULT_TEAM_NAME);
    expectedTribeConfiguration.getTeams().add(expectedTeamConfiguration);
    assertThat(generatedTribeConfiguration).isEqualTo(expectedTribeConfiguration);
  }

  @Test
  void generateEmptyConfiguration_Must_generateTribeConfigurationWithDefaultNameIfChatTitleIsNull() {
    final TlgrmChatContext tlgrmChatContext = new TlgrmChatContext(TLGRM_TEST_CHAT_ID_WITH_NULL_TITLE);
    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> generatedTribeConfiguration =
        tlgrmConfigurationWizardService.generateEmptyConfiguration(tlgrmChatContext);

    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> expectedTribeConfiguration =
        new TribeConfiguration<>(tlgrmChatContext, TribeConfiguration.DEFAULT_TRIBE_NAME);
    final TeamConfiguration<TlgrmUserContext> expectedTeamConfiguration =
        new TeamConfiguration<>(TeamConfiguration.DEFAULT_TEAM_NAME);
    expectedTribeConfiguration.getTeams().add(expectedTeamConfiguration);
    assertThat(generatedTribeConfiguration).isEqualTo(expectedTribeConfiguration);
  }
}