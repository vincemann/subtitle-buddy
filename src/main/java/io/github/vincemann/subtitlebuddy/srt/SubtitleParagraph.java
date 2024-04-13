package io.github.vincemann.subtitlebuddy.srt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Represents a single subtitle paragraph in a subtitle file.
 * A paragraph consists of a start and end time, and the text of the subtitle.
 * Each paragraph can contain multiple lines of text and is displayed as a whole for the duration of the start and end time.
 */
@EqualsAndHashCode
@Getter
@Setter
public class SubtitleParagraph {
	private final Timestamp startTime, endTime;
	private final SubtitleText text;
	
	/* Create a new Subtitle with the given start and end times. */
	public SubtitleParagraph(Timestamp startTime, Timestamp endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.text = new SubtitleText(new ArrayList<>());
	}

    public SubtitleParagraph(Timestamp startTime, Timestamp endTime, SubtitleText text) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    @Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(List<SubtitleSegment> subtitleSegments: text.getSubtitleSegments()){
			for(SubtitleSegment subtitleSegment: subtitleSegments){
				result.append(subtitleSegment.getText());
			}
			result.append(System.lineSeparator());
		}
		return result.toString();
	}
}
