package com.jbtits.github2telegram.service.cfg;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.NonNull;

import java.util.Optional;

public interface ConfigurationKeyValueService<K, C extends AbstractChatContext, U extends AbstractUserContext> {

  void put(@NonNull final K key, @NonNull final TribeConfiguration<C,U> value);

  Optional<TribeConfiguration<C,U>> get(@NonNull final K key);

  void remove(@NonNull final K key);
}
