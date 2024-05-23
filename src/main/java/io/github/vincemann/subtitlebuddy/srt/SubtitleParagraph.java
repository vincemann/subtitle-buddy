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
	private final int id;
	private final SubtitleTimestamps timestamps;
	private final SubtitleText text;

	/* Create a new Subtitle with the given start and end times. */
	public SubtitleParagraph(int id, SubtitleTimestamps timestamps) {
		this.id = id;
		this.timestamps = timestamps;
		this.text = new SubtitleText(new ArrayList<>());
	}

    public SubtitleParagraph(int id, SubtitleTimestamps timestamps, SubtitleText text) {
		this.id = id;
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
		result.append("id:").append(id).append("\n");
		result.append(timestamps.toString()).append("\n");
		result.append("amount subs: ").append(text.getSubtitles().size()).append("\n");
		result.append("text:\n");
		if (text.getSubtitles().size() < 6){
			for(Subtitle subtitle : text.getSubtitles()){
				result.append(subtitle.getText());
			}
		}else{
			int i = 0;
			for(Subtitle subtitle : text.getSubtitles()){
				if (i >= 6)
					break;
				result.append(subtitle.getText());
				i++;
			}
			result.append("...");
		}
		return result.toString();
	}
}
