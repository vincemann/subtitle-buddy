package com.youneedsoftware.subtitleBuddy.srt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@EqualsAndHashCode
@Getter
@Setter
public class Subtitle {
	private final Timestamp startTime, endTime;
	private final SubtitleText subtitleText;
	
	/* Create a new Subtitle with the given start and end times. */
	public Subtitle(Timestamp startTime, Timestamp endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.subtitleText = new SubtitleText(new ArrayList<>());
	}

    public Subtitle(Timestamp startTime, Timestamp endTime, SubtitleText subtitleText) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.subtitleText = subtitleText;
    }

    @Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(List<SubtitleSegment> subtitleSegments: subtitleText.getSubtitleSegments()){
			for(SubtitleSegment subtitleSegment: subtitleSegments){
				result.append(subtitleSegment.getText());
			}
			result.append(System.lineSeparator());
		}
		return result.toString();
	}



	public static String formatLine(String line) {
		/* Replace CRLF with LF for neatness. */
		line = line.replace("\r\n", "\n");
		
		/* Empty line marks the end of a subtitle, replace it with a space.  */
		line = line.replace("\n\n", "\n \n");
		
		return line;
	}
}
