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
	private final SubtitleTimestamps timestamps;
	private final SubtitleText text;

	/* Create a new Subtitle with the given start and end times. */
	public SubtitleParagraph(SubtitleTimestamps timestamps) {
		this.timestamps = timestamps;
		this.text = new SubtitleText(new ArrayList<>());
	}

    public SubtitleParagraph(SubtitleTimestamps timestamps, SubtitleText text) {
        this.timestamps = timestamps;
        this.text = text;
    }

	public Timestamp getStartTime(){
		return timestamps.getStart();
	}

	public Timestamp getEndTime(){
		return timestamps.getEnd();
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
