package com.jbtits.github2telegram.util;

import com.jbtits.github2telegram.domain.dto.entity.FellowRequest;
import com.jbtits.github2telegram.domain.dto.entity.TeamRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
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
    final TribeRequest tribeRequest = YamlUtils.parse(ymlCfg, TribeRequest.class);
    assertThat(tribeRequest).isNotNull();
    final Set<TeamRequest> teamRequestSet = tribeRequest.getTeams();
    assertThat(teamRequestSet).hasSize(1);
    final TeamRequest teamRequest = teamRequestSet.toArray(TeamRequest[]::new)[0];
    assertThat(teamRequest).isNotNull();
    assertThat(teamRequest.getName()).isEqualTo("devTeam");
    assertThat(teamRequest.getFellows()).isNotNull();
    final Set<FellowRequest> fellowRequestSet = teamRequest.getFellows();
    assertThat(fellowRequestSet).hasSize(2);
    final FellowRequest fellowRequest1 = new FellowRequest();
    fellowRequest1.setName("Leopold Stotch");
    fellowRequest1.setUsername("lolkek1");
    final FellowRequest fellowRequest2 = new FellowRequest();
    fellowRequest2.setName("Big Mamba");
    fellowRequest2.setUsername("lolkek2");
    assertThat(fellowRequestSet).contains(fellowRequest1, fellowRequest2);
  }
}
