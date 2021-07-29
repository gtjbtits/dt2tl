package com.jbtits.github2telegram.service.tlgrm.impl.cfg;

import com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.service.tlgrm.impl.TlgrmCachedMetaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_ID_WITH_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
    TlgrmContextTestConfiguration.class,
    TlgrmHashMapConfigurationKeyValueService.class,
    TlgrmConfigurationWizardService.class,
    TlgrmCachedMetaService.class
})
class TlgrmHashMapConfigurationKeyValueServiceTest {

  @Autowired
  private TlgrmHashMapConfigurationKeyValueService keyValueService;

  @Autowired
  private TlgrmConfigurationWizardService wizardService;

  @Test
  void get_Must_returnOptionalEmptyIfNoConfigurationWasCachedBeforeWithSpecifiedKey() {
    final TlgrmUserContext tlgrmUserContext = new TlgrmUserContext(0L, 0L);
    final Optional<TribeConfiguration<TlgrmChatContext, TlgrmUserContext>> tribeConfiguration
        = this.keyValueService.get(0L);
    assertThat(tribeConfiguration).isNotPresent();
  }

  @Test
  void get_Must_returnConfigurationIfITWasCachedBefore() {
    final var tlgrmChatContext = new TlgrmChatContext(TLGRM_TEST_CHAT_ID_WITH_TITLE);
    final var tribeConfiguration
        = this.wizardService.generateEmptyConfiguration(tlgrmChatContext);
    final var tlgrmUserContext = new TlgrmUserContext(tlgrmChatContext, 0L);
    this.keyValueService.put(TLGRM_TEST_CHAT_ID_WITH_TITLE, tribeConfiguration);
    final var cachedConfiguration
        = this.keyValueService.get(TLGRM_TEST_CHAT_ID_WITH_TITLE);
    assertThat(cachedConfiguration).get().isEqualTo(tribeConfiguration);
  }
}