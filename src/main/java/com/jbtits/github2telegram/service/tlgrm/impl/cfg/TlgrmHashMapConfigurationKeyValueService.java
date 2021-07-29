package com.jbtits.github2telegram.service.tlgrm.impl.cfg;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TlgrmHashMapConfigurationKeyValueService
    implements ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> {

  private final ConcurrentMap<Long, TribeConfiguration<TlgrmChatContext, TlgrmUserContext>> map
      = new ConcurrentHashMap<>();

  @Override
  public void put(@NonNull final Long key,
                  @NonNull final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> value) {
    map.put(key, value);
  }

  @Override
  public Optional<TribeConfiguration<TlgrmChatContext, TlgrmUserContext>> get(@NonNull final Long key) {
    return Optional.ofNullable(map.get(key));
  }

  @Override
  public void remove(final @NonNull Long key) {
    map.remove(key);
  }


}
