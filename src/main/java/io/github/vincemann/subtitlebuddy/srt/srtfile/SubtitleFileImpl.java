package io.github.vincemann.subtitlebuddy.srt.srtfile;

import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.util.List;
import java.util.Optional;

@Log4j2
@Singleton
public class SubtitleFileImpl implements SubtitleFile {

    @Getter
    private List<SubtitleParagraph> subtitles;
    private SubtitleParagraph firstSubtitle;
    private SubtitleParagraph lastSubtitle;


    public SubtitleFileImpl(List<SubtitleParagraph> subtitles) throws EmptySubtitleListException {
        this.subtitles = subtitles;
        this.firstSubtitle = subtitles.get(0);
        if (firstSubtitle == null) {
            throw new EmptySubtitleListException("Emtpy subtitle list");
        }
        log.debug("first subtitle StartingTime : " + firstSubtitle.getStartTime());
        this.lastSubtitle = subtitles.get(subtitles.size() - 1);
        log.debug("last subtitle EndTime: " + lastSubtitle.getEndTime());
    }

    @Override
    public Optional<SubtitleParagraph> getSubtitleAtTimeStamp(Timestamp timestamp) throws TimeStampOutOfBoundsException {
        if (timestamp.isNegative()) {
            throw new TimeStampOutOfBoundsException("negative TimeStamp");
        }

        if (timestamp.compareTo(firstSubtitle.getStartTime()) < 0) {
            return Optional.empty();
        }

        if (timestamp.compareTo(lastSubtitle.getEndTime()) > 0) {
            log.warn("The Timestamp :" + timestamp + " comes after the last subtitle of the srt file");
            throw new TimeStampOutOfBoundsException("The Timestamp : " + timestamp + " comes after the last subtitle of the srt file");
        }

        for (SubtitleParagraph subtitle : subtitles) {
            if (subtitle.getEndTime().compareTo(timestamp) > 0 && subtitle.getStartTime().compareTo(timestamp) < 0) {
                return Optional.of(subtitle);
            }
        }
        // we are at a gap between subtitles
        return Optional.empty();
    }
}
