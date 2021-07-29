package com.jbtits.github2telegram.service.tlgrm.impl.cfg;

import com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.FellowConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Team;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmChat;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmChatService;
import com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmUserService;
import com.jbtits.github2telegram.service.cfg.ConfigurationPersistenceService;
import com.jbtits.github2telegram.service.cfg.ConfigurationWizardService;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.FELLOW_1_NAME;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.FELLOW_1_USER_ID;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.FELLOW_2_NAME;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.FELLOW_2_USER_ID;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.FELLOW_3_NAME;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.FELLOW_3_USER_ID;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_ID_WITH_TITLE;
import static com.jbtits.github2telegram.configuration.TlgrmContextTestConfiguration.TLGRM_TEST_CHAT_TITLE;
import static com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration.DEFAULT_TEAM_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ContextConfiguration(classes = {TlgrmContextTestConfiguration.class})
class TlgrmConfigurationPersistenceServiceTest {

	@Autowired
	private ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> tlgrmConfigurationPersistenceService;

	@Autowired
	private ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService;

	@Autowired
	private TlgrmChatService tlgrmChatService;

	@Autowired
	private TlgrmUserService tlgrmUserService;

	@Test
	void get_Must_ReturnEmptyIfNoConfigurationWithSpecifiedIdHasBeenPersisted() {
		final var configuration
				= tlgrmConfigurationPersistenceService.get(new TlgrmChatContext(0L));
		assertThat(configuration).isEmpty();
	}

	@Test
	@Transactional
	void cps_Must_SaveAndReturnExactlyTheSameConfigurationIfNoConfigurationWasSavedBefore() {
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> savingConfiguration
				= this.buildBasicTribeConfiguration();
		tlgrmConfigurationPersistenceService.save(savingConfiguration);

		final var savedConfiguration
				= tlgrmConfigurationPersistenceService.get(savingConfiguration.getContext());
		assertThat(savedConfiguration).get().isEqualTo(savingConfiguration);

		final List<TlgrmChat> tlgrmChats = this.tlgrmChatService.findAll();
		assertThat(tlgrmChats).hasSize(1);
		final TlgrmChat tlgrmChat = tlgrmChats.get(0);
		assertThat(tlgrmChat).hasFieldOrPropertyWithValue("tlgrmChatId", TLGRM_TEST_CHAT_ID_WITH_TITLE);
		final Tribe tribe = tlgrmChat.getTribe();
		assertThat(tribe)
				.isNotNull()
				.hasFieldOrPropertyWithValue("active", false)
				.hasFieldOrPropertyWithValue("name", TLGRM_TEST_CHAT_TITLE);
		final List<Team> teams = tribe.getTeams();
		assertThat(teams).hasSize(1);
		final Team team = teams.stream().findFirst().orElseThrow();
		assertThat(team).hasFieldOrPropertyWithValue("name", DEFAULT_TEAM_NAME);
		final List<Fellow> fellows = team.getFellows();
		assertThat(fellows).hasSize(2);
		final List<String> fellowNames = fellows.stream()
				.map(Fellow::getName)
				.collect(Collectors.toList());
		assertThat(fellowNames).containsExactly(FELLOW_1_NAME, FELLOW_2_NAME);
		final List<TlgrmUser> tlgrmUsers = this.tlgrmUserService.findAll();
		final Set<Long> tlgrmUserIds = tlgrmUsers.stream()
				.map(TlgrmUser::getTlgrmUserId)
				.collect(Collectors.toSet());
		assertThat(tlgrmUserIds).containsExactly(FELLOW_1_USER_ID, FELLOW_2_USER_ID);
	}

	@Test
	@Transactional
	void cps_Must_AppendNewFellowWhenItHasBeenAddedToConfiguration() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
				= this.buildBasicTribeConfiguration();
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		final List<FellowConfiguration<TlgrmUserContext>> fellowConfigurations
				= tribeConfiguration.getTeams().stream()
				.findFirst().orElseThrow()
				.getFellows();
		fellowConfigurations.add(buildFellowConfiguration(tlgrmChatContext, FELLOW_3_NAME, FELLOW_3_USER_ID));
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		assertThat(this.tlgrmConfigurationPersistenceService.get(tlgrmChatContext)).get().isEqualTo(tribeConfiguration);

