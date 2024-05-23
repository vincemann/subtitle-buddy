package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.common.base.Preconditions;
import io.github.vincemann.subtitlebuddy.srt.SrtDelimiter;
import io.github.vincemann.subtitlebuddy.srt.Subtitle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.SubtitleType;

import java.util.ArrayList;
import java.util.List;

public class SubtitleTextParserImpl implements SubtitleTextParser {

    private List<Subtitle> result = new ArrayList<>();
    private Subtitle currentSubtitle = null;
    private StringBuilder currentText = new StringBuilder();
    private SubtitleType currentSubtitleType = SubtitleType.NORMAL;

    private int amountCharsToSkip;

    @Override
    public SubtitleText parse(String subtitleString) throws InvalidDelimiterException {

        for (int i = 0; i < subtitleString.length(); i++) {
            char currentChar = subtitleString.charAt(i);
            if (currentChar == '<') {
                SrtDelimiter srtDelimiter = findDelimiterType(subtitleString.substring(i));
                addCurrSubtitle();
                switch (srtDelimiter) {
                    case ITALIC_START_DELIMITER:
                        skipDelimiter(ITALIC_START_DELIMITER);
                        currentSubtitleType = SubtitleType.ITALIC;
                        break;
                    case NEW_LINE_DELIMITER:
                        skipDelimiter(NEW_LINE_DELIMITER);
                        result.add(Subtitle.NEWLINE);
                        break;
                    case ITALIC_END_DELIMITER:
                        skipDelimiter(ITALIC_END_DELIMITER);
                        currentSubtitleType = SubtitleType.NORMAL;
                        break;
                    default:
                        throw new InvalidDelimiterException(subtitleString.substring(i) + " is an invalid delimiter");
                }
                // -1 bc loop will increment 1
                i += amountCharsToSkip - 1;
            } else {
                currentText.append(currentChar);
            }
        }
        addCurrSubtitle();
        return new SubtitleText(result);
    }

    private void skipDelimiter(String del){
        amountCharsToSkip = del.length();
    }

    private void addCurrSubtitle() {
        if (currentText.length() != 0) {
            currentSubtitle = new Subtitle(currentSubtitleType, currentText.toString());
            currentText.setLength(0);
        }
        if (currentSubtitle != null)
            result.add(currentSubtitle);
        currentSubtitle = null;
    }

    public SrtDelimiter findDelimiterType(String subText) throws InvalidDelimiterException {
        try {
            Preconditions.checkState(subText != null);
            Preconditions.checkState(!subText.isEmpty());
            Preconditions.checkState(subText.charAt(0) == '<');
            char currentChar = ' ';
            StringBuilder delimiter = new StringBuilder();
            int count = 0;
            while (currentChar != '>') {
                Preconditions.checkState(subText.length() > count);
                currentChar = subText.charAt(count);
                delimiter.append(currentChar);
                count++;
            }

            Preconditions.checkState(delimiter.length() >= 3 && delimiter.length() <= 4);
            switch (delimiter.toString()) {
                case ITALIC_START_DELIMITER:
                    return SrtDelimiter.ITALIC_START_DELIMITER;
                case ITALIC_END_DELIMITER:
                    return SrtDelimiter.ITALIC_END_DELIMITER;
                case NEW_LINE_DELIMITER:
                    return SrtDelimiter.NEW_LINE_DELIMITER;
            }
            // unknown delimiter
            throw new InvalidDelimiterException(delimiter + " is an invalid delimiter");
        } catch (IllegalStateException e) {
            throw new InvalidDelimiterException(e);
        }
    }
}
