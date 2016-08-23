package com.tms.config.reader;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yzheng on 8/23/16.
 */

public class FileModifiedTimeStampTracker {
    private File file;
    private int checkPeriodInSecond;
    private AtomicLong lastModifiedTime;
    private AtomicLong lastCheckTime;

    public FileModifiedTimeStampTracker(File file, int checkPeriodInSecond) {
        this.file = file;
        this.checkPeriodInSecond = checkPeriodInSecond;
    }

    /**
     * get the last modified time of the file.
     *
     * If the time between two checks is less than the allowed checkPeriod,
     * then the lastModified time is the same as before.
     * Otherwise, get the file lastModifiedTime.
     *
     * @return
     */

    public Long getLastModifiedTime() {
        long now = System.currentTimeMillis();
        if (lastCheckTime != null && (now - this.lastCheckTime.longValue()) / 1000L < (long)this.checkPeriodInSecond) {
            return this.lastModifiedTime == null ? null : Long.valueOf(this.lastModifiedTime.longValue());
        } else {
            this.lastCheckTime = new AtomicLong(now);
            if (this.file.exists()) {
                this.lastModifiedTime = new AtomicLong(this.file.lastModified());
            } else {
                this.lastModifiedTime = null;
            }

            return this.lastModifiedTime == null ? null : Long.valueOf(this.lastModifiedTime.longValue());
        }
    }
}