package com.jbtits.github2telegram.service.tlgrm.cfg;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TlgrmConfigurationMessagesKeyValueService {

  private final ConcurrentMap<Long, Object> messageIds = new ConcurrentHashMap<>();

  public void putConfigurationMessageId(final long id) {
    messageIds.put(id, new Object());
  }

  public boolean isConfigurationMessageId(final long id) {
    return messageIds.containsKey(id);
  }

  public void removeAllConfigurationMessageIds() {
    messageIds.clear();
  }
}
