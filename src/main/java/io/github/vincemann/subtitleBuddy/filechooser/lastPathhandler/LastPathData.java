package io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LastPathData {
    private String lastPath;
}
