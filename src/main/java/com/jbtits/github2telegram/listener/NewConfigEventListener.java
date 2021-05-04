package com.jbtits.github2telegram.listener;

import com.jbtits.github2telegram.domain.event.NewConfigEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewConfigEventListener {

  @EventListener
  public void handleNewConfigEvent(@NonNull NewConfigEvent event) {
    log.info("New configuration obtained: {}", event);
  }
}
