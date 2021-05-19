package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class TlgrmCallbackQueryData {

  @NonNull
  @ToString.Include
  @EqualsAndHashCode.Include
  private final TlgrmCallbackQueryAction action;

  @NonNull
  @ToString.Include
  @EqualsAndHashCode.Include
  private final Object[] args;

  private TlgrmCallbackQueryData(@NonNull final TlgrmCallbackQueryAction action, Object ... args) {
    this.action = action;
    this.args = args != null ? args : new Object[0];
  }

  @NonNull
  public static TlgrmCallbackQueryData decode(@NonNull final String data) throws TlgrmListenerException {
    final var encodedParts = data.split(",");
    if (encodedParts.length == 0) {
      throw new TlgrmListenerException("Parts are empty. Can't parse CallbackQueryData object");
    }
    final var decodedParts = Stream.of(encodedParts)
        .map(Base64.getDecoder()::decode)
        .map(String::new)
        .collect(Collectors.toList());
    final var actionStr = decodedParts.remove(0);
    final var action = TlgrmCallbackQueryAction.fromString(actionStr);
    return new TlgrmCallbackQueryData(action, decodedParts.toArray());
  }

  public static String encode(@NonNull final List<String> data) {
    final var encodedData = data.stream()
        .map(part -> Base64.getEncoder().encode(part.getBytes()))
        .map(String::new)
        .collect(Collectors.toList());
    return String.join(",", encodedData);
  }
}
