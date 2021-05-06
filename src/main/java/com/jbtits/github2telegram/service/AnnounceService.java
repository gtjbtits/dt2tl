package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.domain.dto.announce.CodeReviewAnnounce;
import com.jbtits.github2telegram.domain.dto.announce.MentionAnnounce;
import com.jbtits.github2telegram.domain.dto.announce.TextAnnounce;
import lombok.NonNull;

public interface AnnounceService {

  void makeAnnounceForReviewers(@NonNull CodeReviewAnnounce announceDto);

  void mention(@NonNull MentionAnnounce mentionAnnounce);

  void sendText(@NonNull TextAnnounce textAnnounce);
}
