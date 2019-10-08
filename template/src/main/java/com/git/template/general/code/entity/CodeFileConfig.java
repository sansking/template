package com.git.template.general.code.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 该类用于从配置文件中读取源码,备份文件,以及类路径所在的文件夹
 */
@ConfigurationProperties(prefix="code")
public class CodeFileConfig{
    private String srcPath;
    private String backupLocation;
    private String classpath;

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String SrcPath) {
        this.srcPath = SrcPath;
    }

    public String getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation) {
        this.backupLocation = backupLocation;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public CodeFileConfig(String srcLocation, String backupLocation, String classpath) {
        this.srcPath = srcLocation;
        this.backupLocation = backupLocation;
        this.classpath = classpath;
    }

    @Override
    public String toString() {
        return "CodeFileConfig [backupLocation=" + backupLocation + ", classpath=" + classpath + ", srcLocation="
                + srcPath + "]";
    }

    
}