package com.youneedsoftware.subtitleBuddy.systemCommandExecutor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ProcessResult {
    @Setter
    @Getter
    private String stdout="";
    @Setter
    @Getter
    private String stderr="";
    @Getter
    private String report ="";
    private int count = 1;

    public ProcessResult() { }

    public void addReport(String errorReport) {
        this.report += count+": " +System.lineSeparator()+ errorReport;
    }


}
