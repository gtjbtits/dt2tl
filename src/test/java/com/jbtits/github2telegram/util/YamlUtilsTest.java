package com.jbtits.github2telegram.util;

import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmFellow;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmTeam;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmTribe;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class YamlUtilsTest {

  @Test
  @SneakyThrows
  void correctYamlConfigurationFile_Must_beCorrectParsedIntoClasses() {
    final File ymlCfg = ResourceUtils.getFile("classpath:tlgrm-config-example.yml");
    final TlgrmTribe tlgrmTribe = YamlUtils.parse(ymlCfg, TlgrmTribe.class);
    assertThat(tlgrmTribe).isNotNull();
    final Set<TlgrmTeam> tlgrmTeamSet = tlgrmTribe.getTeams();
    assertThat(tlgrmTeamSet).hasSize(1);
    final TlgrmTeam tlgrmTeam = tlgrmTeamSet.toArray(TlgrmTeam[]::new)[0];
    assertThat(tlgrmTeam).isNotNull();
    assertThat(tlgrmTeam.getName()).isEqualTo("devTeam");
    assertThat(tlgrmTeam.getFellows()).isNotNull();
    final Set<TlgrmFellow> tlgrmFellowSet = tlgrmTeam.getFellows();
    assertThat(tlgrmFellowSet).hasSize(2);
    final TlgrmFellow tlgrmFellow1 = new TlgrmFellow();
    tlgrmFellow1.setUsername("lolkek1");
    final TlgrmFellow tlgrmFellow2 = new TlgrmFellow();
    tlgrmFellow2.setUsername("lolkek2");
    assertThat(tlgrmFellowSet).contains(tlgrmFellow1, tlgrmFellow2);
  }
}
