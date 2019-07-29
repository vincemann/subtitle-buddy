package io.github.vincemann.subtitleBuddy.srt;

import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.InvalidTimestampFormatException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Timestamp implements Comparable<Timestamp> {
    private long hours, minutes, seconds, milliseconds;

    /* Create a new timestamp at the given time. */
    public Timestamp(long hours, long minutes, long seconds, long milliseconds) {
        this.setHours(hours);
        this.setMinutes(minutes);
        this.setSeconds(seconds);
        this.setMilliseconds(milliseconds);
        adjustTimeStamp();
    }

    public static Timestamp ZERO(){
        return new Timestamp(0,0,0,0);
    }

    /**
     * copy constructor
     * @param timestamp
     */
    public Timestamp(Timestamp timestamp){
        this(timestamp.getHours(),timestamp.getMinutes(),timestamp.getSeconds(),timestamp.getMilliseconds());
    }

    /* Create a new timestamp from the given string.
     * Uses the SRT timestamp format:
     * hours:minutes:seconds,milliseconds
     * eg. 00:00:28,400 */
    public Timestamp(String time) throws InvalidTimestampFormatException {
        String[] topParts = time.split(",");
        if (topParts.length != 2) throw new InvalidTimestampFormatException();
        String[] parts = topParts[0].split(":");
        if (parts.length != 3) throw new InvalidTimestampFormatException();

        try {
            this.setHours(Integer.parseInt(parts[0]));
            this.setMinutes(Integer.parseInt(parts[1]));
            this.setSeconds(Integer.parseInt(parts[2]));
            this.setMilliseconds(Integer.parseInt(topParts[1]));
            adjustTimeStamp();
        }catch (NumberFormatException e){
            throw new InvalidTimestampFormatException();
        }
    }
    
    public Timestamp(long milliSeconds){
        this(0,0,0,milliSeconds);
        adjustTimeStamp();
    }


    public String toAlarmClockString(){
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
    /**
     * sorgt dafür dass seconds nicht über 60 usw
     */
    private void adjustTimeStamp() {
        while(this.milliseconds>=1000) {
            this.seconds++;
            this.milliseconds-=1000;
        }
        while(this.seconds>=60) {
            this.minutes++;
            this.seconds-=60;
        }
        while(this.minutes>=60) {
            this.hours++;
            this.minutes-=60;
        }
        if(this.hours>=99) {
            throw new IllegalArgumentException("Ein Timestamp mit über 99 Stunden wurde erzeugt");
        }
        if(this.isNegative()) {
            long[] backup = new long[]{this.milliseconds, this.seconds, this.minutes, this.hours};
            while (this.milliseconds < 0) {
                this.seconds--;
                this.milliseconds = 1000 - (-this.milliseconds);
            }
            while (this.seconds < 0) {
                this.minutes--;
                this.seconds = 60 - (-this.seconds);
            }
            while (this.minutes < 0) {
                this.hours--;
                this.minutes = 60 - (-this.minutes);
            }
            if (this.hours < 0) {
                this.milliseconds = backup[0];
                this.seconds = backup[1];
                this.minutes = backup[2];
                this.hours = backup[3];
                while(this.milliseconds<=-1000) {
                    this.seconds--;
                    this.milliseconds+=1000;
                }
                while(this.seconds<=-60) {
                    this.minutes--;
                    this.seconds+=60;
                }
                while(this.minutes<=-60) {
                    this.hours--;
                    this.minutes+=60;
                }
                if(this.hours<=-99){
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public boolean isNegative(){
        if(this.milliseconds <0 || this.seconds<0||this.minutes<0||this.hours<0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(Timestamp o) {
        if (o.equals(this)) {
            return 0;
        } else if (this.toMilliSeconds()<o.toMilliSeconds()) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean equalBySeconds(Timestamp timestamp){
        return this.getSeconds() == timestamp.getSeconds()
                && this.getMinutes() == timestamp.getMinutes()
                && this.getHours() == timestamp.getHours();
    }

    public long toMilliSeconds(){
        return this.getMilliseconds()+this.getSeconds()*1000+this.getMinutes()*60*1000+this.getHours()*60*60*1000;
    }

    /* Compiles the timestamp to an SRT timestamp. */
    public String compile() {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "," + String.format("%03d", milliseconds);
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }


}