		final List<Fellow> fellows = tlgrmChatService.findByTlgrmChatId(tlgrmChatContext.getChatId()).orElseThrow()
				.getTribe()
				.getTeams().stream()
				.findFirst().orElseThrow()
				.getFellows();
		assertThat(fellows).hasSize(3);
		final List<String> fellowNames = fellows.stream()
				.map(Fellow::getName)
				.collect(Collectors.toList());
		assertThat(fellowNames).containsExactlyInAnyOrder(FELLOW_1_NAME, FELLOW_2_NAME, FELLOW_3_NAME);
		final List<TlgrmUser> tlgrmUsers = this.tlgrmUserService.findAll();
		final Set<Long> tlgrmUserIds = tlgrmUsers.stream()
				.map(TlgrmUser::getTlgrmUserId)
				.collect(Collectors.toSet());
		assertThat(tlgrmUserIds).containsExactlyInAnyOrder(FELLOW_1_USER_ID, FELLOW_2_USER_ID, FELLOW_3_USER_ID);
	}

	@Test
	@Transactional
	void cps_Must_IgnoreTryToAppendSameFellowToTheSameTeam() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
				= this.buildBasicTribeConfiguration();
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		final TeamConfiguration<TlgrmUserContext> teamConfiguration
				= tribeConfiguration.getTeams().stream()
				.findFirst().orElseThrow();
		this.configurationWizardService.changeTeam(teamConfiguration.getName(), tribeConfiguration,
				new TlgrmUserContext(tlgrmChatContext, FELLOW_1_USER_ID));
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		assertThat(this.tlgrmConfigurationPersistenceService.get(tlgrmChatContext)).get().isEqualTo(tribeConfiguration);

		final List<Fellow> fellows = tlgrmChatService.findByTlgrmChatId(tlgrmChatContext.getChatId()).orElseThrow()
				.getTribe()
				.getTeams().stream()
				.findFirst().orElseThrow()
				.getFellows();
		assertThat(fellows).hasSize(2);
		final List<String> fellowNames = fellows.stream()
				.map(Fellow::getName)
				.collect(Collectors.toList());
		assertThat(fellowNames).containsExactlyInAnyOrder(FELLOW_1_NAME, FELLOW_2_NAME);
		final List<TlgrmUser> tlgrmUsers = this.tlgrmUserService.findAll();
		final Set<Long> tlgrmUserIds = tlgrmUsers.stream()
				.map(TlgrmUser::getTlgrmUserId)
				.collect(Collectors.toSet());
		assertThat(tlgrmUserIds).containsExactlyInAnyOrder(FELLOW_1_USER_ID, FELLOW_2_USER_ID);
	}

	@Test
	@Transactional
	void cps_Must_DeleteExcessFellowWheItHasBeenRemovedFromConfiguration() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
				= this.buildBasicTribeConfiguration();
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		final List<FellowConfiguration<TlgrmUserContext>> fellowConfigurations
				= tribeConfiguration.getTeams().stream()
				.findFirst().orElseThrow()
				.getFellows();
		fellowConfigurations.remove(1);
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		assertThat(this.tlgrmConfigurationPersistenceService.get(tlgrmChatContext)).get().isEqualTo(tribeConfiguration);

		final List<Fellow> fellows = tlgrmChatService.findByTlgrmChatId(tlgrmChatContext.getChatId()).orElseThrow()
				.getTribe()
				.getTeams().stream()
				.findFirst().orElseThrow()
				.getFellows();
		assertThat(fellows).hasSize(1);
		final List<String> fellowNames = fellows.stream()
				.map(Fellow::getName)
				.collect(Collectors.toList());
		assertThat(fellowNames).containsExactlyInAnyOrder(FELLOW_1_NAME);
		final List<TlgrmUser> tlgrmUsers = this.tlgrmUserService.findAll();
		final Set<Long> tlgrmUserIds = tlgrmUsers.stream()
				.map(TlgrmUser::getTlgrmUserId)
				.collect(Collectors.toSet());
		assertThat(tlgrmUserIds).containsExactlyInAnyOrder(FELLOW_1_USER_ID);
	}

	@Test
	@Transactional
	void cps_Must_AppendNewTeamWhenItHasBeenAddedToConfiguration() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
				= this.buildBasicTribeConfiguration();
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		final String secondTeamName = "SecondDevTeam";
		final TeamConfiguration<TlgrmUserContext> teamConfiguration = new TeamConfiguration<>(secondTeamName);
		tribeConfiguration.getTeams().add(teamConfiguration);
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		assertThat(this.tlgrmConfigurationPersistenceService.get(tlgrmChatContext)).get().isEqualTo(tribeConfiguration);

		final List<Team> teams = this.tlgrmChatService.findByTlgrmChatId(tlgrmChatContext.getChatId()).orElseThrow()
				.getTribe().getTeams();
		assertThat(teams).hasSize(2);

		final Team team1 = teams.stream()
				.filter(t -> t.getName().equals(DEFAULT_TEAM_NAME))
				.findFirst().orElseThrow();
		final List<Fellow> fellows = team1.getFellows();
		assertThat(fellows).hasSize(2);
		final List<String> fellowNames = fellows.stream()
				.map(Fellow::getName)
				.collect(Collectors.toList());
		assertThat(fellowNames).containsExactly(FELLOW_1_NAME, FELLOW_2_NAME);
		final List<TlgrmUser> tlgrmUsers = this.tlgrmUserService.findAll();
		final Set<Long> tlgrmUserIds = tlgrmUsers.stream()
				.map(TlgrmUser::getTlgrmUserId)
				.collect(Collectors.toSet());
		assertThat(tlgrmUserIds).containsExactly(FELLOW_1_USER_ID, FELLOW_2_USER_ID);

		final Team team2 = teams.stream()
				.filter(t -> t.getName().equals(secondTeamName))
				.findFirst().orElseThrow();
		final List<Fellow> team2Fellows = team2.getFellows();
		assertThat(team2Fellows).isEmpty();
	}

	@Test
	@Transactional
	void cps_Must_DeleteExcessTeamWhenItHasBeenRemovedFromConfiguration() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
				= this.buildBasicTribeConfiguration();
		final String secondTeamName = "SecondDevTeam";
		final TeamConfiguration<TlgrmUserContext> teamConfiguration = new TeamConfiguration<>(secondTeamName);
		tribeConfiguration.getTeams().add(teamConfiguration);
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		tribeConfiguration.getTeams().remove(teamConfiguration);
		tlgrmConfigurationPersistenceService.save(tribeConfiguration);
		assertThat(this.tlgrmConfigurationPersistenceService.get(tlgrmChatContext)).get().isEqualTo(tribeConfiguration);

		final List<Team> teams = this.tlgrmChatService.findByTlgrmChatId(tlgrmChatContext.getChatId()).orElseThrow()
				.getTribe().getTeams();
		assertThat(teams).hasSize(1);

		final Team team = teams.stream()
				.filter(t -> t.getName().equals(DEFAULT_TEAM_NAME))
				.findFirst().orElseThrow();
		final List<Fellow> fellows = team.getFellows();
		assertThat(fellows).hasSize(2);
		final List<String> fellowNames = fellows.stream()
				.map(Fellow::getName)
				.collect(Collectors.toList());
		assertThat(fellowNames).containsExactly(FELLOW_1_NAME, FELLOW_2_NAME);
		final List<TlgrmUser> tlgrmUsers = this.tlgrmUserService.findAll();
		final Set<Long> tlgrmUserIds = tlgrmUsers.stream()
				.map(TlgrmUser::getTlgrmUserId)
				.collect(Collectors.toSet());
		assertThat(tlgrmUserIds).containsExactly(FELLOW_1_USER_ID, FELLOW_2_USER_ID);
	}

	@Test
	void cps_Must_RejectSaveTribeConfigurationWithNonUniqueTeamNames() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final var tribeConfiguration = this.configurationWizardService
				.generateEmptyConfiguration(tlgrmChatContext);
		final TeamConfiguration<TlgrmUserContext> teamConfiguration = new TeamConfiguration<>(DEFAULT_TEAM_NAME);
		tribeConfiguration.getTeams().add(teamConfiguration);
		assertThatThrownBy(() -> this.tlgrmConfigurationPersistenceService.save(tribeConfiguration))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@NonNull
	private TribeConfiguration<TlgrmChatContext, TlgrmUserContext> buildBasicTribeConfiguration() {
		final TlgrmChatContext tlgrmChatContext = this.buildTlgrmChatContext();
		final var tribeConfiguration = this.configurationWizardService
				.generateEmptyConfiguration(tlgrmChatContext);
		final List<FellowConfiguration<TlgrmUserContext>> fellowConfigurations
				= tribeConfiguration.getTeams().stream()
				.findFirst().orElseThrow()
				.getFellows();
		fellowConfigurations.add(buildFellowConfiguration(tlgrmChatContext, FELLOW_1_NAME, FELLOW_1_USER_ID));
		fellowConfigurations.add(buildFellowConfiguration(tlgrmChatContext, FELLOW_2_NAME, FELLOW_2_USER_ID));
		return tribeConfiguration;
	}

	@NonNull
	private FellowConfiguration<TlgrmUserContext> buildFellowConfiguration(
			@NonNull final TlgrmChatContext tlgrmChatContext,
			@NonNull final String name,
			final long userId) {
		final TlgrmUserContext tlgrmUserContext = new TlgrmUserContext(tlgrmChatContext.getChatId(), userId);
		return new FellowConfiguration<>(tlgrmUserContext, name);
	}

	@NonNull
	private TlgrmChatContext buildTlgrmChatContext() {
		return new TlgrmChatContext(TLGRM_TEST_CHAT_ID_WITH_TITLE);
	}
}
