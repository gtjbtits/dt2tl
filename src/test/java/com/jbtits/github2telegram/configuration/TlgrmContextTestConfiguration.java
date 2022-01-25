package com.jbtits.github2telegram.configuration;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.helpers.impl.DefaultTlgrmMetaHelper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class TlgrmContextTestConfiguration {

  public static final long TLGRM_TEST_CHAT_ID_WITH_TITLE = 777777L;
  public static final Chat TLGRM_TEST_CHAT_WITH_TITLE = new Chat();

  public static final long TLGRM_TEST_CHAT_ID_WITH_EMPTY_TITLE = 777778L;
  public static final Chat TLGRM_TEST_CHAT_WITH_EMPTY_TITLE = new Chat();

  public static final long TLGRM_TEST_CHAT_ID_WITH_NULL_TITLE = 777779L;
  public static final Chat TLGRM_TEST_CHAT_WITH_NULL_TITLE = new Chat();

  public static final String TLGRM_TEST_CHAT_TITLE = "Tlgrm test chat";

  public static final ChatMember TLGRM_TEST_CHAT_WITH_TITLE_FELLOW_1 = new ChatMemberMember();

  public static final String FELLOW_1_NAME = "test_fellow_1";
  public static final long FELLOW_1_USER_ID = 1111L;
  public static final String FELLOW_2_NAME = "test_fellow_2";
  public static final long FELLOW_2_USER_ID = 2222L;
  public static final String FELLOW_3_NAME = "test_fellow_3";
  public static final long FELLOW_3_USER_ID = 3333L;

  static {
    TLGRM_TEST_CHAT_WITH_TITLE.setTitle(TLGRM_TEST_CHAT_TITLE);
    TLGRM_TEST_CHAT_WITH_EMPTY_TITLE.setTitle(Strings.EMPTY);

    final var user1 = new User();
    user1.setId(FELLOW_1_USER_ID);
    user1.setFirstName("First Name");
    ((ChatMemberMember) TLGRM_TEST_CHAT_WITH_TITLE_FELLOW_1).setUser(user1);
  }

  @Bean
  TelegramBotInitializer telegramBotInitializer() {
    return mock(TelegramBotInitializer.class);
  }

  @Bean
  TlgrmSender tlgrmSender() {
    final TlgrmSender tlgrmSender = mock(TlgrmSender.class);
    when(tlgrmSender.getChatMetadata(TLGRM_TEST_CHAT_ID_WITH_TITLE)).thenReturn(TLGRM_TEST_CHAT_WITH_TITLE);
    when(tlgrmSender.getChatMetadata(TLGRM_TEST_CHAT_ID_WITH_EMPTY_TITLE)).thenReturn(TLGRM_TEST_CHAT_WITH_EMPTY_TITLE);
    when(tlgrmSender.getChatMetadata(TLGRM_TEST_CHAT_ID_WITH_NULL_TITLE)).thenReturn(TLGRM_TEST_CHAT_WITH_NULL_TITLE);
    when(tlgrmSender.getChatMemberMetadata(TLGRM_TEST_CHAT_ID_WITH_TITLE, FELLOW_1_USER_ID))
        .thenReturn(TLGRM_TEST_CHAT_WITH_TITLE_FELLOW_1);
    return tlgrmSender;
  }

  @Bean
  TlgrmMetaHelper tlgrmMetaHelper() {
    final TlgrmMetaHelper tlgrmMetaHelper = mock(DefaultTlgrmMetaHelper.class);
    when(tlgrmMetaHelper.extractName(any())).thenCallRealMethod();
    return tlgrmMetaHelper;
  }
}
